package proyecto.utad.mapony.csv;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import proyecto.utad.mapony.csv.map.MaponyCsvMap;
import util.GeoHashCiudad;
import util.constantes.MaponyCte;
import util.reducers.MaponyRed;

public class MaponyCsvJob extends Configured implements Tool {

	private static Properties properties;
	private static final Logger logger = LoggerFactory.getLogger(MaponyCsvJob.class);
	private String rutaFicheros;
	
	
	private static void loadProperties(final String fileName) throws IOException {
		if (null == properties) {
			properties = new Properties();
		}
		try {
			FileInputStream in = new FileInputStream(fileName);
			properties.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		new GeoHashCiudad(properties.getProperty(MaponyCte.paises)); 
	}
	
	
	public int run(String[] args) throws Exception {
		setRutaFicheros(properties.getProperty(MaponyCte.datos));

		Configuration config = getConf();

		Path pathOrigen = new Path(getRutaFicheros());
		Path outPath = new Path("data/csv");

		// Borramos todos los directorios que puedan existir
		FileSystem.get(outPath.toUri(), config).delete(outPath, true);
		
		Job jobMapony = Job.getInstance(config, MaponyCte.jobNameCsv);
		jobMapony.setJarByClass(MaponyCsvJob.class);

		jobMapony.setInputFormatClass(TextInputFormat.class);
		jobMapony.setOutputFormatClass(TextOutputFormat.class);
		
		jobMapony.setMapOutputKeyClass(Text.class);
		jobMapony.setMapOutputValueClass(Text.class);

		jobMapony.setOutputKeyClass(Text.class);
		jobMapony.setOutputValueClass(Text.class);

		MultipleInputs.addInputPath(jobMapony, pathOrigen, TextInputFormat.class, MaponyCsvMap.class);

//		jobMapony.setMapperClass(MaponyMap.class);
		jobMapony.setReducerClass(MaponyRed.class);

//		jobMapony.setNumReduceTasks(6);

//		FileInputFormat.addInputPath(jobMapony, inputPath);
		FileOutputFormat.setOutputPath(jobMapony, outPath);

		jobMapony.waitForCompletion(true);

		getLogger().info(MaponyCte.getMsgFinJob(MaponyCte.jobNameCsv));

		return 0;
	}

	public static void main(String args[]) throws Exception {
		loadProperties(MaponyCte.propiedades);
		
		getLogger().info(MaponyCte.MSG_PROPIEDADES_CARGADAS);

		ToolRunner.run(new MaponyCsvJob(), args);
		System.exit(1);

	}


	/**
	 * @return the logger
	 */
	private static final Logger getLogger() {
		return logger;
	}


	/**
	 * @return the rutaFicheros
	 */
	private final String getRutaFicheros() {
		return rutaFicheros;
	}


	/**
	 * @param rutaFicheros the rutaFicheros to set
	 */
	private final void setRutaFicheros(String rutaFicheros) {
		this.rutaFicheros = rutaFicheros;
	}
}
