package org.utad.analisisLogs.ejemplos.multipleOutput;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class TestMultipleOutputReducer extends Reducer<Text, Text, Text, Text> {
	private MultipleOutputs<Text, Text> multipleOut;

	@Override
	protected void cleanup(Context context) throws IOException,
			InterruptedException {
		multipleOut.close();
	}

	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		multipleOut = new MultipleOutputs<Text, Text>(context);
	}

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		String name = key.toString().replace("-", "");
		for (Text value : values) {
			multipleOut.write(key, value, name);
			// Podría añadir más salidas según mis necesidades
			// a través de cláusulas if, o porque un par key/value
			// traiga diversas informaciones que quiero subdividir
			// en diferentes ficheros
			// if(caso1) multipleOut.write( key, value, name2);
			// multipleOut.write( key, value, name3);
		}
	}
}