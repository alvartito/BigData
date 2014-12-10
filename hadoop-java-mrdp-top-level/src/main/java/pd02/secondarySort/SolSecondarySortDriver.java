package pd02.secondarySort;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class SolSecondarySortDriver {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.printf("Usage: SolSecondarySortDriver <input dir> <output dir>\n");
			System.exit(-1);
		}
		Job job = new Job();
		job.setJarByClass(SolSecondarySortDriver.class);
		job.setJobName("Secondary Sort");

		
		Path inputPath = new Path(args[0]);
		Path outPath = new Path(args[1]);
				
		FileInputFormat.setInputPaths(job, inputPath);
		FileOutputFormat.setOutputPath(job, outPath);
		FileSystem.get(outPath.toUri(), job.getConfiguration()).delete(outPath, true);

		FileInputFormat.setInputPaths(job, inputPath);
		FileOutputFormat.setOutputPath(job, outPath);
		
		/* Se definen la clase Map y la clase Reduce del job
		 * Si os olvidais específicar uno de ellos, no significa que esta fase
		 * no existirá, sino más bien que esta fase se ejecutará con la clase
		 * por defecto (=clase Identidad)
		 */		
		job.setMapperClass(SolSecondarySortMapper.class);
		job.setReducerClass(SolSecondarySortReducer.class);
		
		job.setPartitionerClass(IdPartitioner.class);
		job.setSortComparatorClass(IdNumComparator.class);
		job.setGroupingComparatorClass(GroupIdComparator.class);

		
		// TODO configurar el job
		
		job.setNumReduceTasks(2);
		
		boolean success = job.waitForCompletion(true);
		System.exit(success ? 0 : 1);
	}
	
}
