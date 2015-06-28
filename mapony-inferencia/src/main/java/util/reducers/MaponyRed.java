package util.reducers;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class MaponyRed extends Reducer<Text, Text, Text, Text> {

	Text amigoA = new Text();
	Text amigoB = new Text();

	protected void reduce(Text clave, Text valor, Context context) throws IOException, InterruptedException {
		context.write(clave, valor);
	};
}