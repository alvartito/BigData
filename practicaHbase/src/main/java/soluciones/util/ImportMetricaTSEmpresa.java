package soluciones.util;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

/*
 * El objetivo de esta practica de importar los datos usando mapreduce para generar un modelo como el indicado abajo
 */


//TODO esta habrá que modificarla
/*
 * Modelo de datos
 * Rowkey: <metrica>/<TS>
 * Column Family: price, totals
 * Qualifier: <empresa>
*/

public class ImportMetricaTSEmpresa {

	int i = 0;
	
    public static final String tableName="MetricaTSEmpresa";
    //public static final String fileName="file:/tmp/NASDAQ_daily_prices_subset_RK-metrica-TS.tsv";
    public static final String fileName="data/NASDAQ_daily_prices_subset_RK-metrica-TS.tsv";
    
    public static final String columnFamily1="price";
    public static final String columnFamily2="totals";
	
    private Configuration conf = null;
    private HBaseAdmin admin = null;

/*
 * Clase mapper para hacer mapreduce con un fichero de entrada y una tabla de salida
 */
    static class Map extends Mapper<LongWritable, Text, Text, Put> {

		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			
			// Obtiene una linea del fichero de log
			String messageStr = value.toString();
			if (messageStr.contains("\t")) {
				// separa por tabuladores los campos
				String[] logRecvArr = messageStr.split("\t");
				if (logRecvArr.length != 4) {
					System.out.println("Linea incorrecta: "+messageStr);
					
					return;
				}
				
				String rowKey=logRecvArr[0];				
				String empresa=logRecvArr[1];
				String valor=logRecvArr[2];
				String timestamp=logRecvArr[3];

				// crea el objeto put
				Put put = new Put(Bytes.toBytes(rowKey), Long.valueOf(timestamp));
				
				// determina a que ColumnFamily pertenece la celda
				if (rowKey.startsWith("volume") || rowKey.startsWith("adj")) {
					put.add(Bytes.toBytes(columnFamily2), Bytes.toBytes(empresa), Bytes.toBytes(valor));
				} else {
					put.add(Bytes.toBytes(columnFamily1), Bytes.toBytes(empresa), Bytes.toBytes(valor));					
				}
				
				// escribe el dato en el conexto
				context.write(new Text("1"), put);
			} else {
				System.out.println("Formato incorrecto");
			}			
		}
	}

	/*
	 * Crea los objetos de configuracion y admin para acceso a HBase
	 */
    private ImportMetricaTSEmpresa() throws IOException {
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
	    HColumnDescriptor coldef1 = new HColumnDescriptor(columnFamily1);
	    // set maxversions a 1 para ColumnFamily1
	    coldef1.setMaxVersions(1);
	    // crea el descriptor de la ColumnFamily2
	    HColumnDescriptor coldef2 = new HColumnDescriptor(columnFamily2);
	    // set maxversions a 1 para ColumnFamily2
	    coldef2.setMaxVersions(1);
	    // actualiza el descriptor de la tabla
	    desc.addFamily(coldef1);
	    desc.addFamily(coldef2);
	    // crea la tabla
	    admin.createTable(desc);
    }

    /*
     * Ejecuta el job M/R
     */
	private void execute() throws Exception {
		// construye el objeto job y le asigna los tipos de entrada y salida
		
		Job job = Job.getInstance();
		job.setJobName(this.getClass().getSimpleName());
		job.setJarByClass(ImportMetricaTSEmpresa.class);
//		job.setOutputKeyClass(Text.class);
//		job.setOutputValueClass(Text.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		// asigna los formatos de entrada y salida del job
		job.setInputFormatClass(TextInputFormat.class);
//		job.setOutputFormatClass(TextOutputFormat.class);
		// asigna la clase Map
		job.setMapperClass(Map.class);
		// indica que no hay reducer
		job.setNumReduceTasks(0);
		
		// asigna el fichero de entrada
		FileInputFormat.setInputPaths(job, new Path(fileName));

		// Vincula este job a la tabla de HBase como salida
		TableMapReduceUtil.initTableReducerJob(tableName, null, job);

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
		
		ImportMetricaTSEmpresa modelo=new ImportMetricaTSEmpresa();
		
		modelo.creaTabla();
		modelo.execute();		
	}

}
