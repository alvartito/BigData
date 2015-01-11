package alvaro.sanchez.blasco.jobs.job2secondarysort;

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

import alvaro.sanchez.blasco.comparator.GroupIdComparator;
import alvaro.sanchez.blasco.comparator.IdNumComparator;
import alvaro.sanchez.blasco.partitioner.AnalisisLogsPartitioner;
import alvaro.sanchez.blasco.util.AnalisisLogsConstantes;
import alvaro.sanchez.blasco.writables.FechaHoraNumWritableComparable;
import alvaro.sanchez.blasco.writables.ProcesoNumWritable;

public class SolSecondarySortDriver extends Configured implements Tool {
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.printf("Usage: SolSecondarySortDriver <output dir>\n");
			System.exit(-1);
		}

		ToolRunner.run(new SolSecondarySortDriver(), args);
	}

	public int run(String[] args) throws Exception {
		
		Path inPath = new Path("out_wc/part-r-00000");
		Path outPath = new Path(args[0]);
		
		Configuration config = getConf();

		Job job2ss = Job.getInstance(config, "Secondary Sort");
		job2ss.setJarByClass(SolSecondarySortDriver.class);
		
		// Borramos todos los directorios que puedan existir
		FileSystem.get(outPath.toUri(), config).delete(outPath, true);
		
		//TODO Añadir map, reducer, combiner, partitioner, comparator
		FileInputFormat.setInputPaths(job2ss, inPath);
		FileOutputFormat.setOutputPath(job2ss, outPath);
		
		job2ss.setMapOutputKeyClass(FechaHoraNumWritableComparable.class);
		job2ss.setMapOutputValueClass(ProcesoNumWritable.class);
		job2ss.setOutputKeyClass(IntWritable.class);
		
		job2ss.setPartitionerClass(AnalisisLogsPartitioner.class);
		job2ss.setSortComparatorClass(IdNumComparator.class);
		job2ss.setGroupingComparatorClass(GroupIdComparator.class);
		
		job2ss.setMapperClass(AnalisisLogsSecondarySortMapper.class);
		job2ss.setReducerClass(AnalisisLogsSecondarySortReducer.class);
		
		job2ss.setNumReduceTasks(2);
		
		boolean success = job2ss.waitForCompletion(true);
		
		long tipoAna = job2ss.getCounters().findCounter(AnalisisLogsConstantes.GRUPO_PROCESOS, "Ana")
				.getValue();
		
		
		
		return success ? 0 : 1;
	}
	
}
