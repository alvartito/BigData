package pd02.secondarySort;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SolSecondarySortReducer extends
		Reducer<CkIdNum, SongNum, IntWritable, Text> {

	@Override
	protected void reduce(CkIdNum key, Iterable<SongNum> values, Context context)
			throws IOException, InterruptedException {
		// TODO rellenar
	}
}
