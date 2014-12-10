package pd03.reduceJoin.sol;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class SolReduceJoinDriver {

	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			System.out.printf("Usage: SolReduceJoinDriver <salary file> <employee file> <output dir>\n");
			System.exit(-1);
		}
		Job job = new Job();
		job.setJarByClass(SolReduceJoinDriver.class);
		job.setJobName("Reduce Join");
		
		MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class,
				SolSalaries.class);
		MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class,
				SolEmployees.class);

		FileOutputFormat.setOutputPath(job, new Path(args[2]));
		
		job.setMapOutputKeyClass(SolKeyRJ.class);
		job.setMapOutputValueClass(SolUnionDatos.class);
		job.setOutputKeyClass(SolKeyRJ.class);
		job.setOutputValueClass(SolUnionDatos.class);
		
		job.setReducerClass(SolReduceJoinReducer.class);
		
		job.setGroupingComparatorClass(SolReduceJoinGroupIdComparator.class);
		
		job.setNumReduceTasks(4);
		
		boolean success = job.waitForCompletion(true);
		System.exit(success ? 0 : 1);
	}
	
}
