package com.utad.master.hbase.soluciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FamilyFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

/*
 * El objetivo de esta práctica es aprender a usar las funciones básicas del API de HBase
 */

/*
 * Modelo de datos:
 * Rowkey: <empresa>
 * Column Family: price, totals
 * Qualifier: <metrica>
 * Version: <timestamp>
 */

public class BasicoEmpresaResuelto {

    public static final String tableName="EmpresaTest";
    public static final String columnFamilty1="price";
    public static final String columnFamilty2="totals";

    private Configuration conf = null;
    private HBaseAdmin admin = null;

    private BasicoEmpresaResuelto() throws IOException {
    	// obtiene la configuración de HBase
	    this.conf = HBaseConfiguration.create();
	    // Obtiene la instancia que permite ejecutar acciones contra HMaster
        this.admin = new HBaseAdmin(conf);
    }

    /*
     * Crea la tabla tableName con dos CF: price, totals
     * Asigna maxVersions a 300
     */
    private void creaTabla() throws IOException {
    	
    	System.out.println("*** creaTabla ***");
    	
    	// comprueba si existe la tabla y si existe la borra
    	if (admin.tableExists(tableName)) {
    		borraTabla();
    	}
    	    	
		// instancia el descriptor de la tabla
	    HTableDescriptor desc = new HTableDescriptor(TableName.valueOf(tableName));
	    // instancia el descriptor de la CF1
	    HColumnDescriptor coldef1 = new HColumnDescriptor(columnFamilty1);
	    coldef1.setMaxVersions(300);
	    // instancia el descriptor de la CF2
	    HColumnDescriptor coldef2 = new HColumnDescriptor(columnFamilty2);
	    coldef2.setMaxVersions(300);
	    // añade los descriptores de las CF al general de la tabla
	    desc.addFamily(coldef1);
	    desc.addFamily(coldef2);
	    // crea la tabla
	    admin.createTable(desc);
    }
    
    /*
     * Crea 3 registros
     * 
     * empresa	open	high	low		close	volume		adj		timestamp
     * DELL		83.87	84.75	82.50	82.81	48736000	10.35	872578800000
	 * DELL		25.25	25.36	24.81	25.25	26264700	25.25	1218006000000
	 * DITC		1.56	1.69	1.53	1.60	133600		1.60	1035442800000
     */
    private void creaRegistro() throws IOException {

    	System.out.println("*** creaRegistro ***");

    	// Creamos la instancia para acceder por RPC a HBase
    	HTable tabla=new HTable(conf, tableName);

    	// Datos del registro1
		String rowKey="DELL";				
		String timestamp="872578800000";

		// Creamos el objeto para persistir registro1
		Put put1 = new Put(Bytes.toBytes(rowKey), Long.valueOf(timestamp));
		put1.add(Bytes.toBytes("price"), Bytes.toBytes("open"), Bytes.toBytes("83.87"));					
		put1.add(Bytes.toBytes("price"), Bytes.toBytes("high"), Bytes.toBytes("84.75"));					
		put1.add(Bytes.toBytes("price"), Bytes.toBytes("low"), Bytes.toBytes("82.50"));					
		put1.add(Bytes.toBytes("price"), Bytes.toBytes("close"), Bytes.toBytes("82.81"));					

		put1.add(Bytes.toBytes("totals"), Bytes.toBytes("volume"), Bytes.toBytes("48736000"));					
		put1.add(Bytes.toBytes("totals"), Bytes.toBytes("adj"), Bytes.toBytes("10.35"));					
		
		// persistimos registro1
		tabla.put(put1);

    	// Datos del registro2
		rowKey="DELL";				
		timestamp="1218006000000";

		// Creamos el objeto para persistir registro2
		Put put2 = new Put(Bytes.toBytes(rowKey), Long.valueOf(timestamp));
		put2.add(Bytes.toBytes("price"), Bytes.toBytes("open"), Bytes.toBytes("25.25"));					
		put2.add(Bytes.toBytes("price"), Bytes.toBytes("high"), Bytes.toBytes("25.36"));					
		put2.add(Bytes.toBytes("price"), Bytes.toBytes("low"), Bytes.toBytes("24.81"));					
		put2.add(Bytes.toBytes("price"), Bytes.toBytes("close"), Bytes.toBytes("25.25"));					

		put2.add(Bytes.toBytes("totals"), Bytes.toBytes("volume"), Bytes.toBytes("26264700"));					
		put2.add(Bytes.toBytes("totals"), Bytes.toBytes("adj"), Bytes.toBytes("25.25"));					
		
		// persistimos registro2
		tabla.put(put2);

    	// Datos del registro3
		rowKey="DITC";				
		timestamp="1035442800000";

		// Creamos el objeto para persistir registro3
		Put put3 = new Put(Bytes.toBytes(rowKey), Long.valueOf(timestamp));
		put3.add(Bytes.toBytes("price"), Bytes.toBytes("open"), Bytes.toBytes("1.56"));					
		put3.add(Bytes.toBytes("price"), Bytes.toBytes("high"), Bytes.toBytes("1.69"));					
		put3.add(Bytes.toBytes("price"), Bytes.toBytes("low"), Bytes.toBytes("1.53"));					
		put3.add(Bytes.toBytes("price"), Bytes.toBytes("close"), Bytes.toBytes("1.60"));					

		put3.add(Bytes.toBytes("totals"), Bytes.toBytes("volume"), Bytes.toBytes("133600"));					
		put3.add(Bytes.toBytes("totals"), Bytes.toBytes("adj"), Bytes.toBytes("1.60"));					
		
		// persistimos registro3
		tabla.put(put3);

		tabla.close();
    }

