package proyecto.utad.mapony.groupNear;

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
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import proyecto.utad.mapony.groupNear.map.MaponyGroupNearMap;
import util.beans.RawDataWritable;
import util.constantes.MaponyCte;
import util.reducers.MaponyGroupNearRed;

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
	}
	
	
	public int run(String[] args) throws Exception {
//		setRutaFicheros(properties.getProperty(MaponyCte.datos));

		Configuration config = getConf();

		Path outPath = new Path(properties.getProperty(MaponyCte.salida_group_near));

		// Borramos todos los directorios que puedan existir
		FileSystem.get(outPath.toUri(), config).delete(outPath, true);
		
		Job jobMaponyGroupNear = Job.getInstance(config, MaponyCte.jobNameGroupNear);
		jobMaponyGroupNear.setJarByClass(MaponyGroupNearJob.class);

		jobMaponyGroupNear.setInputFormatClass(TextInputFormat.class);
		jobMaponyGroupNear.setOutputFormatClass(SequenceFileOutputFormat.class);
		SequenceFileOutputFormat.setCompressOutput(jobMaponyGroupNear, true);
		SequenceFileOutputFormat.setOutputCompressorClass(jobMaponyGroupNear, BZip2Codec.class);
		SequenceFileOutputFormat.setOutputCompressionType(jobMaponyGroupNear, CompressionType.BLOCK);

		jobMaponyGroupNear.setMapOutputKeyClass(Text.class);
		jobMaponyGroupNear.setMapOutputValueClass(RawDataWritable.class);

		jobMaponyGroupNear.setOutputKeyClass(Text.class);
		jobMaponyGroupNear.setOutputValueClass(RawDataWritable.class);

		MultipleInputs.addInputPath(jobMaponyGroupNear, new Path("data/yfcc100m_dataset-0.bz2"), TextInputFormat.class, MaponyGroupNearMap.class);
		MultipleInputs.addInputPath(jobMaponyGroupNear, new Path("data/yfcc100m_dataset-1.bz2"), TextInputFormat.class, MaponyGroupNearMap.class);
		MultipleInputs.addInputPath(jobMaponyGroupNear, new Path("data/yfcc100m_dataset-2.bz2"), TextInputFormat.class, MaponyGroupNearMap.class);
		MultipleInputs.addInputPath(jobMaponyGroupNear, new Path("data/yfcc100m_dataset-3.bz2"), TextInputFormat.class, MaponyGroupNearMap.class);
		MultipleInputs.addInputPath(jobMaponyGroupNear, new Path("data/yfcc100m_dataset-4.bz2"), TextInputFormat.class, MaponyGroupNearMap.class);
		MultipleInputs.addInputPath(jobMaponyGroupNear, new Path("data/yfcc100m_dataset-5.bz2"), TextInputFormat.class, MaponyGroupNearMap.class);
		MultipleInputs.addInputPath(jobMaponyGroupNear, new Path("data/yfcc100m_dataset-6.bz2"), TextInputFormat.class, MaponyGroupNearMap.class);
		MultipleInputs.addInputPath(jobMaponyGroupNear, new Path("data/yfcc100m_dataset-7.bz2"), TextInputFormat.class, MaponyGroupNearMap.class);
		MultipleInputs.addInputPath(jobMaponyGroupNear, new Path("data/yfcc100m_dataset-8.bz2"), TextInputFormat.class, MaponyGroupNearMap.class);
		MultipleInputs.addInputPath(jobMaponyGroupNear, new Path("data/yfcc100m_dataset-9.bz2"), TextInputFormat.class, MaponyGroupNearMap.class);

		jobMaponyGroupNear.setCombinerClass(MaponyGroupNearRed.class);
		jobMaponyGroupNear.setReducerClass(MaponyGroupNearRed.class);

		FileOutputFormat.setOutputPath(jobMaponyGroupNear, outPath);

		jobMaponyGroupNear.waitForCompletion(true);

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
