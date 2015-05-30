package proyecto.utad.mapony;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import writables.RawDataWritable;

public class MaponyRed extends Reducer<Text, RawDataWritable, Text, RawDataWritable> {

	Text amigoA = new Text();
	Text amigoB = new Text();

	protected void reduce(Text clave, RawDataWritable valor, Context context) throws IOException, InterruptedException {
		context.write(clave, valor);
	};
}