package util.reducers;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import util.writables.RawDataArrayWritable;
import util.writables.RawDataWritable;

/**
 * @author Álvaro Sánchez Blasco
 *
 */
public class MaponyGNArrayRed extends Reducer<Text, RawDataWritable, Text, ArrayWritable> {

	public void reduce(Text key, Iterable<RawDataWritable> values, Context context) throws IOException, InterruptedException {

		ArrayList<RawDataWritable> list = new ArrayList<RawDataWritable>();    
	    for (RawDataWritable val : values) {
	        list.add(new RawDataWritable(val));
	    }

	    
	    context.write(key, new RawDataArrayWritable(Text.class, list.toArray(new RawDataWritable[list.size()])));

	}
}