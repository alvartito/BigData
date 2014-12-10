package pd02.secondarySort;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SolSecondarySortMapper extends
		Mapper<LongWritable, Text, CkIdNum, SongNum> {

	private IntWritable id = new IntWritable();
	private Text song = new Text();
	private IntWritable num = new IntWritable();
	
	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		// input line is :
		// 248,"All my loving",13
		
		// no olvidar quitar las canciones que se han escuchado menos de 5 veces

		// TODO rellenar
		CkIdNum myKey = new CkIdNum(id, num);
		SongNum val = new SongNum(song, num);
		context.write(myKey, val);


	}
}