    /*
     * PROPUESTO 1
     * 
     * Usar put en batch con los mismos registros que antes incrementando timestamp + 60000
     */
    
    private void propuesto1() throws IOException, InterruptedException {
    	
    	System.out.println("*** propuesto1 ***");

    	// Creamos la instancia para acceder por RPC a HBase
    	HTable tabla=new HTable(conf, tableName);

    	// Datos del registro1
		String rowKey="DELL";				
		String timestamp="872578860000";

		List<Put> listaTransacciones=new ArrayList<Put>();
		
		// Creamos el objeto para persistir registro1
		Put put1 = new Put(Bytes.toBytes(rowKey), Long.valueOf(timestamp));
		put1.add(Bytes.toBytes("price"), Bytes.toBytes("open"), Bytes.toBytes("83.87"));					
		put1.add(Bytes.toBytes("price"), Bytes.toBytes("high"), Bytes.toBytes("84.75"));					
		put1.add(Bytes.toBytes("price"), Bytes.toBytes("low"), Bytes.toBytes("82.50"));					
		put1.add(Bytes.toBytes("price"), Bytes.toBytes("close"), Bytes.toBytes("82.81"));					

		put1.add(Bytes.toBytes("totals"), Bytes.toBytes("volume"), Bytes.toBytes("48736000"));					
		put1.add(Bytes.toBytes("totals"), Bytes.toBytes("adj"), Bytes.toBytes("10.35"));					
		
		// guardamos registro1 en la lista de transacciones
		listaTransacciones.add(put1);
		
    	// Datos del registro2
		rowKey="DELL";				
		timestamp="1218006060000";

		// Creamos el objeto para persistir registro2
		Put put2 = new Put(Bytes.toBytes(rowKey), Long.valueOf(timestamp));
		put2.add(Bytes.toBytes("price"), Bytes.toBytes("open"), Bytes.toBytes("25.25"));					
		put2.add(Bytes.toBytes("price"), Bytes.toBytes("high"), Bytes.toBytes("25.36"));					
		put2.add(Bytes.toBytes("price"), Bytes.toBytes("low"), Bytes.toBytes("24.81"));					
		put2.add(Bytes.toBytes("price"), Bytes.toBytes("close"), Bytes.toBytes("25.25"));					

		put2.add(Bytes.toBytes("totals"), Bytes.toBytes("volume"), Bytes.toBytes("26264700"));					
		put2.add(Bytes.toBytes("totals"), Bytes.toBytes("adj"), Bytes.toBytes("25.25"));					
		
		// guardamos registro2 en la lista de transacciones
		listaTransacciones.add(put2);

    	// Datos del registro3
		rowKey="DITC";				
		timestamp="1035442860000";

		// Creamos el objeto para persistir registro3
		Put put3 = new Put(Bytes.toBytes(rowKey), Long.valueOf(timestamp));
		put3.add(Bytes.toBytes("price"), Bytes.toBytes("open"), Bytes.toBytes("1.56"));					
		put3.add(Bytes.toBytes("price"), Bytes.toBytes("high"), Bytes.toBytes("1.69"));					
		put3.add(Bytes.toBytes("price"), Bytes.toBytes("low"), Bytes.toBytes("1.53"));					
		put3.add(Bytes.toBytes("price"), Bytes.toBytes("close"), Bytes.toBytes("1.60"));					

		put3.add(Bytes.toBytes("totals"), Bytes.toBytes("volume"), Bytes.toBytes("133600"));					
		put3.add(Bytes.toBytes("totals"), Bytes.toBytes("adj"), Bytes.toBytes("1.60"));					
		
		// guardamos registro3 en la lista de transacciones
		listaTransacciones.add(put3);

		Object[] results = new Object[listaTransacciones.size()];
		// lazamos la lista de transacciones Put en batch
		tabla.batch(listaTransacciones, results);
		
		// cerramos la tabla
		tabla.close();
    }
        
