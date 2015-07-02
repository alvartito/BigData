package util.reducers;

import java.io.IOException;

import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import util.writables.RawDataWritable;

/**
 * @author Álvaro Sánchez Blasco
 *
 */
public class MaponyGNArrayRed extends Reducer<Text, RawDataWritable, Text, MapWritable> {

	public void reduce(Text key, Iterable<RawDataWritable> values, Context context) throws IOException, InterruptedException {
		MapWritable mapWritable = new MapWritable();
		for (RawDataWritable val : values) {
			mapWritable.put(key, new Text(val.toString()));
		}
		context.write(key, mapWritable);
	}
}