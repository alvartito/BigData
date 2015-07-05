package proyecto.utad.mapony.pruebas;

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

import proyecto.utad.mapony.csv.MaponyCsvJob;
import proyecto.utad.mapony.pruebas.map.MaponyPruebasMap;
import util.constantes.MaponyCte;
import util.reducers.MaponyRed;

//TODO mvn eclipse:eclipse -DdownloadJavadocs -DdownloadSources
public class MaponyPruebasJob extends Configured implements Tool {

	private static Properties properties;
	private static String indexES;
	private static String typeES;
	private static String clusterName;
	private static final Logger logger = LoggerFactory.getLogger(MaponyPruebasJob.class);
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
//		indexES = properties.getProperty(MaponyCte.indice);
//		typeES = properties.getProperty(MaponyCte.tipo);
//		clusterName = properties.getProperty(MaponyCte.cluster);
//
//		new GeoHashCiudad(properties.getProperty(MaponyCte.paises));
	}

	public int run(String[] args) throws Exception {
//		setRutaFicheros(properties.getProperty(MaponyCte.datos));

		Configuration config = getConf();

		Path pathOrigen = new Path("data/allCountries.txt");
		Path outPath = new Path("data/pruebas");

		// Borramos todos los directorios que puedan existir
		FileSystem.get(outPath.toUri(), config).delete(outPath, true);
		
		Job job = Job.getInstance(config, MaponyCte.jobNamePruebas);
		job.setJarByClass(MaponyCsvJob.class);

		job.setInputFormatClass(TextInputFormat.class);
//		job.setOutputFormatClass(TextOutputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);
		SequenceFileOutputFormat.setCompressOutput(job, true);
		SequenceFileOutputFormat.setOutputCompressorClass(job, BZip2Codec.class);
		SequenceFileOutputFormat.setOutputCompressionType(job, CompressionType.BLOCK);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		MultipleInputs.addInputPath(job, pathOrigen, TextInputFormat.class, MaponyPruebasMap.class);
//		MultipleInputs.addInputPath(job, pathOrigen, SequenceFileInputFormat.class, MaponyPruebasMap.class);
		
		job.setCombinerClass(MaponyRed.class);
		job.setReducerClass(MaponyRed.class);
		FileOutputFormat.setOutputPath(job, outPath);

		job.waitForCompletion(true);

		getLogger().info(MaponyCte.getMsgFinJob(MaponyCte.jobNamePruebas));

		return 0;
	}

	public static void main(final String args[]) throws Exception {
		// if (args.length != 1) {
		// System.out.printf("Uso: <config.properties file>\n");
		// System.exit(-1);
		// }
		// loadProperties(args[0]);

		loadProperties(MaponyCte.propiedades);

		getLogger().info(MaponyCte.MSG_PROPIEDADES_CARGADAS);
//		new ElasticSearchClient(indexES, typeES, clusterName);
		ToolRunner.run(new MaponyPruebasJob(), args);
		System.exit(1);
	}

	/**
	 * @return the rutaFicheros
	 */
	private final String getRutaFicheros() {
		return rutaFicheros;
	}

	/**
	 * @param rutaFicheros
	 *            the rutaFicheros to set
	 */
	private final void setRutaFicheros(final String rutaFicheros) {
		this.rutaFicheros = rutaFicheros;
	}

	// /**
	// * @param rutaPaises the rutaPaises to set
	// */
	// private final void setRutaPaises(String rutaPaises) {
	// this.rutaPaises = rutaPaises;
	// }

	/**
	 * @return the logger
	 */
	private static final Logger getLogger() {
		return logger;
	}
}
