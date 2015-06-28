package proyecto.utad.mapony;

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
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.elasticsearch.hadoop.mr.EsOutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import proyecto.utad.mapony.maps.MaponyMap;
import proyecto.utad.mapony.reducers.FlickReducer;
import writables.CustomWritable;
import aux.GeoHashCiudad;
import constantes.Constantes;

//TODO mvn eclipse:eclipse -DdownloadJavadocs -DdownloadSources
public class MainJob extends Configured implements Tool {

	private static Properties properties;
	private static String indexES;
	private static String typeES;
	private static String clusterName;
	private static final Logger logger = LoggerFactory.getLogger(MainJob.class);
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
		indexES = properties.getProperty(Constantes.indice);
		typeES = properties.getProperty(Constantes.tipo);
		clusterName = properties.getProperty(Constantes.cluster);
		
		new GeoHashCiudad(properties.getProperty(Constantes.paises)); 
	}

	public int run(String[] args) throws Exception {
		// Lectura de las properties de configuracion
		setRutaFicheros(properties.getProperty(Constantes.datos));
//		setRutaPaises(properties.getProperty(Constantes.paises));

		final String ip = properties.getProperty(Constantes.ip);
		final String port = properties.getProperty(Constantes.puerto);
		final int numeroReducer = Integer.parseInt(properties.getProperty(Constantes.reducers));

		// Creamos el job
		Job job = Job.getInstance(getConf(), Constantes.jobName);
		job.setJarByClass(MainJob.class);

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
		MultipleInputs.addInputPath(job, pathOrigen, TextInputFormat.class, MaponyMap.class);

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
		getLogger().info(Constantes.MSG_FIN_JOB);

		return 1;
	}

	public static void main(final String args[]) throws Exception {
//		if (args.length != 1) {
//			System.out.printf("Uso: <config.properties file>\n");
//			System.exit(-1);
//		}
//		loadProperties(args[0]);
		
		loadProperties(Constantes.propiedades);
		
		getLogger().info(Constantes.MSG_PROPIEDADES_CARGADAS);
		new ElasticSearchClient(indexES, typeES, clusterName);
		ToolRunner.run(new MainJob(), args);
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