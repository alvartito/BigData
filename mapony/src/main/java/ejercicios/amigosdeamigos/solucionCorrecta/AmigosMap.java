package ejercicios.amigosdeamigos.solucionCorrecta;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import writables.AmigosWritable;
/** @author Álvaro Sánchez Blasco */
public class AmigosMap extends Mapper<LongWritable, Text, Text, AmigosWritable> {

	Text outKey = new Text();
	Text outKeyRev = new Text();
	AmigosWritable outValue = new AmigosWritable();
	AmigosWritable outValueRev = new AmigosWritable();

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
		outValue.set(true, amigoB);

		outKeyRev.set(amigoB);
		outValueRev.set(false, amigoA);

		context.write(outKey, outValue);
		context.write(outKeyRev, outValueRev);
		
	};
	
	
	
}
