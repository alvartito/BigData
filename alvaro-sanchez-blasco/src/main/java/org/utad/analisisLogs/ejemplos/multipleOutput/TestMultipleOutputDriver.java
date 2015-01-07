package org.utad.analisisLogs.ejemplos.multipleOutput;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class TestMultipleOutputDriver {

	public static void main(String[] args) throws Exception {
		
		Configuration conf = new Configuration();
		
		Job job = new Job(conf);
		job.setJarByClass(TestMultipleOutputDriver.class);
		
		job.setJobName("Word Count");
		
		job.setMapperClass(TestMultipleOutputMapper.class);
		job.setReducerClass(TestMultipleOutputReducer.class);
		job.setInputFormatClass(KeyValueTextInputFormat.class);
		
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		//Esto es si queremos hacer varias salidas con diferente formato de salida.
		MultipleOutputs.addNamedOutput(job, "Autor", TextOutputFormat.class,  IntWritable.class, Text.class); 
		MultipleOutputs.addNamedOutput(job, "Paper", TextOutputFormat.class,  Text.class, Text.class); 
		MultipleOutputs.addNamedOutput(job, "PaperAutor", TextOutputFormat.class,  Text.class, IntWritable.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		boolean success = job.waitForCompletion(true);
		System.exit(success ? 0 : 1);
	}

}
