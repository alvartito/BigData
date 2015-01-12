package alvaro.sanchez.blasco.jobs;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.CounterGroup;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import alvaro.sanchez.blasco.comparator.GroupIdComparator;
import alvaro.sanchez.blasco.comparator.IdNumComparator;
import alvaro.sanchez.blasco.jobs.job1wordcount.AnalisisLogsWordCountMapper;
import alvaro.sanchez.blasco.jobs.job1wordcount.AnalisisLogsWordCountReducer;
import alvaro.sanchez.blasco.jobs.job2secondarysort.AnalisisLogsSecondarySortMapper;
import alvaro.sanchez.blasco.jobs.job2secondarysort.AnalisisLogsSecondarySortReducer;
import alvaro.sanchez.blasco.partitioner.AnalisisLogsPartitioner;
import alvaro.sanchez.blasco.util.AnalisisLogsConstantes;
import alvaro.sanchez.blasco.writables.FechaHoraNumWritableComparable;
import alvaro.sanchez.blasco.writables.FechaHoraProcesoWritableComparable;
import alvaro.sanchez.blasco.writables.ProcesoNumWritable;

import com.google.common.collect.Lists;

/**
 * @author Álvaro Sánchez Blasco
 * 
 *         Driver para el proceso MR de Análisis de logs.
 * 
 * */
public class AnalisisLogsDriver extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.printf(AnalisisLogsConstantes.HELP_ANALISIS_LOGS);
			System.exit(-1);
		}

		ToolRunner.run(new AnalisisLogsDriver(), args);
	}

	public int run(String[] args) throws Exception {

		Path inPath = new Path(args[0]);
		Path tempPath = new Path(AnalisisLogsConstantes.CTE_PATH_TEMPORAL);
		Path outPath = new Path(args[1]);

		Configuration config = getConf();

		config.setStrings(AnalisisLogsConstantes.GRUPO_PROCESOS,
				AnalisisLogsConstantes.CONTADORES);

		Job job1wc = Job.getInstance(config, "Analisis Logs WC");
		job1wc.setJarByClass(AnalisisLogsDriver.class);

		// Borramos todos los directorios que puedan existir
		FileSystem.get(tempPath.toUri(), config).delete(tempPath, true);

		// Recuperamos los datos del path origen
		FileStatus[] glob = inPath.getFileSystem(getConf()).globStatus(inPath);

		// Tratamos todos los archivos incluidos en el directorio de orígen.
		for (FileStatus fileStatus : glob) {
			String fs = fileStatus.getPath().getName();
			String sbFicheros = "data/" + fs;
			MultipleInputs.addInputPath(job1wc, new Path(sbFicheros),
					TextInputFormat.class);
		}

		FileOutputFormat.setOutputPath(job1wc, tempPath);

		job1wc.setMapperClass(AnalisisLogsWordCountMapper.class);
		job1wc.setReducerClass(AnalisisLogsWordCountReducer.class);

		job1wc.setMapOutputKeyClass(FechaHoraProcesoWritableComparable.class);
		job1wc.setMapOutputValueClass(IntWritable.class);
		job1wc.setOutputKeyClass(FechaHoraProcesoWritableComparable.class);
		job1wc.setOutputValueClass(IntWritable.class);

		boolean success = job1wc.waitForCompletion(true);
		if (success) {
			Job job2ss = Job.getInstance(config, "Secondary Sort");
			job2ss.setJarByClass(AnalisisLogsDriver.class);

			// Borramos todos los directorios que puedan existir
			FileSystem.get(outPath.toUri(), config).delete(outPath, true);

			FileInputFormat
					.setInputPaths(job2ss, new Path(
							AnalisisLogsConstantes.CTE_PATH_TEMPORAL
									+ "/part-r-00000"));
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

			success = job2ss.waitForCompletion(true);

			CounterGroup cg = job2ss.getCounters().getGroup(
					AnalisisLogsConstantes.CONTADORES);

			if (null != cg && cg.size() > 0) {
				printCounters(cg);
			}
		}
		return success ? 0 : 1;
	}

	/**
	 * Método que muestra en consola el recuento de lineas de log asociado a
	 * cada componente, ordenado por número de registros leídos de mayor a
	 * menor.
	 * 
	 * @param counterGroup
	 *            a mostrar por pantalla
	 * @throws IOException
	 */
	private static void printCounters(CounterGroup counterGroup)
			throws IOException {
		System.out
				.println("\nRecuento de lineas de log asociadas a cada componente\n");

		List<Counter> counters = Lists.newArrayList(counterGroup.iterator());
		Collections.sort(counters, new Comparator<Counter>() {
			public int compare(Counter o1, Counter o2) {
				Long l1 = o1.getValue();
				Long l2 = o2.getValue();
				// Los que tengan mayor número de apariciones, primero.
				return -(l1.compareTo(l2));
			}
		});
		for (Counter counter : counters) {
			System.out.println(counter.getName() + ":" + counter.getValue());
		}
	}
}
