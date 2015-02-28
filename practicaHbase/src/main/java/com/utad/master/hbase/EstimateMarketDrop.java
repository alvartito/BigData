package com.utad.master.hbase;

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
import org.apache.hadoop.hbase.util.Bytes;

/**
 * El objetivo de esta practiva es calcular la probabilidad de caida de la cotización de una empresa
 * dado un modelo de probabilidades basado en la diferencia open-close
 */

public class EstimateMarketDrop {

	// nombre de la tabla
	public static final String tableName = "MetricaTSEmpresa";
	// nombre del modelo
	public static final String columnFamilty1 = "model1";
	// nombre de la metrica para ese modelo
	public static final String model = "open-close";
	// nombres de los segmentos del modelo estadístico
	public static final String qualifier1 = "o-c<0";
	public static final String qualifier2 = "0<=o-c<10";
	public static final String qualifier3 = "10<=o-c<20";
	public static final String qualifier4 = "o-c>=20";
	// formatos de la rowkey
	public static final String formatRkTimestamp = "%13s";
	public static final String formatRkEmpresa = "%5s";

	// formato de entrada de la fecha
	private static final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	// variables para acceder a HBase
	private Configuration conf = null;

	// instanciamos las clases necesarias para acceder a HBase en el constructor
	// dado que son threadsafe
	public EstimateMarketDrop() throws IOException {
		this.conf = HBaseConfiguration.create();
	}

	/**
	 * Busca la row con el maximo value para la metrica open-close
	 * 
	 * @param dateStart
	 *            dia de busqueda
	 * @param statModel
	 *            qualifier para buscar dentro del modelo estadístico
	 * @return
	 * @throws IOException
	 */
	private List<String> getMetric(Date dateStart, String statModel) throws IOException {

		// Calcula la fecha de fin sumando un dia
		Calendar c = Calendar.getInstance();
		c.setTime(dateStart);
		c.add(Calendar.DAY_OF_MONTH, 1);
		Date dateEnd = c.getTime();

		// inicializa variables
		float maxMetric = 0;
		String empresa = "";

		/* ***********
		 * Inicio del codigo del alumno Debe instanciar un objeto ResultScanner sobre la tabla
		 * ***********
		 */

		ResultScanner scanner = null;

		/* ***********
		 * Fin del codigo del alumno ***********
		 */

		// iteramos la lista de resultados
		for (Result result : scanner) {
			// captura la RK
			String rk = Bytes.toString(result.getRow());
			Cell kv = result.getColumnLatestCell(Bytes.toBytes(columnFamilty1),
					Bytes.toBytes(statModel));
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

		// si ha encontrado un resultado lo mete en la lista
		ArrayList<String> resultados = new ArrayList<String>();
		if (!empresa.isEmpty()) {
			resultados.add(empresa);
			resultados.add(String.valueOf(maxMetric));
		}

		return resultados;
	}

	/**
	 * Funcion que permite construir el modelo estadístico
	 * 
	 * @param metric
	 *            metrica que define el modelo estadístico
	 * @return
	 */
	private static float getProbability(float metric) {

		if ((metric) < 0) {
			return (float) 0;
		} else if (((metric) >= 0) && (metric) < 10) {
			return (float) 0.2;
		} else if (((metric) >= 10) && (metric) < 20) {
			return (float) 0.5;
		} else if ((metric) >= 20) {
			return (float) 0.9;
		}

		return 0;
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
	 * Por ejemplo, usando la query scan 'MetricaTSEmpresa', {LIMIT => 10, STARTROW => 'open-close/
	 * 953539200000', STOPROW => 'open-close/ 953625600000', VERSIONS => 1, FILTER =>
	 * "((FamilyFilter (=, 'binary:model1')) AND (QualifierFilter (=, 'binary:10<=o-c<20'))" } ROW
	 * COLUMN+CELL open-close/ 953539200000/ DITC column=model1:10<=o-c<20, timestamp=953539200000,
	 * value=A0\x00\x00 Obtenemos el timestamp date -d '@953539200' Mon Mar 20 00:00:00 PST 2000 Es
	 * decir, 20/03/2000. Si introducimos este timestamp en el programa obtenemos la empresa y la
	 * probabilidad correspondiente La probabilidad maxima es 0.5 para la empresa DITC
	 */

	public static boolean printProbability(EstimateMarketDrop model, Date date, String segment)
			throws IOException {

		String empresa = "";
		float metric = 0;

		List<String> metricList = model.getMetric(date, segment);
		if (!metricList.isEmpty()) {
			empresa = metricList.get(0);
			metric = Float.valueOf(metricList.get(1));
		}

		// imprime los resultados si los hay
		if (!empresa.isEmpty()) {
			float probability = EstimateMarketDrop.getProbability(metric);
			System.out.println("La probabilidad maxima es " + probability + " para la empresa "
					+ empresa);

			return true;
		}

		return false;
	}

	public static void main(String[] args) throws Exception {

		EstimateMarketDrop model = new EstimateMarketDrop();

		Date date = EstimateMarketDrop.getDate();

		// prueba con el segmento de mas alta probabilidad
		if (EstimateMarketDrop.printProbability(model, date, qualifier4)) {
			return;
		}

		// prueba con el segmento de mas alta probabilidad
		if (EstimateMarketDrop.printProbability(model, date, qualifier3)) {
			return;
		}

		// prueba con el segmento de mas alta probabilidad
		if (EstimateMarketDrop.printProbability(model, date, qualifier2)) {
			return;
		}

		// prueba con el segmento de mas alta probabilidad
		if (EstimateMarketDrop.printProbability(model, date, qualifier1)) {
			return;
		}

	}

}
