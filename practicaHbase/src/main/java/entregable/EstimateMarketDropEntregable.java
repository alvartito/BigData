package entregable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * El objetivo de esta practiva es calcular la probabilidad de caida de la
 * cotización de una empresa dado un modelo de probabilidades basado en la
 * diferencia open-close
 */

public class EstimateMarketDropEntregable {

	public static final String tableName = "TimestampEmpresaModeloMetrica";
	public static final String modeloProbabilistico = "mod1";
	public static final String columnFamily = modeloProbabilistico;
	public static final String qualifierEmpresa = "emp";
	public static final String qualifierProbabilidad = "prob";
	public static final String nombreMercado = "NASDAQ";

	// nombre del modelo

	// nombre de la metrica para ese modelo
	public static final String metrica = "open-close";

	// formatos de la rowkey
	public static final String formatRowKeyTimestamp = "%13s";
	public static final String formatRowKeyEmpresa = "%5s";

	// formato de entrada de la fecha
	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	// variables para acceder a HBase
	private Configuration conf = null;

	// instanciamos las clases necesarias para acceder a HBase en el constructor
	// dado que son threadsafe
	public EstimateMarketDropEntregable() throws IOException {
		this.conf = HBaseConfiguration.create();
	}

	/**
	 * Busca la row con el maximo value para el qualifier 'prob'
	 * 
	 * @param dateStart
	 *            dia de busqueda
	 * @param statModel
	 *            qualifier para buscar dentro del modelo estadístico
	 * @return
	 * @throws IOException
	 */
	private Hashtable<Float, String> getMetric(Date dateStart) throws IOException {

		// Calcula la fecha de fin sumando un dia
		Calendar c = Calendar.getInstance();
		c.setTime(dateStart);
		c.add(Calendar.DAY_OF_MONTH, 1);
		Date dateEnd = c.getTime();

		// // inicializa variables
		// float maxMetric = 0;
		// String empresa = "";
		//
		/* ***********
		 * Inicio del codigo del alumno Debe instanciar un objeto ResultScanner
		 * sobre la tabla ***********
		 */

		// Crear los filtros adecuados, y lanzar el scan, para llegar al
		// resultado deseado

		// Creamos la instancia para acceder por RPC a HBase
		HTable tabla = new HTable(conf, tableName);

		// crea al array de filtros
		// List<Filter> filters = new ArrayList<Filter>();

		// creating a scan object with start and stop row keys
		Scan scan = new Scan(Bytes.toBytes(nombreMercado+"/"+modeloProbabilistico + "/" + dateStart.getTime()),
				Bytes.toBytes(nombreMercado+"/"+modeloProbabilistico + "/" + dateEnd.getTime()));

		// // crea el filtro para ColumnFamily
		// Filter filter1 = new FamilyFilter(CompareFilter.CompareOp.EQUAL,
		// new BinaryComparator(Bytes.toBytes(columnFamily)));
		// filters.add(filter1);
		//
		// // crea el filtro para qualifier
		// Filter filter2 = new QualifierFilter(CompareFilter.CompareOp.EQUAL,
		// new BinaryComparator(
		// Bytes.toBytes(qualifierEmpresa)));
		// filters.add(filter2);
		//
		// // crea el filtro para el RoyKey, el value que nos han pedido
		// Filter filter3 = new ValueFilter(CompareFilter.CompareOp.EQUAL, new
		// BinaryComparator(
		// Bytes.toBytes(modeloProbabilistico+"/"+dateEnd)));
		// filters.add(filter3);
		//
		// // crea la lista de filtros con AND
		// FilterList filterList = new
		// FilterList(FilterList.Operator.MUST_PASS_ALL, filters);
		//
		/* ***********
		 * Fin del codigo del alumno ***********
		 */

		// asociamos al scan la lista de filtros
		// scan.setFilter(filterList);

		// obtenemos el iterador sobre la lista de resultados
		ResultScanner scanner = tabla.getScanner(scan);
		// iteramos la lista de resultados (rango de RK)
		Hashtable<Float, String> resultados = new Hashtable<Float, String>();

		for (Result result : scanner) {
			Cell ke = result.getColumnLatestCell(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifierEmpresa));

			String sEmpresa = Bytes.toString(CellUtil.cloneValue(ke));

			Cell kp = result.getColumnLatestCell(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifierProbabilidad));

			// captura el value asociado a la celda
			float metric = Bytes.toFloat(CellUtil.cloneValue(kp));

			resultados.put(metric, sEmpresa);
		}
		// cerramos el scanner
		scanner.close();
		// Cerramos la tabla
		tabla.close();

		// si ha encontrado un resultado lo mete en la lista

		return resultados;
	}

	/**
	 * Función para entrada de datos del usuario
	 * 
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	private static Date getDate() throws IOException, ParseException {

		InputStreamReader istream = new InputStreamReader(System.in);
		BufferedReader bufRead = new BufferedReader(istream);

		System.out.println("Teclee fecha dd/MM/yyyy");
		String dateInString = bufRead.readLine();
		Date date = formatter.parse(dateInString);

		return date;
	}

	/*
	 * Por ejemplo, usando la query scan 'MetricaTSEmpresa', {LIMIT => 10,
	 * STARTROW => 'open-close/953539200000', STOPROW => 'open-close/
	 * 953625600000', VERSIONS => 1, FILTER =>
	 * "((FamilyFilter (=, 'binary:model1')) AND (QualifierFilter (=, 'binary:10<=o-c<20'))"
	 * } ROW COLUMN+CELL open-close/ 953539200000/ DITC
	 * column=model1:10<=o-c<20, timestamp=953539200000, value=A0\x00\x00
	 * Obtenemos el timestamp date -d '@953539200' Mon Mar 20 00:00:00 PST 2000
	 * Es decir, 20/03/2000. Si introducimos este timestamp en el programa
	 * obtenemos la empresa y la probabilidad correspondiente La probabilidad
	 * maxima es 0.5 para la empresa DITC
	 */

	public static boolean printProbability(EstimateMarketDropEntregable model, Date date) throws IOException {

		Hashtable<Float, String> metricList = model.getMetric(date);
		// imprime los resultados, si los hay
		if (!metricList.isEmpty()) {
			List<Float> tmp = Collections.list(metricList.keys());
			float max = Collections.max(tmp);
			String empresa = metricList.get(max);
			System.out.println("\nLa probabilidad maxima es " + max + " para la empresa " + empresa);
		}

		return false;
	}

	public static void main(String[] args) throws Exception {

		EstimateMarketDropEntregable model = new EstimateMarketDropEntregable();

		// Fecha de entrada por teclado
		Date date = EstimateMarketDropEntregable.getDate();

		// Buscamos en nuestra tabla la empresa con mayor probabilidad de
		// desplome para la fecha introducida.

		// prueba con el segmento de mas alta probabilidad
		if (EstimateMarketDropEntregable.printProbability(model, date)) {
			return;
		}
	}

}
