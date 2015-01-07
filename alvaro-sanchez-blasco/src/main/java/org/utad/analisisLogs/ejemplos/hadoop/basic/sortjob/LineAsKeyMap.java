package org.utad.analisisLogs.ejemplos.hadoop.basic.sortjob;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class LineAsKeyMap extends Mapper<LongWritable, Text, Text, NullWritable> {

	@Override
	protected void map(LongWritable offset, Text line, Context context) 
			throws IOException ,InterruptedException {
		context.write(line, NullWritable.get());
	};
	
}