package pd06.grafos.sol;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class SolGrafosDrivers {
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.printf("Usage: Grafos <input dir> <output dir>\n");
			System.exit(-1);
		}

		// an iteration for every MR job
		for (int i = 1; i < 10; i++) { // set a hard core stop: we should not
										// iterate more than 10 times
			Job job = new Job();
			job.setJarByClass(SolGrafosDrivers.class);
			job.setJobName("Grafos, iteration = " + i);

			// Prepare temporal path
			String inputPath;
			if (i == 1) {
				inputPath = args[0];
			} else {
				int previousIteration = i - 1;
				inputPath = args[1] + "_iteration_" + previousIteration;
			}
			String outputPath = args[1] + "_iteration_" + i;
			FileInputFormat.setInputPaths(job, new Path(inputPath));
			FileOutputFormat.setOutputPath(job, new Path(outputPath));

			job.setMapperClass(SolGrafosMapper.class);
			job.setReducerClass(SolGrafosReducer.class);

			job.setMapOutputKeyClass(IntWritable.class);
			job.setMapOutputValueClass(SolNodeState.class);
			job.setOutputKeyClass(IntWritable.class);
			job.setOutputValueClass(SolNodeState.class);

			job.setInputFormatClass(KeyValueTextInputFormat.class);

			boolean success = job.waitForCompletion(true);
			if (!success) {
				System.err
						.println("Error in MR: job failed for iteration " + i);
				System.exit(1);
			}

			// iteration stop condition based on counters
			long totalNodes = job.getCounters()
					.findCounter("nodes", "totalNumber").getValue();
			long reachedNodes = job.getCounters()
					.findCounter("nodes", "reachedNumber").getValue();
			if (totalNodes == reachedNodes) {
				System.out.println("\n\nThe algorithm has stopped.\n"
						+ " We have reached the total number of nodes: "
						+ reachedNodes);
				System.exit(0);
			}
		}
	}
}
