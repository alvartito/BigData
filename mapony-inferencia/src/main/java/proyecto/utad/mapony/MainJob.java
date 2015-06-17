package proyecto.utad.mapony;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

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
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.hadoop.mr.EsOutputFormat;
import org.elasticsearch.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import proyecto.utad.mapony.maps.MaponyMap;
import proyecto.utad.mapony.reducers.FlickReducer;
import writables.CustomWritable;

//TODO mvn eclipse:eclipse -DdownloadJavadocs -DdownloadSources
public class MainJob extends Configured implements Tool {

	private static Properties properties;
	private static String indexES;
	private static String typeES;
	private static String clusterName;
	private static Client client;
	private static final Logger logger = LoggerFactory.getLogger(MainJob.class);
	private String rutaFicheros;

	private static void loadProperties(String fileName) throws IOException {
		if (null == properties) {
			properties = new Properties();
		}
		try {
			FileInputStream in = new FileInputStream(fileName);
			properties.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		indexES = properties.getProperty("index_name");
		typeES = properties.getProperty("type_name");
		clusterName = properties.getProperty("clusterName");
	}

	public int run(String[] args) throws Exception {
		Node node = nodeBuilder().clusterName(clusterName).client(true).node();
		// Node node1 = nodeBuilder().node();
		// Settings set = node1.settings();

		client = node.client();
		logger.info("\nComprobación de Indices\n");
		if (isIndexExist(indexES)) {
			deleteIndex(client, indexES);
			createIndex(indexES);
			logger.info("Indice: " + indexES + "/" + typeES + " borrado y creado");
		} else {
			createIndex(indexES);
			logger.info("Indice: " + indexES + "/" + typeES + " nuevo y creado");
		}

		logger.info("\nMainJob START");
		// Lectura de las properties de configuracion
		setRutaFicheros(properties.getProperty("ruta_ficheros"));

		String ip = properties.getProperty("ip");
		String port = properties.getProperty("port");
		String numeroReducer = properties.getProperty("numero_reducer");

		// TODO Ficheros de entrada. Recepciona la ruta de la que hay que procesar el contenido. Mirar en los
		// proyectos
		// entregados.
		Path input_1 = new Path(getRutaFicheros());
		// Creamos el job
		Job job = Job.getInstance(getConf(), "Flickrjob");
		// Job job = new Job(getConf(), "Flickrjob");
		job.setJarByClass(MainJob.class);

		Configuration conf = job.getConfiguration();
		// conf.set("fs.defaultFS", "hdfs://localhost.localdomain:8020");
		conf.setBoolean("mapred.map.tasks.speculative.execution", false);
		conf.setBoolean("mapred.reduce.tasks.speculative.execution", false);
		// conf.set("es.input.json", "yes");
		conf.set("key.value.separator.in.input.line", " ");
		conf.set("es.nodes", ip + ":" + port);
		conf.set("es.resource", indexES + "/" + typeES);

		// MultipleInputs
		MultipleInputs.addInputPath(job, input_1, TextInputFormat.class, MaponyMap.class);

		// Output a Elastic Search Output Format
		job.setOutputFormatClass(EsOutputFormat.class);

		// Salida del mapper
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(CustomWritable.class);

		// Salida del reducer
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(MapWritable.class);

		// Mapper y Reducer
		job.setReducerClass(FlickReducer.class);

		// Numero de Reducers
		job.setNumReduceTasks(Integer.parseInt(numeroReducer));

		job.waitForCompletion(true);
		logger.info("MainJob FINISH SUCCESS");

		return 1;
	}

	public static void main(String args[]) throws Exception {

		if (args.length != 1) {
			System.out.printf("Uso: <config.properties file>\n");
			System.exit(-1);
		}
		//		loadProperties("job.properties");
		loadProperties(args[0]);
		logger.info("Propiedades cargadas");
		// new ClienteES(indexES, typeES, clusterName);
		ToolRunner.run(new MainJob(), args);
		System.exit(1);
	}

	private boolean isIndexExist(String index) {
		ActionFuture<IndicesExistsResponse> exists = client.admin().indices().exists(new IndicesExistsRequest(index));
		IndicesExistsResponse actionGet = exists.actionGet();
		return actionGet.isExists();
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
	private final void setRutaFicheros(String rutaFicheros) {
		this.rutaFicheros = rutaFicheros;
	}

	private void createIndex(String index) {
		XContentBuilder typemapping = ClienteES.buildJsonMappings();
		XContentBuilder settings = ClienteES.buildJsonSettings();

		client.admin().indices().create(new CreateIndexRequest(index).settings(settings).mapping(typeES, typemapping)).actionGet();

	}

	private void deleteIndex(Client client, String index) {
		try {
			DeleteIndexResponse delete = client.admin().indices().delete(new DeleteIndexRequest(index)).actionGet();
			if (!delete.isAcknowledged()) {
			} else {
			}
		} catch (Exception e) {
		}
	}

}
