package proyecto.utad.mapony;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MaponyMap extends Mapper<LongWritable, Text, Text, Text> {

	private Text outKey = new Text();
	private Text outVal = new Text();

	protected void map(LongWritable offset, Text line, Context context) throws IOException, InterruptedException {
		String[] logRecvArr = line.toString().split("\t");

		StringBuilder sbValue = new StringBuilder();
		int i = 0;
		for (String sCadena : logRecvArr) {
			sbValue.append(sCadena).append("-");
		}
		outKey.set((i++) + "");
		outVal.set(sbValue.toString());
		context.write(outKey, outVal);
	};
}
