package com.utad.master.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
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

/*
 * El objetivo de esta practica es usar un modelo de datos de tipo serie temporal inversa y paginar en filas y columnas.
 * 
 * Para ello se calculara la media de las cotizaciones maximas del mes 2001-01 para cada empresa
 * ------------------------------------------
 * timestamp de 2001-01-01 => 978336000000
 * timestamp de 2001-01-31 => 980928000000
 * 
 * timestamp inverso de 2001-01-01 => 9223371058518775807
 * timestamp inverso de 2001-01-31 => 9223371055926775807
 */

/*
 * Modelo de datos
 * Rowkey: <metrica>/<TS>
 * Column Family: price, totals
 * Qualifier: <empresa>
*/

public class ModeloMetricaTS {

	public static final String tableName = "MetricaTS";
	public static final String fileName = "file:/tmp/NASDAQ_daily_prices_subset_RK-metrica-TS.tsv";
	public static final String columnFamily1 = "price";
	public static final String columnFamily2 = "totals";

	public static final String START_ROW = "high  /9223371055926775807";
	public static final String STOP_ROW = "high  /9223371058518775807";

	private Configuration conf = null;

	/*
	 * Crea los objetos de configuracion y admin para acceso a HBase
	 */
	public ModeloMetricaTS() throws IOException {
		this.conf = HBaseConfiguration.create();
	}

	/**
	 * @param tabla
	 *            tabla sobre la que se hace la query
	 * @param startrk
	 *            RK de comienzo
	 * @param coff
	 *            columna de comienzo
	 * @param pageSize
	 *            tamaño de pagina para las consultas
	 * @return rk RK final
	 * @throws IOException
	 */
	public byte[] query(HTable tabla, byte[] startrk, int coff, int pageSize) throws IOException {

		System.out.println("*** RK= " + Bytes.toString(startrk) + " coff=" + coff + " ***");

		/* ***********
		 * Inicio del codigo del alumno devuelve el ultimo rk leido despues de iterar sobre los
		 * resultados ***********
		 */

		byte[] rk = null;

		/* ***********
		 * Fin del codigo del alumno ***********
		 */

		return rk;
	}

	public static void main(String[] args) throws IOException {

		ModeloMetricaTS modelo = new ModeloMetricaTS();

		// instancia HTable para la tabla tablename
		HTable tabla = new HTable(modelo.conf, tableName);

		// RK inicial
		byte[] rkStart = Bytes.toBytes(START_ROW);
		// tamaño de pagina
		int pageSize = 5;
		// rkstart contiene la clave inicial para cada pagina
		// itera para paginar por filas
		while (rkStart != null) {
			byte[] rkStart1 = rkStart;
			byte[] rkStart2 = rkStart;
			int coff = 0;
			// rkstart2 será null cuando haya terminado de paginar por columnas
			// itera para paginar por columnas
			while (rkStart2 != null) {
				rkStart2 = modelo.query(tabla, rkStart, coff, pageSize);
				if (rkStart2 != null) {
					rkStart1 = rkStart2;
				}
				coff += pageSize;
			}

			// la condicion de salida es que la clave inicial sea igual a la ultima clave de salida
			// por columnas
			// esto solo pasara cuando se hayan agotado las paginas tanto en filas como en columnas
			if (rkStart == rkStart1) {
				break;
			}
			rkStart = rkStart1;
		}
	}
	
}
