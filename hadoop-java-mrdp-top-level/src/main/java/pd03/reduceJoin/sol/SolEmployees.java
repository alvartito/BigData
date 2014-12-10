package pd03.reduceJoin.sol;

import java.io.IOException;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SolEmployees extends Mapper<LongWritable, Text, SolKeyRJ, SolUnionDatos> {

	private BooleanWritable myTrue = new BooleanWritable( true);
	
	private IntWritable id = new IntWritable();
	private Text nombre = new Text();
	private Text apellido = new Text();
	private Text fechaInc = new Text();

	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		// input line is :
		// ID empleado, fecha Nacimiento, nombre, apellido, sexo, fecha incorporacion
		// 10001,'1953-09-02','Georgi','Facello','M','1986-06-26'
		String[] word = value.toString().split(",");
		id.set( Integer.parseInt( word[0]));
		
		SolKeyRJ myKey = new SolKeyRJ( myTrue, id);
		
		nombre.set( word[2]);
		apellido.set( word[3]);
		fechaInc.set( word[5]);
		SolUnionDatos val = new SolUnionDatos( myTrue, nombre, apellido, fechaInc);

		context.write(myKey, val);
	}
	
}
