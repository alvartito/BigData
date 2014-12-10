package pd04.reduceJoinYMapJoin.sol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SolEmployeesMJ extends
		Mapper<LongWritable, Text, SolKeyRJMJ, SolUnionDatosMJ> {

	private BooleanWritable myTrue = new BooleanWritable(true);

	private IntWritable id = new IntWritable();
	private Text nombre = new Text();
	private Text apellido = new Text();
	private Text fechaInc = new Text();
	private Text dptName = new Text();

	private HashMap<String, String> dpt = new HashMap<String, String>();
	private HashMap<Integer, String> dptEmp = new HashMap<Integer, String>();

	@Override
	public void setup(Context context) throws IOException, InterruptedException {
		// Load the 2 files from the Distributed Cache and fill up the HashMaps

		BufferedReader brDpt = new BufferedReader(new FileReader(
				"load_departments.dump.clean"));
		BufferedReader brDptEmp = new BufferedReader(new FileReader(
				"load_dept_emp.dump.clean"));

		String line;
		while ((line = brDpt.readLine()) != null) {
			// ID dpt, nombre dpt
			// 'd001','Marketing'
			String words[] = line.split(",");
			dpt.put(words[0], words[1]);
		}
		brDpt.close();

		while ((line = brDptEmp.readLine()) != null) {
			// ID empleado, ID dpt, fecha inicio, fecha final
			// 10018,'d004','1992-07-29','9999-01-01'
			String words[] = line.split(",");
			
			// solo nos interesa el departamento actual de cada empleado
			if( words[3].equals( "'9999-01-01'")){
				dptEmp.put(Integer.parseInt(words[0]), words[1]);
			}
		}
		brDptEmp.close();
	}

	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		// input line is :
		// ID empleado, fecha Nacimiento, nombre, apellido, sexo, fecha incorporacion
		// 10001,'1953-09-02','Georgi','Facello','M','1986-06-26'
		String[] word = value.toString().split(",");
		int id_int = Integer.parseInt( word[0]);
		id.set( id_int);
		
		// Map employee with its department ID
		String dptId = null;
		if( dptEmp.containsKey( id_int)){
			dptId = dptEmp.get( id_int);
		}
		else {
			// case where employee does not belong anylonger to the company
			return;
		}
		
		// Map the department ID with the department name
		if( dpt.containsKey( dptId)){
			dptName.set( dpt.get( dptId));
		}
		else {
			// should not appear: every department ID should have a name
			System.err.println( "Error in map join with dpt table: "
					+ "cannot find the name for department ID " + dptId);
			return;
		}
		
		SolKeyRJMJ myKey = new SolKeyRJMJ( myTrue, id);
		
		nombre.set( word[2]);
		apellido.set( word[3]);
		fechaInc.set( word[5]);
		SolUnionDatosMJ val = new SolUnionDatosMJ( myTrue, nombre, apellido, fechaInc, dptName);

		context.write(myKey, val);
	}
}