    /*
     * Consulta los registros con Get
     */
    private void consultaRegistro() throws IOException {
    	
    	System.out.println("*** consultaRegistro ***");

    	// Creamos la instancia para acceder por RPC a HBase
    	HTable tabla=new HTable(conf, tableName);

    	// Creamos objeto para consultar una fila con RK = DELL
        Get get = new Get(Bytes.toBytes("DELL"));

        // asociamos la CF price y el qualifier high
        String qualifier="high";
        get.addColumn(Bytes.toBytes(columnFamilty1), Bytes.toBytes(qualifier));
        
        // maximo numero de versiones a devolver = 2
        get.setMaxVersions(2);

        // Consultamos y obtenermos el resultado
        Result result = tabla.get(get);

        // obtenemos los objetos KeyValue y los imprimimos
        List<Cell> listaResultados=result.listCells();
        for (Cell kv : listaResultados) {
            System.out.println("Key: " + kv.toString());
            System.out.println("Value: " + Bytes.toString(CellUtil.cloneValue(kv)));
        }
        
		tabla.close();
    }

    /*
     * PROPUESTO 1a
     * 
     * Consultar con get cual es el timestamp de la ultima version de la row DELL
     * Si la transaccion propuesto1 ha funcionado el timestamp debe ser 1218006060000
     */
    
    private void propuesto1a() throws IOException {
    
    	System.out.println("*** propuesto1a ***");

    	// Creamos la instancia para acceder por RPC a HBase
    	HTable tabla=new HTable(conf, tableName);

    	// Datos del registro1
		String rowKey="DELL";				

		// instanciamos la transaccion Get
		Get consultaGet=new Get(Bytes.toBytes(rowKey));
		// queremos la ultima versión por timestamp por tanto no necesitamos consultaGet.setMaxVersions();
				
		// hacemos el get
		Result result=tabla.get(consultaGet);
		
        // obtenemos los objetos KeyValue y los imprimimos
        List<Cell> listaResultados=result.listCells();
        for (Cell kv : listaResultados) {
            System.out.println("Key: " + kv.toString());
            System.out.println("Value: " + Bytes.toString(CellUtil.cloneValue(kv)));
       }

        tabla.close();
    }

