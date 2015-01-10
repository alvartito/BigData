package alvaro.sanchez.blasco.jobs.job1wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import alvaro.sanchez.blasco.writables.FechaHoraProcesoWritableComparable;

/**
 * @author cloudera
 * 
 * */
public class AnalisisLogsWordCountReducer
		extends
		Reducer<FechaHoraProcesoWritableComparable, IntWritable, FechaHoraProcesoWritableComparable, IntWritable> {

	private MultipleOutputs<FechaHoraProcesoWritableComparable, IntWritable> multipleOut;

	@Override
	public void reduce(FechaHoraProcesoWritableComparable key,
			Iterable<IntWritable> values, Context context) throws IOException,
			InterruptedException {
		int wordCount = 0;
		for (IntWritable value : values) {
			wordCount += value.get();
		}
		// context.write(key, new IntWritable(wordCount));
		multipleOut.write(key, new IntWritable(wordCount), generateFileName(key));
	}

	@Override
	protected void cleanup(Context context) throws IOException,
			InterruptedException {
		multipleOut.close();
	}

	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		multipleOut = new MultipleOutputs<FechaHoraProcesoWritableComparable, IntWritable>(
				context);
	}

	private String generateFileName(FechaHoraProcesoWritableComparable k) {
		// expect Text k in format "Surname|Forename"
		String dir = k.getFecha().toString();
		String fich = k.getHora().toString();
		
		String tmp = dir+"/"+fich;

		// example for k = Smith|John
		// output written to /user/hadoop/path/to/output/Smith/John-r-00000
		// (etc)
		return tmp;
	}
}
