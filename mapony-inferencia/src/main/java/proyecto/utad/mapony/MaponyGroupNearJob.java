package proyecto.utad.mapony;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.BZip2Codec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import proyecto.utad.mapony.maps.MaponyCsvMap;
import proyecto.utad.mapony.maps.MaponyGroupNearMap;
import proyecto.utad.mapony.reducers.MaponyRed;
import aux.GeoHashCiudad;
import constantes.Constantes;

public class MaponyGroupNearJob extends Configured implements Tool {

	private static Properties properties;
	private static final Logger logger = LoggerFactory.getLogger(MaponyGroupNearJob.class);
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
		Path outPath = new Path("data/gnj");

		// conf.set("fs.defaultFS", "hdfs://localhost.localdomain:8020");
		
		// Borramos todos los directorios que puedan existir
		FileSystem.get(outPath.toUri(), config).delete(outPath, true);
		
		Job jobMaponyGroupNear = Job.getInstance(config, "Mapony Group Near Job");
		jobMaponyGroupNear.setJarByClass(MaponyGroupNearJob.class);

		jobMaponyGroupNear.setInputFormatClass(TextInputFormat.class);
		jobMaponyGroupNear.setOutputFormatClass(SequenceFileOutputFormat.class);
		SequenceFileOutputFormat.setCompressOutput(jobMaponyGroupNear, true);
		SequenceFileOutputFormat.setOutputCompressorClass(jobMaponyGroupNear, BZip2Codec.class);
		SequenceFileOutputFormat.setOutputCompressionType(jobMaponyGroupNear, CompressionType.BLOCK);

		jobMaponyGroupNear.setMapOutputKeyClass(Text.class);
		jobMaponyGroupNear.setMapOutputValueClass(Text.class);

		jobMaponyGroupNear.setOutputKeyClass(Text.class);
//		jobMaponyGroupNear.setOutputValueClass(Text.class);

		MultipleInputs.addInputPath(jobMaponyGroupNear, pathOrigen, TextInputFormat.class, MaponyGroupNearMap.class);

		jobMaponyGroupNear.setCombinerClass(MaponyRed.class);
		jobMaponyGroupNear.setReducerClass(MaponyRed.class);

//		jobMapony.setNumReduceTasks(6);

//		FileInputFormat.addInputPath(jobMapony, inputPath);
		FileOutputFormat.setOutputPath(jobMaponyGroupNear, outPath);

		jobMaponyGroupNear.waitForCompletion(true);

		System.out.println("Fin ejecucion para el job mapony");

		return 0;
	}

	public static void main(String args[]) throws Exception {
		loadProperties(Constantes.propiedades);
		
		getLogger().info(Constantes.MSG_PROPIEDADES_CARGADAS);

		ToolRunner.run(new MaponyGroupNearJob(), args);
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
