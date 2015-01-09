/**
 * 
 */
package org.utad.analisisLogs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.utad.analisisLogs.jobWordCount.WordcountDriver;
import org.utad.analisisLogs.jobWordCount.WordcountMapper;
import org.utad.analisisLogs.jobWordCount.WordcountReducer;
import org.utad.analisisLogs.writables.FechaHoraProcesoWritableComparable;

/**
 * TODO para el tema de los contadores, @see WordCounterDriver
 * TODO para el tema de los partitioner, @see PersonaScoreDriver
 * @author Álvaro Sánchez Blasco
 *
 */
public class AnalisisLogsDriver extends Configured implements Tool {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.printf("Usage: AnalisisLogsDriver <input dir> <output dir>\n");
			System.exit(-1);
		}

		ToolRunner.run(new AnalisisLogsDriver(), args);
	}

	@Override
	public int run(String[] args) throws Exception {
		
		Path inPath = new Path(args[0]);
		Path outPath = new Path(args[1]);
		Path tempPath = new Path("out_tmp");
		
		Configuration config = getConf();

		Job job = Job.getInstance(config, "Analisis Logs WC");
		job.setJarByClass(WordcountDriver.class);
		
		// Borramos todos los directorios que puedan existir
		FileSystem.get(outPath.toUri(), config).delete(outPath, true);
		
		FileInputFormat.setInputPaths(job, inPath);
		FileOutputFormat.setOutputPath(job, outPath);

		job.setMapperClass(WordcountMapper.class);
		job.setReducerClass(WordcountReducer.class);

		job.setMapOutputKeyClass(FechaHoraProcesoWritableComparable.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setOutputKeyClass(FechaHoraProcesoWritableComparable.class);
		job.setOutputValueClass(IntWritable.class);
		
		boolean success = job.waitForCompletion(true);
		return (success ? 0 : 1);
	}

}
