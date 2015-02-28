package com.utad.master.hbase.soluciones;

import java.io.IOException;
import java.util.ArrayList;
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
import org.apache.hadoop.hbase.filter.ColumnPaginationFilter;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FamilyFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.InclusiveStopFilter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * El objetivo de esta practica es usar un modelo de datos de tipo SERIE TEMPORAL EMPRESA y paginar en filas y columnas.
 * Se paginará unicamente sobre 'price'
 * 
 * <p>Para ello se calculara la media de las cotizaciones maximas del mes 2001-01 para cada empresa
 * <p>------------------------------------------
 * <p>timestamp de 2001-01-01 => 978336000000
 * <p>timestamp de 2001-01-31 => 980928000000
 * 
 * <p>timestamp inverso de 2001-01-01 => 9223371058518775807
 * <p>timestamp inverso de 2001-01-31 => 9223371055926775807
 */

/*
 * Modelo de datos
 * Rowkey: <metrica>/<TS>
 * Column Family: price, totals
 * Qualifier: <empresa>
*/

public class ModeloMetricaTSResuelto {

	public static final String tableName = "MetricaTS";
//	public static final String fileName = "file:/tmp/NASDAQ_daily_prices_subset_RK-metrica-TS.tsv";
	public static final String fileName = "data/NASDAQ_daily_prices_subset_RK-metrica-TS.tsv";
	public static final String columnFamily1 = "price";
	
	public static final String START_ROW = "high  /9223371055926775807";
	
	//Siempre es el mismo, el que se defina en los requisitos.
	public static final String STOP_ROW = "high  /9223371058518775807";

	private Configuration conf = null;

	/*
	 * Crea los objetos de configuracion y admin para acceso a HBase
	 */
	public ModeloMetricaTSResuelto() throws IOException {
		this.conf = HBaseConfiguration.create();
	}

	/**
	 * @param tabla
	 *            tabla sobre la que se hace la query
	 * @param startRowKey
	 *            RowKey de comienzo
	 * @param coff
	 *            columna de comienzo
	 * @param pageSize
	 *            tamaño de pagina para las consultas
	 * @return RowKey RowKey final
	 * @throws IOException
	 */
	public byte[] query(HTable tabla, byte[] startRowKey, int coff, int pageSize) throws IOException {

		System.out.println("*** RowKey= " + Bytes.toString(startRowKey) + " coff=" + coff + " ***");

		/* ***********
		 * Inicio del codigo del alumno devuelve el ultimo RowKey leido despues de iterar sobre los
		 * resultados ***********
		 */

		byte[] RowKey = null;

		// instancia la lista de filtros
		List<Filter> filters = new ArrayList<Filter>();

		// filtro de ColumnFamily
		Filter filter1 = new FamilyFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(
				Bytes.toBytes(columnFamily1)));
		filters.add(filter1);

		// filtro para que incluya la stop rowkey
		Filter filter2 = new InclusiveStopFilter(Bytes.toBytes(STOP_ROW));
		filters.add(filter2);

		// filtro de paginado por RowKey
		Filter filter3 = new PageFilter(pageSize);
		filters.add(filter3);

		// filtro de paginado por columnas
		Filter filter4 = new ColumnPaginationFilter(pageSize, coff);
		filters.add(filter4);

		// instancia la lista de filtros con AND
		FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);

		// instancia el scan y asigna los filtros
		Scan scan = new Scan();
		scan.setFilter(filterList);

		// sufijo para paginar en base a la RowKey final de la pagina anterior
		byte[] sufijo = { 0 };

		// asigna la startRowKey
		scan.setStartRow(Bytes.add(startRowKey, sufijo));

		// asigna la stopRowKey
		scan.setStopRow(Bytes.toBytes(STOP_ROW));

		// asigna el numero maximo de versiones a 1
		scan.setMaxVersions(1);

		// instancia el iterador
		ResultScanner scanner = tabla.getScanner(scan);
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
			RowKey = result.getRow();
		}
		scanner.close();

		/* ***********
		 * Fin del codigo del alumno ***********
		 */

		return RowKey;
	}

	public static void main(String[] args) throws IOException {

		ModeloMetricaTSResuelto modelo = new ModeloMetricaTSResuelto();

		// Instancia HTable para la tabla tablename
		HTable tabla = new HTable(modelo.conf, tableName);

		// RK inicial
		byte[] RowKeyStart = Bytes.toBytes(START_ROW);
		
		// tamaño de pagina
		int pageSize = 5;

		/*
		 * ESCANEAMOS PRIMERO POR FILAS, Y LUEGO POR COLUMNAS, 
		 * EN PEQUEÑOS BLOQUES DE TAMAÑÓ 5X5
		 * DE IZQUIERDA A DERECHA, DE ARRIBA HACIA ABAJO
		 */
		// RowKeystart contiene la clave inicial para cada pagina
		// itera para paginar por filas
		while (RowKeyStart != null) {
			byte[] RowKeyStart1 = RowKeyStart;
			byte[] RowKeyStart2 = RowKeyStart;
			int coff = 0;

			// RowKeystart2 será null cuando haya terminado de paginar por columnas
			// itera para paginar por columnas
			while (RowKeyStart2 != null) {
				RowKeyStart2 = modelo.query(tabla, RowKeyStart, coff, pageSize);
				
				if (RowKeyStart2 != null) {
					RowKeyStart1 = RowKeyStart2;
				}
				
				coff += pageSize;
			}

			// la condicion de salida es que la clave inicial sea igual a la ultima clave de salida
			// por columnas
			// esto solo pasara cuando se hayan agotado las paginas tanto en filas como en columnas
			if (RowKeyStart == RowKeyStart1) {
				break;
			}
			
			RowKeyStart = RowKeyStart1;
		}
	}
	
}
