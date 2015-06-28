package proyecto.utad.mapony.inferencia;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.elasticsearch.hadoop.mr.EsOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import proyecto.utad.mapony.inferencia.map.MaponyInferenciaMap;
import util.ElasticSearchClient;
import util.GeoHashCiudad;
import util.constantes.MaponyCte;
import util.reducers.FlickReducer;
import util.writables.CustomWritable;

//TODO mvn eclipse:eclipse -DdownloadJavadocs -DdownloadSources
public class MaponyInferenciaJob extends Configured implements Tool {

	private static Properties properties;
	private static String indexES;
	private static String typeES;
	private static String clusterName;
	private static final Logger logger = LoggerFactory.getLogger(MaponyInferenciaJob.class);
	private String rutaFicheros;
//	private String rutaPaises;

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
		indexES = properties.getProperty(MaponyCte.indice);
		typeES = properties.getProperty(MaponyCte.tipo);
		clusterName = properties.getProperty(MaponyCte.cluster);
		
		new GeoHashCiudad(properties.getProperty(MaponyCte.paises)); 
	}

	public int run(String[] args) throws Exception {
		// Lectura de las properties de configuracion
		setRutaFicheros(properties.getProperty(MaponyCte.datos));
//		setRutaPaises(properties.getProperty(Constantes.paises));

		final String ip = properties.getProperty(MaponyCte.ip);
		final String port = properties.getProperty(MaponyCte.puerto);
		final int numeroReducer = Integer.parseInt(properties.getProperty(MaponyCte.reducers));

		// Creamos el job
		Job job = Job.getInstance(getConf(), MaponyCte.jobNameMainJob);
		job.setJarByClass(MaponyInferenciaJob.class);

		Path pathOrigen = new Path(getRutaFicheros());

		Configuration conf = job.getConfiguration();

		//TODO Con MultipleInputs de verdad, descomentar este bloque
//		final FileSystem fs = FileSystem.get(new URI("hdfs://quickstart.cloudera:8020/"),
//				conf);
//
//		// Recuperamos los datos del path origen
//		FileStatus[] glob = fs.globStatus(pathOrigen);
//
//		// Si tenemos datos...
//		if (null != glob) {
//			if (glob.length > 0) {
//				for (FileStatus fileStatus : glob) {
//					Path pFich = fileStatus.getPath();
//					// MultipleInputs
//					MultipleInputs.addInputPath(job, pFich, TextInputFormat.class, MaponyMap.class);
//				}
//			}
//		}
		
		// conf.set("fs.defaultFS", "hdfs://localhost.localdomain:8020");
		conf.setBoolean("mapred.map.tasks.speculative.execution", false);
		conf.setBoolean("mapred.reduce.tasks.speculative.execution", false);
		// conf.set("es.input.json", "yes");
		conf.set("key.value.separator.in.input.line", " ");
		conf.set("es.nodes", ip + ":" + port);
		conf.set("es.resource", indexES + "/" + typeES);

		// Output a Elastic Search Output Format
		job.setOutputFormatClass(EsOutputFormat.class);

		//TODO con MultipleInputs de verdad, comentar esta linea
		MultipleInputs.addInputPath(job, pathOrigen, SequenceFileInputFormat.class, MaponyInferenciaMap.class);

		// Salida del mapper
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(CustomWritable.class);

		// Salida del reducer
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(MapWritable.class);

		// Mapper y Reducer
		job.setReducerClass(FlickReducer.class);

		// Numero de Reducers
		job.setNumReduceTasks(numeroReducer);

		job.waitForCompletion(true);
		getLogger().info(MaponyCte.getMsgFinJob(MaponyCte.jobNameMainJob));

		return 1;
	}

	public static void main(final String args[]) throws Exception {
//		if (args.length != 1) {
//			System.out.printf("Uso: <config.properties file>\n");
//			System.exit(-1);
//		}
//		loadProperties(args[0]);
		
		loadProperties(MaponyCte.propiedades);
		
		getLogger().info(MaponyCte.MSG_PROPIEDADES_CARGADAS);
		new ElasticSearchClient(indexES, typeES, clusterName);
		ToolRunner.run(new MaponyInferenciaJob(), args);
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

//	/**
//	 * @param rutaPaises the rutaPaises to set
//	 */
//	private final void setRutaPaises(String rutaPaises) {
//		this.rutaPaises = rutaPaises;
//	}

	/**
	 * @return the logger
	 */
	private static final Logger getLogger() {
		return logger;
	}
}
