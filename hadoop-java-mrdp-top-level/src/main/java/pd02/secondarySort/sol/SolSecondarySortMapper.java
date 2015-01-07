package pd02.secondarySort.sol;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SolSecondarySortMapper extends
		Mapper<LongWritable, Text, CkIdNumWritableComparable, SongNumWritable> {

	private IntWritable id = new IntWritable();
	private Text song = new Text();
	private IntWritable num = new IntWritable();

	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		// input line is :
		// 248,"All my loving",13
		String[] word = value.toString().split(",");

		// first, don't consider the line if the song was written less than 5
		// time
		if (Integer.parseInt(word[2]) < 5)
			return;

		id.set(Integer.parseInt(word[0]));
		song.set(word[1]);
		num.set(Integer.parseInt(word[2]));

		CkIdNumWritableComparable myKey = new CkIdNumWritableComparable(id, num);
		SongNumWritable val = new SongNumWritable(song, num);
		context.write(myKey, val);
	}
}
