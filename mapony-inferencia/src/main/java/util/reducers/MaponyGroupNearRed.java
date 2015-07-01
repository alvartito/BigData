package util.reducers;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import util.beans.RawDataWritable;

public class MaponyGroupNearRed extends Reducer<Text, RawDataWritable, Text, RawDataWritable> {

	protected void reduce(Text clave, RawDataWritable valor, Context context) throws IOException, InterruptedException {
		context.write(clave, valor);
	};
}