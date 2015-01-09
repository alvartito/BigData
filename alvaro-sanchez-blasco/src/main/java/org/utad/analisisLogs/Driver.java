package org.utad.analisisLogs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.utad.analisisLogs.partitioner.AnalisisLogsPartitioner;
import org.utad.analisisLogs.writables.KeyFechaHoraWritableComparable;
import org.utad.analisisLogs.writables.ProcesoContadorWritable;
import org.utad.analisisLogs.writables.comparator.AnalisisLogsComparator;
import org.utad.analisisLogs.writables.comparator.GroupIdAnalisisLogsComparator;

public class Driver  extends Configured implements Tool {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.printf("Usage: AnalisisLogsDriver <input dir> <output dir>\n");
			System.exit(-1);
		}

		ToolRunner.run(new Driver(), args);
	}

	@Override
	public int run(String[] args) throws Exception {

		Path inPath = new Path(args[0]);
		Path outPath = new Path(args[1]);
		
		Configuration config = getConf();

		Job job = Job.getInstance(config, "Analisis Logs");
		job.setJarByClass(AnalisisLogsDriver.class);
		
		// Borramos todos los directorios que puedan existir
		FileSystem.get(outPath.toUri(), config).delete(outPath, true);
		
		//TODO Añadir map, reducer, combiner, partitioner, comparator
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.setMapOutputKeyClass(KeyFechaHoraWritableComparable.class);
		job.setMapOutputValueClass(ProcesoContadorWritable.class);
		
		job.setOutputKeyClass(KeyFechaHoraWritableComparable.class);
		job.setOutputValueClass(ProcesoContadorWritable.class);

		job.setPartitionerClass(AnalisisLogsPartitioner.class);
		job.setSortComparatorClass(AnalisisLogsComparator.class);
		job.setGroupingComparatorClass(GroupIdAnalisisLogsComparator.class);
		
		job.setMapperClass(AnalisisLogsMapper.class);
		job.setReducerClass(AnalisisLogsReducer.class);
		
		// Establecemos el número de tareas Reduce
		job.setNumReduceTasks(2);
		
		boolean success = job.waitForCompletion(true);
		return (success ? 0 : 1);
	}

}
