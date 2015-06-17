package proyecto.utad.mapony.reducers;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import writables.CustomWritable;

public class MaponyRed extends Reducer<Text, CustomWritable, Text, CustomWritable> {

	Text amigoA = new Text();
	Text amigoB = new Text();

	protected void reduce(Text clave, CustomWritable valor, Context context) throws IOException, InterruptedException {
		context.write(clave, valor);
	};
}