    /*
     * Usando filtros hace un scan de la ultima versión de la columna totals:volume del registo con RK=DITC
     */
    private void scanRegistros() throws IOException {

    	System.out.println("*** scanRegistros ***");

    	// Creamos la instancia para acceder por RPC a HBase
    	HTable tabla=new HTable(conf, tableName);

    	// crea al array de filtros
	    List<Filter> filters = new ArrayList<Filter>();

	    // crea el filtro para CF
        Filter filter1 = new FamilyFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(columnFamilty2)));
        filters.add(filter1);
	    // crea el filtro para qualifier
        Filter filter2 = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("volume")));
        filters.add(filter2);
	    // crea el filtro para RK
        Filter filter3 = new RowFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("DITC")));
        filters.add(filter3);

        // crea la lista de filtros con AND
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);

	    Scan scan = new Scan();
    	// asociamos al scan la lista de filtros
	    scan.setFilter(filterList);

	    // obtenemos el iterador sobre la lista de resultados
	    ResultScanner scanner = tabla.getScanner(scan);
	    // iteramos la lista de resultados
	    for (Result result : scanner) {
	      System.out.println(result);
	    }
	    
	    // cerramos el scanner
	    scanner.close();
	    // cerramos la tabla
	    tabla.close();
    }

    /*
     * PROPUESTO 2
     * 
     * Usando sólo filtros hacer un Scan para encontrar la última versión del registro con valor price:low=24.81
     */
    
    private void propuesto2() throws IOException {
    	
    	System.out.println("*** propuesto2 ***");

    	// Creamos la instancia para acceder por RPC a HBase
    	HTable tabla=new HTable(conf, tableName);

    	// crea al array de filtros
	    List<Filter> filters = new ArrayList<Filter>();

	    // crea el filtro para CF
        Filter filter1 = new FamilyFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes(columnFamilty1)));
        filters.add(filter1);
	    // crea el filtro para qualifier
        Filter filter2 = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("low")));
        filters.add(filter2);
	    // crea el filtro para el value que nos han pedido
        Filter filter3 = new ValueFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("24.81")));
        filters.add(filter3);

        // crea la lista de filtros con AND
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);

	    Scan scan = new Scan();
    	// asociamos al scan la lista de filtros
	    scan.setFilter(filterList);

	    // obtenemos el iterador sobre la lista de resultados
	    ResultScanner scanner = tabla.getScanner(scan);
	    // iteramos la lista de resultados
	    for (Result result : scanner) {
	      System.out.println(result);
	    }
	    // cerramos el scanner
	    scanner.close();
	    // cerramos la tabla
	    tabla.close();
    }

    /*
     * Borra las columnas price:high y price:low del registro rk=DELL con TS=872578800000
     */
    private void borraRegistro() throws IOException {

    	System.out.println("*** borraRegistro ***");
    	
    	// Creamos la instancia para acceder por RPC a HBase
    	HTable tabla=new HTable(conf, tableName);

    	// Creamos la instancia del objeto delete para la RK=DELL
    	Delete delete = new Delete(Bytes.toBytes("DELL"));
    	// añadimos la columna price:high
    	delete.deleteColumn(Bytes.toBytes("price"), Bytes.toBytes("high"), Long.valueOf("872578800000"));
    	// añadimos la columna price:low
    	delete.deleteColumn(Bytes.toBytes("price"), Bytes.toBytes("low"), Long.valueOf("872578800000"));
    	
    	// borra las celdas
    	tabla.delete(delete);
    	// cerramos la tabla
    	tabla.close();
    }

    /*
     * PROPUESTO 3
     * 
     * Visualizar los resultados de la fila DELL con timestamp 872578800000
     */
    
    private void propuesto3() throws IOException {
    	
    	System.out.println("*** propuesto3 ***");

    	// Creamos la instancia para acceder por RPC a HBase
    	HTable tabla=new HTable(conf, tableName);

    	// Creamos objeto para consultar una fila con RK = DELL
        Get get = new Get(Bytes.toBytes("DELL"));
        
        // Exigimos que el timestamp sea 
        get.setTimeStamp(872578800000l);
        
        // Consultamos y obtenermos el resultado
        Result result = tabla.get(get);

        // obtenemos los objetos KeyValue y los imprimimos
        List<Cell> listaResultados=result.listCells();
        for (Cell kv : listaResultados) {
            System.out.println("KeyValue: " + kv.toString());
        }
        
		tabla.close();
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

    public static void main(String[] args) throws IOException, InterruptedException {
    	
    	BasicoEmpresaResuelto modelo=new BasicoEmpresaResuelto();
    	
    	modelo.creaTabla();
    	modelo.creaRegistro();
    	modelo.propuesto1();
    	modelo.propuesto1a();
    	modelo.consultaRegistro();
    	modelo.scanRegistros();
    	modelo.propuesto2();
    	modelo.borraRegistro();
    	modelo.propuesto3();
    	modelo.borraTabla();
    }
    
}
