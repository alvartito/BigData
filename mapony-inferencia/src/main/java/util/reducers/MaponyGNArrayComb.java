package util.reducers;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import util.writables.RawDataWritable;

/**
 * @author Álvaro Sánchez Blasco
 *
 */
public class MaponyGNArrayComb extends Reducer<Text, RawDataWritable, Text, RawDataWritable> {

	public void reduce(Text key, Iterable<RawDataWritable> values, Context context) throws IOException, InterruptedException {
		for (RawDataWritable val : values) {
			context.write(key, val);
		}
	}
}