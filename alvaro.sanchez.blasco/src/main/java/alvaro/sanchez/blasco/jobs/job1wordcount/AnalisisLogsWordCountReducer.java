package alvaro.sanchez.blasco.jobs.job1wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import alvaro.sanchez.blasco.writables.FechaHoraProcesoWritableComparable;

/**
 * @author cloudera
 * 
 * */
public class AnalisisLogsWordCountReducer
		extends
		Reducer<FechaHoraProcesoWritableComparable, IntWritable, FechaHoraProcesoWritableComparable, IntWritable> {

	@Override
	public void reduce(FechaHoraProcesoWritableComparable key,
			Iterable<IntWritable> values, Context context) throws IOException,
			InterruptedException {
		int wordCount = 0;
		for (IntWritable value : values) {
			wordCount += value.get();
		}
		context.write(key, new IntWritable(wordCount));
	}
}
