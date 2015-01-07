package org.utad.analisisLogs.ejemplos.hadoop.basic.wordcount;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.utad.analisisLogs.ejemplos.hadoop.basic.sortjob.SortJobHadoop;

public class WordCountHadoop extends Configured implements Tool {
	
	@Override
  public int run(String[] args) throws Exception {

		if(args.length != 2) {
			System.err.println("Usage: wordcount-hadoop <in> <out>");
			System.exit(2);
		}

		Path output = new Path(args[1]);
		FileSystem.get(output.toUri(), getConf()).delete(output, true);
		
		Job job = new Job(getConf(), "word count hadoop");
		job.setJarByClass(WordCountHadoop.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));

		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
		
		return 0;
  }

	public static void main(String[] args) throws Exception {
		ToolRunner.run(new SortJobHadoop(), args);
	}
}
