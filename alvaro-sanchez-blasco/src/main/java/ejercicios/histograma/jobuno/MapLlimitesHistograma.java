package ejercicios.histograma.jobuno;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import alvaro.sanchez.blasco.util.writables.HistogramaWritable2;
/** @author Álvaro Sánchez Blasco */
public class MapLlimitesHistograma extends Mapper<LongWritable, Text, IntWritable, HistogramaWritable2> {

	IntWritable outKey = new IntWritable();
	IntWritable min = new IntWritable();
	IntWritable max = new IntWritable();

	/**
	 * Map(number, null) 
	 * 	–Emit(1, (number, number)) 
	 */

	@Override
	protected void map(LongWritable offset, Text line, Context context)
			throws IOException, InterruptedException {

		String row[] = line.toString().split("\t");
		String number = row[0];

		outKey.set(1);
		min.set(Integer.parseInt(number));
		max.set(Integer.parseInt(number));
		HistogramaWritable2 outValue = new HistogramaWritable2(min, max);

		context.write(outKey, outValue);
		
	};
}
