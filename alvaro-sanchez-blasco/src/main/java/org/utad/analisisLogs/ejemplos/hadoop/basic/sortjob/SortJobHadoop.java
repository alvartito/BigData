package org.utad.analisisLogs.ejemplos.hadoop.basic.sortjob;


import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Simple example job that writes the same records that reads
 * but sorted (input must be text)
 */
public class SortJobHadoop extends Configured implements Tool {
			
  @Override
  public int run(String[] args) throws Exception {
		if(args.length != 2) {
			System.out.println("Invalid number of arguments\n\n" +
					"Usage: sort-hadoop <input_path> <output_path>\n\n");
			return -1;
		}
		String input = args[0];
		String output = args[1];
		
		Path oPath = new Path(output);
		FileSystem.get(oPath.toUri(), getConf()).delete(oPath, true);
		
		Job job = new Job(getConf(), "Sort Hadoop");
		job.setJarByClass(SortJobHadoop.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		
		job.setMapperClass(LineAsKeyMap.class);
		job.setReducerClass(IdentityReducer.class); // Equivalent to job.setReducerClass(Reducer.class) 		
		
		FileInputFormat.addInputPath(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));
		
		job.waitForCompletion(true);
		
		return 0;
  }

	public static void main(String args[]) throws Exception {
		ToolRunner.run(new SortJobHadoop(), args);
	}
}
