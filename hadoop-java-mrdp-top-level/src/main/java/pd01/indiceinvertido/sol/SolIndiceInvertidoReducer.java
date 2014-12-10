package pd01.indiceinvertido.sol;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SolIndiceInvertidoReducer extends Reducer<Text, Text, Text, Text> {

	Text references = new Text();

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

		StringBuilder sb = new StringBuilder();

		for (Text ref : values) {
			sb.append(ref).append(",");
		}
		references.set(sb.toString());
		context.write(key, references);
	}
}
