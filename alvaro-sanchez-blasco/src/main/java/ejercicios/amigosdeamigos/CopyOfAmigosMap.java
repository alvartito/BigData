package ejercicios.amigosdeamigos;

import java.io.IOException;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import writables.AmigosWritable2;
/** @author Álvaro Sánchez Blasco */
public class CopyOfAmigosMap extends Mapper<LongWritable, Text, Text, AmigosWritable2> {

	Text outKey = new Text();
	Text outKeyRev = new Text();
	AmigosWritable2 outValue = new AmigosWritable2();
	AmigosWritable2 outValueRev = new AmigosWritable2();

	/**
	 * Map(a, b) 
	 * 	–Emit(a, (true, b)) 
	 * 	–Emit(b, (false, a))
	 */

	@Override
	protected void map(LongWritable offset, Text line, Context context)
			throws IOException, InterruptedException {
		String row[] = line.toString().split(",");
		String amigoA = row[0];
		String amigoB = row[1];

		outKey.set(amigoA);
		outValue.set(new BooleanWritable(true), new Text(amigoB));

		outKeyRev.set(amigoB);
		outValueRev.set(new BooleanWritable(false), new Text(amigoA));

		context.write(outKey, outValue);
		context.write(outKeyRev, outValueRev);
		
	};
	
	
	
}
