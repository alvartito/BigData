package entregable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FamilyFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * El objetivo de esta practiva es calcular la probabilidad de caida de la
 * cotización de una empresa dado un modelo de probabilidades basado en la
 * diferencia open-close
 */

public class EstimateMarketDropEntregable {

	// Nombre de la tabla
	public static final String tableName = "TimestampEmpresaModeloMetrica";
	// Modelo probabilistico (puede ser parametrizable)
	public static final String modeloProbabilistico = "mod1";
	// Column Family de nuestra tabla (como sólo tenemos un modelo
	// probabilistico, asociamos el valor del mismo)
	public static final String columnFamily = modeloProbabilistico;
	// Qualifier para la empresa
	public static final String qualifierEmpresa = "emp";
	// Qualifier para la probabilidad de desplome
	public static final String qualifierProbabilidad = "prob";
	// Nombre del mercado de valores (parametrizable)
	public static final String nombreMercado = "NASDAQ";

	// formato de entrada de la fecha
	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	// variables para acceder a HBase
	private Configuration conf = null;

	private static String dateInString;

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
	private ArrayList<String> getMetric(Date dateStart) throws IOException {

		// Calcula la fecha de fin sumando un dia
		Calendar c = Calendar.getInstance();
		c.setTime(dateStart);
		c.add(Calendar.DAY_OF_MONTH, 1);
		Date dateEnd = c.getTime();

		// Creamos la instancia para acceder por RPC a HBase
		HTable tabla = new HTable(conf, tableName);

		// crea al array de filtros
		List<Filter> filters = new ArrayList<Filter>();

		// Crear los filtros adecuados, y lanzar el scan, para llegar al
		// resultado deseado
		// Creamos un objeto scan con start y stop rows, ya que nuestro modelo
		// está preparado para este tipo de búsquedas.
		Scan scan = new Scan(Bytes.toBytes(nombreMercado + "/" + modeloProbabilistico + "/" + dateStart.getTime()),
				Bytes.toBytes(nombreMercado + "/" + modeloProbabilistico + "/" + dateEnd.getTime()));

		// Crea el filtro para ColumnFamily. Lo añadimos porque como puede ser
		// parametrizable, hay que tenerlo en cuenta.
		Filter filterCF = new FamilyFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(
				Bytes.toBytes(columnFamily)));
		filters.add(filterCF);

		// crea la lista de filtros con AND
		FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);

		// asociamos al scan la lista de filtros
		scan.setFilter(filterList);

		// obtenemos el iterador sobre la lista de resultados
		ResultScanner scanner = tabla.getScanner(scan);

		// iteramos la lista de resultados (rango de RK)
		Hashtable<Float, String> resultados = new Hashtable<Float, String>();

		// si ha encontrado un resultado lo mete en la lista
		for (Result result : scanner) {
			Cell ke = result.getColumnLatestCell(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifierEmpresa));

			// captura el value asociado a la celda
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

		ArrayList<String> retorno = new ArrayList<String>();
		if (!resultados.isEmpty()) {
			List<Float> tmp = Collections.list(resultados.keys());
			float max = Collections.max(tmp);
			String empresa = resultados.get(max);

			retorno.add(empresa);
			retorno.add(String.valueOf(max));
		}

		return retorno;
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
		dateInString = bufRead.readLine();
		Date date = formatter.parse(dateInString);

		return date;
	}

	public static boolean printProbability(EstimateMarketDropEntregable model, Date date) throws IOException {

		ArrayList<String> metricList = model.getMetric(date);
		// imprime los resultados, si los hay
		if (!metricList.isEmpty()) {
			String empresa = metricList.get(0);
			String probabilidad = metricList.get(1);
			System.out.println("\nLa probabilidad maxima es " + probabilidad + " para la empresa " + empresa
					+ " en el día " + dateInString);
		}

		return false;
	}

	public static void main(String[] args) throws Exception {

		EstimateMarketDropEntregable model = new EstimateMarketDropEntregable();

		// Fecha de entrada por teclado
		Date date = EstimateMarketDropEntregable.getDate();

		// Buscamos en nuestra tabla la empresa con mayor probabilidad de
		// desplome para la fecha introducida.

		if (EstimateMarketDropEntregable.printProbability(model, date)) {
			return;
		}
	}

}
