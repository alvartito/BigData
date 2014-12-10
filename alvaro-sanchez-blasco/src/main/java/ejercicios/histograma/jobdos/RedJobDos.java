package ejercicios.histograma.jobdos;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
/** @author Álvaro Sánchez Blasco */
public class RedJobDos extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable>{

	protected void reduce(IntWritable bar, Iterable<IntWritable> counts,
			Context context) throws IOException, InterruptedException {
		int total = 0;
		for (IntWritable count : counts) {
			total += Integer.parseInt(count.toString());
		}
		context.write(bar, new IntWritable(total));
	}
	
}
