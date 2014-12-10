package pd05.nline.sol;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class SolNlineDriver {
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out
					.printf("Usage: NLine <input dir> <output dir>\n");
			System.exit(-1);
		}
		Job job = new Job();
		job.setJarByClass(SolNlineDriver.class);
		job.setJobName("Parameter sweep");

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.setMapperClass(SolNlineMapper.class);

		job.setInputFormatClass(NLineInputFormat.class);

		job.setOutputKeyClass(NullWritable.class);
		
		job.setNumReduceTasks(0);
		
		boolean success = job.waitForCompletion(true);
		System.exit(success ? 0 : 1);
	}
}
