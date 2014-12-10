package pd03.reduceJoin.sol;

import java.io.IOException;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SolSalaries extends Mapper<LongWritable, Text, SolKeyRJ, SolUnionDatos> {
	
	private BooleanWritable myFalse = new BooleanWritable( false);
	
	private IntWritable id = new IntWritable();
	private IntWritable sueldo = new IntWritable();
	private Text fechaIni = new Text();
	private Text fechaFin = new Text();

	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		// input line is :
		// ID empleado, sueldo, fecha inicio, fecha final
		// 10001,60117,'1986-06-26','1987-06-26'
		String[] word = value.toString().split(",");
		id.set( Integer.parseInt( word[0]));
		
		SolKeyRJ myKey = new SolKeyRJ( myFalse, id);
		
		sueldo.set( Integer.parseInt( word[1]));
		fechaIni.set( word[2]);
		fechaFin.set( word[3]);
		SolUnionDatos val = new SolUnionDatos( myFalse, sueldo, fechaIni, fechaFin);

		context.write(myKey, val);
	}
}
