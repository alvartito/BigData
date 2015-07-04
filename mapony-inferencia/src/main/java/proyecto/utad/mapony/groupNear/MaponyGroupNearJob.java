package proyecto.utad.mapony.groupNear;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.ArrayWritable;
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

import proyecto.utad.mapony.groupNear.map.MaponyGroupNearMap;
import util.GeoHashCiudad;
import util.constantes.MaponyCte;
import util.reducers.MaponyGNArrayRed;
import util.writables.RawDataWritable;

public class MaponyGroupNearJob extends Configured implements Tool {

	private static Properties properties;
	private static final Logger logger = LoggerFactory.getLogger(MaponyGroupNearJob.class);
//	private String rutaFicheros;

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
//		setRutaFicheros(properties.getProperty(MaponyCte.datos));

		Configuration config = getConf();

		Path outPath = new Path(properties.getProperty(MaponyCte.salida_group_near));

		// Borramos todos los directorios que puedan existir
		FileSystem.get(outPath.toUri(), config).delete(outPath, true);
		
		Job job = Job.getInstance(config, MaponyCte.jobNameGroupNear);
		job.setJarByClass(MaponyGroupNearJob.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
//		jobMaponyGroupNear.setOutputFormatClass(SequenceFileOutputFormat.class);
//		SequenceFileOutputFormat.setCompressOutput(jobMaponyGroupNear, true);
//		SequenceFileOutputFormat.setOutputCompressorClass(jobMaponyGroupNear, BZip2Codec.class);
//		SequenceFileOutputFormat.setOutputCompressionType(jobMaponyGroupNear, CompressionType.BLOCK);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(RawDataWritable.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(ArrayWritable.class);

		MultipleInputs.addInputPath(job, new Path("data/sample"), TextInputFormat.class, MaponyGroupNearMap.class);
//		MultipleInputs.addInputPath(job, new Path("data/yfcc100m_dataset-0.bz2"), TextInputFormat.class, MaponyGroupNearMap.class);
//		MultipleInputs.addInputPath(job, new Path("data/yfcc100m_dataset-1.bz2"), TextInputFormat.class, MaponyGroupNearMap.class);
//		MultipleInputs.addInputPath(job, new Path("data/yfcc100m_dataset-2.bz2"), TextInputFormat.class, MaponyGroupNearMap.class);
//		MultipleInputs.addInputPath(job, new Path("data/yfcc100m_dataset-3.bz2"), TextInputFormat.class, MaponyGroupNearMap.class);
//		MultipleInputs.addInputPath(job, new Path("data/yfcc100m_dataset-4.bz2"), TextInputFormat.class, MaponyGroupNearMap.class);
//		MultipleInputs.addInputPath(job, new Path("data/yfcc100m_dataset-5.bz2"), TextInputFormat.class, MaponyGroupNearMap.class);
//		MultipleInputs.addInputPath(job, new Path("data/yfcc100m_dataset-6.bz2"), TextInputFormat.class, MaponyGroupNearMap.class);
//		MultipleInputs.addInputPath(job, new Path("data/yfcc100m_dataset-7.bz2"), TextInputFormat.class, MaponyGroupNearMap.class);
//		MultipleInputs.addInputPath(job, new Path("data/yfcc100m_dataset-8.bz2"), TextInputFormat.class, MaponyGroupNearMap.class);
//		MultipleInputs.addInputPath(job, new Path("data/yfcc100m_dataset-9.bz2"), TextInputFormat.class, MaponyGroupNearMap.class);

//		job.setCombinerClass(MaponyGroupNearRed.class);
		job.setReducerClass(MaponyGNArrayRed.class);

		job.setNumReduceTasks(6);
		
		FileOutputFormat.setOutputPath(job, outPath);

		job.waitForCompletion(true);

		getLogger().info(MaponyCte.getMsgFinJob(MaponyCte.jobNameGroupNear));

		return 0;
	}

	public static void main(String args[]) throws Exception {
		loadProperties(MaponyCte.propiedades);
		
		getLogger().info(MaponyCte.MSG_PROPIEDADES_CARGADAS);

		ToolRunner.run(new MaponyGroupNearJob(), args);
		System.exit(1);

	}

	/**
	 * @return the logger
	 */
	private static final Logger getLogger() {
		return logger;
	}
//
//	/**
//	 * @return the rutaFicheros
//	 */
//	private final String getRutaFicheros() {
//		return rutaFicheros;
//	}
//
//
//	/**
//	 * @param rutaFicheros the rutaFicheros to set
//	 */
//	private final void setRutaFicheros(String rutaFicheros) {
//		this.rutaFicheros = rutaFicheros;
//	}
}
