package org.utad.analisisLogs.ejemplos.hadoop.basic.wordcount;

import java.io.IOException;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class SortByOcurrences extends Configured implements Tool {
	
	public static class SwapMapper extends Mapper<Text, Text, IntWritable, Text> {

		IntWritable count = new IntWritable();
		
		public void map(Text word, Text countStr, Context context) throws IOException, InterruptedException {
			count.set(Integer.valueOf(countStr.toString()));
			context.write(count, word);			
		}
	}

	@Override
  public int run(String[] args) throws Exception {

		if(args.length != 2) {
			System.err.println("Usage: sort-by-ocurrences <in> <out>");
			System.exit(2);
		}

		Path output = new Path(args[1]);
		FileSystem.get(output.toUri(), getConf()).delete(output, true);
				
		Job job = new Job(getConf(), "sort by ocurrences");
		job.setJarByClass(SortByOcurrences.class);
		job.setInputFormatClass(KeyValueTextInputFormat.class);		
		job.setMapperClass(SwapMapper.class);		
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(Text.class);		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		// Optional for this particular case
		//job.setMapOutputValueClass(Text.class);
		//job.setMapOutputKeyClass(IntWritable.class);
		//job.setReducerClass(Reducer.class);
		//job.setOutputFormatClass(TextOutputFormat.class);

		job.waitForCompletion(true);
		
		return 0;
  }

	public static void main(String[] args) throws Exception {
		ToolRunner.run(new SortByOcurrences(), args);
	}
}
