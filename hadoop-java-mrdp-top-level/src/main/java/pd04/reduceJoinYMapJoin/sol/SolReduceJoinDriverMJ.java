package pd04.reduceJoinYMapJoin.sol;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class SolReduceJoinDriverMJ extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {

		if (args.length != 3) {
			System.out
					.printf("Usage: SolReduceJoinDriverMJ <salary file> <employee file> <output dir>\n");
			System.exit(-1);
		}
		
		Configuration conf = getConf();
		// TODO arreglar esto
		// DistributedCache.addCacheFile( new URI("/tmp/load_departments.dump.clean"), conf);
		
		
		Job job = new Job( conf);
		job.setJarByClass(SolReduceJoinDriverMJ.class);
		job.setJobName("Reduce Join y Map Join");

		MultipleInputs.addInputPath(job, new Path(args[0]),
				TextInputFormat.class, SolSalariesMJ.class);
		MultipleInputs.addInputPath(job, new Path(args[1]),
				TextInputFormat.class, SolEmployeesMJ.class);

		FileOutputFormat.setOutputPath(job, new Path(args[2]));

		job.setMapOutputKeyClass(SolKeyRJMJ.class);
		job.setMapOutputValueClass(SolUnionDatosMJ.class);
		job.setOutputKeyClass(SolKeyRJMJ.class);
		job.setOutputValueClass(SolUnionDatosMJ.class);

		job.setReducerClass(SolReduceJoinReducerMJ.class);

		job.setGroupingComparatorClass(SolReduceJoinGroupIdComparatorMJ.class);

		job.setNumReduceTasks(4);

		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run( new Configuration(), 
				new SolReduceJoinDriverMJ(), args);
		System.exit(exitCode);
	}

}
