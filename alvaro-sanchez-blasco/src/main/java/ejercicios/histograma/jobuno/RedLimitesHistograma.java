package ejercicios.histograma.jobuno;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import alvaro.sanchez.blasco.util.writables.HistogramaWritable2;

public class RedLimitesHistograma extends Reducer<IntWritable, HistogramaWritable2, IntWritable, HistogramaWritable2> {

	IntWritable minOut = new IntWritable();
	IntWritable maxOut = new IntWritable();
	/**
	 * Red(1, limits) 
	 * 	–Emit(1, (min, max)) 
	 */
	protected void reduce(IntWritable uno, Iterable<HistogramaWritable2> limits,
			Context context) throws IOException, InterruptedException {
		// - infinito
		Double max = new Double(-99999999);
		// + infinito
		Double min = new Double(999999999);
		
		for (HistogramaWritable2 limite : limits) {
			max = Math.max(max, new Double(limite.getMax().get()));
			min = Math.min(min, new Double(limite.getMin().get()));
		}
		
		minOut.set(min.intValue());
		maxOut.set(max.intValue());
		
		HistogramaWritable2 salida = new HistogramaWritable2(minOut,maxOut);
		
		context.write(new IntWritable(1), salida);
	};

}
