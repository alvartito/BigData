package entregable.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.mutable.Mutable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

/**
 * El objetivo de esta practica de importar los datos usando mapreduce para
 * generar un modelo como el indicado abajo
 * <p>
 * Modelo de datos
 * <li>
 * Rowkey: modelo/timestamp/empresa
 * <li>
 * Column Family: probabilidad
 * <li>
 * Qualifier: prob
 * <li>
 * Value: empresa/probabilidad
 * <p>
 * Cabecera de la tabla a importar:
 * <li>
 * exchange
 * <li>
 * stock_symbol
 * <li>
 * date
 * <li>
 * stock_price_open
 * <li>
 * stock_price_high
 * <li>
 * stock_price_low
 * <li>
 * stock_price_close
 * <li>
 * stock_volume
 * <li>
 * stock_price_adj_close
 */

public class ImportMetricaTSEntregable {

	int i = 0;

	// public static final String
	// fileName="file:/tmp/NASDAQ_daily_prices_subset_RK-metrica-TS.tsv";

	// Usaremos el fichero original para parsear nuestros datos:
	public static final String fileName = "data/NASDAQ_daily_prices_subset.csv";

	public static final String tableName = "TimestampEmpresaModeloMetrica";
	public static final String modeloProbabilistico = "mod1";
	public static final String columnFamily = modeloProbabilistico;
	public static final String qualifierEmpresa = "emp";
	public static final String qualifierProbabilidad = "prob";
	

	private Configuration conf = null;
	private HBaseAdmin admin = null;

	private static Date dateFormateada;
	
	/*
	 * Clase mapper para hacer mapreduce con un fichero de entrada y una tabla
	 * de salida
	 */
	static class Map extends Mapper<LongWritable, Text, Text, Text> {

		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			// Obtiene una linea del fichero de log
			String messageStr = value.toString();
			// Omitimos la cabecera
			if (!messageStr.contains("exchange,")) {
				if (messageStr.contains(",")) {
					// separa por tabuladores los campos
					String[] logRecvArr = messageStr.split(",");
					if (logRecvArr.length != 9) {
						System.out.println("Linea incorrecta: " + messageStr);

						return;
					}

					// Recuperamos unicamente los valores que nos interesan
//					String mercado = logRecvArr[0];
					String nombreEmpresa = logRecvArr[1];
					String date = logRecvArr[2];
					// formato de entrada de la fecha
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					
					try {
						dateFormateada = formatter.parse(date);
					} catch (ParseException e) {
						e.printStackTrace();
					}

					float spOpen = new Float(logRecvArr[3]);
					float spClose = new Float(logRecvArr[6]);

					float probability = getProbability(spOpen - spClose);

					// escribe el dato en el conexto
					context.write(new Text(modeloProbabilistico+"/"+dateFormateada), new Text(qualifierEmpresa+"\t"+nombreEmpresa));

					// escribe el dato en el conexto
					context.write(new Text(modeloProbabilistico+"/"+dateFormateada), new Text(qualifierProbabilidad+"\t"+probability));

				} else {
					System.out.println("Formato incorrecto");
				}
			} else {
//				System.out.println("Cabecera de la tabla:\n");
//				String[] cabecera = messageStr.split(",");
//				for (String string : cabecera) {
//					System.out.println("\t" + string);
//				}
			}
		}
	}
	
	static class Red extends TableReducer<Text, Text, Text> {
		protected void red(Text key, Text value, Context context) throws IOException, InterruptedException{

			// crea el objeto put
			Put put = new Put(Bytes.toBytes(key.toString()), System.currentTimeMillis());

			String[] valor = value.toString().split("\t");

			// determina a que ColumnFamily pertenece la celda
			if(valor[0].equals(qualifierEmpresa)){
				put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifierEmpresa), Bytes.toBytes(valor[1]));
			} else {
				put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifierProbabilidad), Bytes.toBytes(valor[1]));
			}

			// escribe el dato en el conexto
			context.write(new Text("1"), put);
		}
	}

	/*
	 * Crea los objetos de configuracion y admin para acceso a HBase
	 */
	private ImportMetricaTSEntregable() throws IOException {
		this.conf = HBaseConfiguration.create();
		this.admin = new HBaseAdmin(conf);
	}

	/*
	 * Crea la tabla
	 */
	private void creaTabla() throws IOException {

		// si existe la borramos
		if (admin.tableExists(tableName)) {
			borraTabla();
		}

		// crea el descriptor de la tabla
		HTableDescriptor desc = new HTableDescriptor(TableName.valueOf(tableName));
		// crea el descriptor de la ColumnFamily1
		HColumnDescriptor coldef1 = new HColumnDescriptor(columnFamily);
		// set maxversions a 1 para ColumnFamily1
		coldef1.setMaxVersions(1);
		// actualiza el descriptor de la tabla
		desc.addFamily(coldef1);
		// crea la tabla
		admin.createTable(desc);
	}

	/**
	 * Funcion que permite construir el modelo estadístico
	 * 
	 * @param metric
	 *            metrica que define el modelo estadístico
	 * @return
	 */
	private static float getProbability(float openMinusClose) {

		if ((openMinusClose) < 0) {
			return (float) 0;
		} else if (((openMinusClose) >= 0) && (openMinusClose) < 10) {
			return (float) 0.2;
		} else if (((openMinusClose) >= 10) && (openMinusClose) < 20) {
			return (float) 0.5;
		} else if ((openMinusClose) >= 20) {
			return (float) 0.9;
		}

		return 0;
	}

	/*
	 * Ejecuta el job M/R
	 */
	private void execute() throws Exception {
		// construye el objeto job y le asigna los tipos de entrada y salida

		Job job = Job.getInstance();
		job.setJobName(this.getClass().getSimpleName());
		job.setJarByClass(ImportMetricaTSEntregable.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		// asigna los formatos de entrada y salida del job
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Put.class);
		
		// asigna la clase Map
		job.setMapperClass(Map.class);
		job.setReducerClass(Red.class);
		// indica que no hay reducer
		job.setNumReduceTasks(1);

		// asigna el fichero de entrada
		FileInputFormat.setInputPaths(job, new Path(fileName));

		// Vincula este job a la tabla de HBase como salida
		TableMapReduceUtil.initTableReducerJob(tableName, Red.class, job);

		// lanza el job
		job.waitForCompletion(true);
	}

	/*
	 * Borra la tabla
	 */
	private void borraTabla() throws IOException {

		System.out.println("*** borraTabla ***");

		// deshabilita la tabla
		admin.disableTable(tableName);
		// borra la tabla
		admin.deleteTable(tableName);
	}

	public static void main(String[] args) throws Exception {

		ImportMetricaTSEntregable modelo = new ImportMetricaTSEntregable();

		modelo.creaTabla();
		modelo.execute();
	}

}
