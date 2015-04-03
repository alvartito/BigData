package entregable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FamilyFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * El objetivo de esta practiva es calcular la probabilidad de caida de la cotización de una empresa
 * dado un modelo de probabilidades basado en la diferencia open-close
 */

public class EstimateMarketDropEntregable {


	public static final String tableName = "TimestampEmpresaModeloMetrica";
	public static final String columnFamily = "probabilidad";
	public static final String qualifier = "prob";
	public static final String modeloProbabilistico = "mod1";
	
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
	private List<String> getMetric(Date dateStart) throws IOException {

		// Calcula la fecha de fin sumando un dia
		Calendar c = Calendar.getInstance();
		c.setTime(dateStart);
		//c.add(Calendar.DAY_OF_MONTH, 1);
		Date dateEnd = c.getTime();

		// inicializa variables
		float maxMetric = 0;
		String empresa = "";

		/* ***********
		 * Inicio del codigo del alumno Debe instanciar un objeto ResultScanner sobre la tabla
		 * ***********
		 */

		//Crear los filtros adecuados, y lanzar el scan, para llegar al resultado deseado
		
		/* ***********
		 * Fin del codigo del alumno ***********
		 */

		// Creamos la instancia para acceder por RPC a HBase
		HTable tabla = new HTable(conf, tableName);

		// crea al array de filtros
		List<Filter> filters = new ArrayList<Filter>();

		// crea el filtro para ColumnFamily
		Filter filter1 = new FamilyFilter(CompareFilter.CompareOp.EQUAL,
				new BinaryComparator(Bytes.toBytes(columnFamily)));
		filters.add(filter1);
		
		// crea el filtro para qualifier
		Filter filter2 = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(
				Bytes.toBytes(qualifier)));
		filters.add(filter2);
		
		StringBuilder sbRk = new StringBuilder();
		sbRk.append(modeloProbabilistico).append("/").append(dateEnd);
		
		// crea el filtro para el RoyKey, el value que nos han pedido
		Filter filter3 = new ValueFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(
				Bytes.toBytes(sbRk.toString())));
		filters.add(filter3);

		// crea la lista de filtros con AND
		FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);

		Scan scan = new Scan();
		// asociamos al scan la lista de filtros
		scan.setFilter(filterList);

		// obtenemos el iterador sobre la lista de resultados
		ResultScanner scanner = tabla.getScanner(scan);
		// iteramos la lista de resultados (rango de RK)
		for (Result result : scanner) {
			// captura la RK
			String rk = Bytes.toString(result.getRow());
			Cell kv = result.getColumnLatestCell(Bytes.toBytes(columnFamily),
					Bytes.toBytes(qualifier));
			// captura el value asociado a la celda
			float metric = Bytes.toFloat(CellUtil.cloneValue(kv));
			// selecciona el maximo
			if (metric > maxMetric) {
				maxMetric = metric;
				int posEmp = rk.lastIndexOf("/");
				empresa = rk.substring(posEmp + 1);
			}
		}
		// cerramos el scanner
		scanner.close();
		//Cerramos la tabla
		tabla.close();
		
		// si ha encontrado un resultado lo mete en la lista
		ArrayList<String> resultados = new ArrayList<String>();
		if (!empresa.isEmpty()) {
			resultados.add(empresa);
			resultados.add(String.valueOf(maxMetric));
		}

		return resultados;
	}


	/**
	 * PROPUESTO 2 Usando sólo filtros hacer un Scan para encontrar la última
	 * versión del registro con valor price:low=24.81
	 */
	private void propuesto2() throws IOException {

		System.out.println("\n*** propuesto2 ***\n");

		// Creamos la instancia para acceder por RPC a HBase
		HTable tabla = new HTable(conf, tableName);

		// crea al array de filtros
		List<Filter> filters = new ArrayList<Filter>();

		// crea el filtro para ColumnFamily
		Filter filter1 = new FamilyFilter(CompareFilter.CompareOp.EQUAL,
				new BinaryComparator(Bytes.toBytes(columnFamily)));
		filters.add(filter1);
		
		// crea el filtro para qualifier
		Filter filter2 = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(
				Bytes.toBytes("low")));
		filters.add(filter2);
		
		// crea el filtro para el RoyKey, el value que nos han pedido
		Filter filter3 = new ValueFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(
				Bytes.toBytes("24.81")));
		filters.add(filter3);

		// crea la lista de filtros con AND
		FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);

		Scan scan = new Scan();
		// asociamos al scan la lista de filtros
		scan.setFilter(filterList);

		// obtenemos el iterador sobre la lista de resultados
		ResultScanner scanner = tabla.getScanner(scan);
		// iteramos la lista de resultados (rango de RK)
		for (Result result : scanner) {
			// Resultado del scanner (rango de RK)
			System.out.println(result);
			// Contenido de cada RK
			List<Cell> celdas = result.listCells();
			for (Cell cell : celdas) {
				System.out.println("Key: " + cell.toString());
				System.out.println("Value: "
						+ Bytes.toString(CellUtil.cloneValue(cell)));
			}

		}

		// cerramos el scanner
		scanner.close();
		// cerramos la tabla
		tabla.close();
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
	 * Por ejemplo, usando la query 
	 * scan 'MetricaTSEmpresa', {LIMIT => 10, STARTROW => 'open-close/953539200000', 
	 * STOPROW => 'open-close/ 953625600000', VERSIONS => 1, 
	 * FILTER =>"((FamilyFilter (=, 'binary:model1')) AND (QualifierFilter (=, 'binary:10<=o-c<20'))" } 
	 * ROW COLUMN+CELL open-close/ 953539200000/ DITC column=model1:10<=o-c<20, timestamp=953539200000,
	 * value=A0\x00\x00 
	 * Obtenemos el timestamp date -d '@953539200' Mon Mar 20 00:00:00 PST 2000 
	 * Es decir, 20/03/2000. Si introducimos este timestamp en el programa obtenemos la empresa y la
	 * probabilidad correspondiente La probabilidad maxima es 0.5 para la empresa DITC
	 */

	public static boolean printProbability(EstimateMarketDropEntregable model, Date date)
			throws IOException {

		String empresa = "";
		float metric = 0;

		List<String> metricList = model.getMetric(date);
		if (!metricList.isEmpty()) {
			empresa = metricList.get(0);
			metric = Float.valueOf(metricList.get(1));
		}

		// imprime los resultados, si los hay
		if (!empresa.isEmpty()) {
//			float probability = EstimateMarketDropEntregable.getProbability(metric);
//			System.out.println("La probabilidad maxima es " + probability + " para la empresa "
//					+ empresa);

			return true;
		}

		return false;
	}

	public static void main(String[] args) throws Exception {

		EstimateMarketDropEntregable model = new EstimateMarketDropEntregable();

		//Fecha de entrada por teclado
		Date date = EstimateMarketDropEntregable.getDate();
		
		// Buscamos en nuestra tabla la empresa con mayor probabilidad de
		// desplome para la fecha introducida.

		// prueba con el segmento de mas alta probabilidad
		if (EstimateMarketDropEntregable.printProbability(model, date)) {
			return;
		}
	}

}
