package proyecto.utad.mapony;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.IOException;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearchClient {

	private String index;
	private String type;
	private Client client;
	private Node node;
	private final Logger logger = LoggerFactory.getLogger(ElasticSearchClient.class);

	/**
	 * Crea la conexión con ES, en un cluster determinado, creando un indice que llega como parámetro.
	 * 
	 * @param index
	 *            - Indice.
	 * @param type
	 *            - tipo.
	 * @param clusterName
	 *            - Nombre del cluster de ES.
	 */
	public ElasticSearchClient(String index, String type, String clusterName) {
		setIndex(index);
		setType(type);
		setNode(nodeBuilder().clusterName(clusterName).client(true).node());
		setClient(getNode().client());
		if (existeIndiceEnElCluster()) {
			borrarIndice();
			crearIndice();
			getLogger().info("Indice: " + getIndex() + "/" + getType() + " borrado y creado");
		} else {
			crearIndice();
			getLogger().info("Indice: " + getIndex() + "/" + getType() + " nuevo y creado");
		}
	}

	/**
	 * Comprueba si el indice que queremos dar de alta, ya existe en el cluster.
	 * 
	 * @param index
	 *            - Indice a comprobar.
	 * @return true si ya existe.
	 */
	private boolean existeIndiceEnElCluster() {
		ActionFuture<IndicesExistsResponse> existe = getClient().admin().indices().exists(new IndicesExistsRequest(getIndex()));
		IndicesExistsResponse actionGet = existe.actionGet();
		return actionGet.isExists();
	}

	
	/**
	 * Crea el indice que llega como parametro en el cluster de ES.
	 * 
	 * @param index
	 *            - Indice a crear.
	 */
	private void crearIndice() {
		XContentBuilder typemapping = buildJsonMappings();
		XContentBuilder settings = buildJsonSettings();

		getClient().admin().indices().create(new CreateIndexRequest(getIndex()).settings(settings).mapping(getType(), typemapping)).actionGet();
	}

	/**
	 * Método que borra, si existe, el indice en el cluster de ES.
	 * 
	 * @param client
	 * @param index
	 */
	private void borrarIndice() {
		try {
			DeleteIndexResponse delete = getClient().admin().indices().delete(new DeleteIndexRequest(getIndex())).actionGet();
			if (!delete.isAcknowledged()) {
			} else {
			}
		} catch (Exception e) {
		}
	}

	public static XContentBuilder buildJsonSettings() {
		XContentBuilder builder = null;
		try {
			builder = XContentFactory.jsonBuilder();
			builder.startObject()
						.startObject("index")
							.startObject("analysis")
								.startObject("analyzer")
									.startObject("custom_analyser")
										.field("tokenizer", "standard")
										.field("char_filter","html_strip")
										.field("type", "custom")
										.field("filter", new String[] { "lowercase","unique", "max_length", "my_stop" })
									.endObject()
								.endObject()
								.startObject("filter")
									.startObject("my_stop")
										.field("type", "stop")
										.field("stopwords",new String[] { "_english_" })
									.endObject()
								.endObject()
								.startObject("filter")
									.startObject("max_length")
										.field("type", "length")
										.field("max",10)
										.field("min",4)
									.endObject()
								.endObject()
							.endObject()
						.endObject()
					.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder;
	}
	
	public static  XContentBuilder buildJsonMappings() {
		XContentBuilder builder = null;
		try {

			builder = XContentFactory.jsonBuilder();
			builder.startObject()
						.startObject("_id")
							.field("path", "id")
						.endObject()
						.startObject("_all")
							.field("enabled", true)
							.field("analyzer", "custom_analyser")
						.endObject()
						.startObject("properties")
							.startObject("tags")
								.field("type", "string")
								.field("store", "true")
								.field("index", "analyzed")
								.field("ignore_above", 200)
							.endObject()
							.startObject("descripcion")
								.field("type", "string")
								.field("store", "true")
								.field("index", "analyzed")
								.field("ignore_above", 200)
							.endObject()
							.startObject("foto")
								.field("type", "string")
								.field("store", true)
								.field("index", "not_analyzed")
								.field("include_in_all", false)
							.endObject()
							.startObject("id")
								.field("type", "string")
								.field("store", true)
								.field("index", "not_analyzed")
								.field("include_in_all",false)
							.endObject()
							.startObject("titulo")
								.field("type", "string")
								.field("store", true)
								.field("index", "analyzed")
							.endObject()
							.startObject("ciudad")
								.field("type", "string")
								.field("store", true)
								.field("index", "not_analyzed")
							.endObject()
							.startObject("location")
								.field("type", "geo_point")
								.field("geohash", true)
								.field("geohash_prefix", true)
								.field("include_in_all", false)
								.field("geohash_precision", 10)
								.field("lat_lon", "true")
							.endObject()
						.endObject()
					.endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder;
	}
	
	/**
	 * @return the index
	 */
	private final String getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	private final void setIndex(String index) {
		this.index = index;
	}

	/**
	 * @return the type
	 */
	private final String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	private final void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the client
	 */
	private final Client getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	private final void setClient(Client client) {
		this.client = client;
	}

	/**
	 * @return the node
	 */
	private final Node getNode() {
		return node;
	}

	/**
	 * @param node the node to set
	 */
	private final void setNode(Node node) {
		this.node = node;
	}

	/**
	 * @return the logger
	 */
	private final Logger getLogger() {
		return logger;
	}
	
}