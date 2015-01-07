package pd02.secondarySort.sol;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SolSecondarySortReducer extends
		Reducer<CkIdNumWritableComparable, SongNumWritable, IntWritable, Text> {
	Text outputValue = new Text();

	@Override
	protected void reduce(CkIdNumWritableComparable key, Iterable<SongNumWritable> values, Context context)
			throws IOException, InterruptedException {
		StringBuilder sb = new StringBuilder("");
		for (SongNumWritable sn: values){
			sb = sb.append( sn.toString());
		}
		outputValue.set( sb.toString());
		context.write(key.getId(), outputValue);
	}

}
