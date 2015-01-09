package org.utad.analisisLogs.jobSecondarySort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.utad.analisisLogs.AnalisisLogsDriver;
import org.utad.analisisLogs.jobWordCount.WordcountDriver;

public class SolSecondarySortDriver extends Configured implements Tool {
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.printf("Usage: SolSecondarySortDriver <input dir> <output dir>\n");
			System.exit(-1);
		}

		ToolRunner.run(new SolSecondarySortDriver(), args);
	}

	@Override
	public int run(String[] args) throws Exception {
		
		Path inPath = new Path(args[0]);
		Path outPath = new Path(args[1]);
		
		Configuration config = getConf();

		Job job = Job.getInstance(config, "Secondary Sort");
		job.setJarByClass(SolSecondarySortDriver.class);
		
		// Borramos todos los directorios que puedan existir
		FileSystem.get(outPath.toUri(), config).delete(outPath, true);
		
		//TODO Añadir map, reducer, combiner, partitioner, comparator
		FileInputFormat.setInputPaths(job, inPath);
		FileOutputFormat.setOutputPath(job, outPath);
		
		job.setMapOutputKeyClass(FechaHoraNumWritableComparable.class);
		job.setMapOutputValueClass(ProcesoNumWritable.class);
		job.setOutputKeyClass(IntWritable.class);
		
		job.setPartitionerClass(IdPartitioner.class);
		job.setSortComparatorClass(IdNumComparator.class);
		job.setGroupingComparatorClass(GroupIdComparator.class);
		
		job.setMapperClass(SolSecondarySortMapper.class);
		job.setReducerClass(SolSecondarySortReducer.class);
		
		job.setNumReduceTasks(2);
		
		boolean success = job.waitForCompletion(true);
		return success ? 0 : 1;
	}
	
}
