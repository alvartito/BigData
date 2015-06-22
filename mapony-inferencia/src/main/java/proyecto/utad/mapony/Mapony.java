package proyecto.utad.mapony;

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

import proyecto.utad.mapony.maps.MaponyCsvMap;
import proyecto.utad.mapony.reducers.MaponyRed;
import aux.GeoHashCiudad;
import constantes.Constantes;

public class Mapony extends Configured implements Tool {

	private static Properties properties;
	private static final Logger logger = LoggerFactory.getLogger(Mapony.class);
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

		new GeoHashCiudad(properties.getProperty(Constantes.paises)); 
	}
	
	
	public int run(String[] args) throws Exception {
		setRutaFicheros(properties.getProperty(Constantes.datos));

		Configuration config = getConf();

		Path pathOrigen = new Path(getRutaFicheros());
		Path outPath = new Path("data/csv");

		// Borramos todos los directorios que puedan existir
		FileSystem.get(outPath.toUri(), config).delete(outPath, true);
		
		Job jobMapony = Job.getInstance(config, "Mapony Job");
		jobMapony.setJarByClass(Mapony.class);

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

		System.out.println("Fin ejecucion para el job mapony");

		return 0;
	}

	public static void main(String args[]) throws Exception {
		loadProperties(Constantes.propiedades);
		
		getLogger().info(Constantes.MSG_PROPIEDADES_CARGADAS);

		ToolRunner.run(new Mapony(), args);
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
