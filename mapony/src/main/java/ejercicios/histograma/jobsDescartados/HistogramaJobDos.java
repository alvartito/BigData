package ejercicios.histograma.jobsDescartados;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import writables.HistogramaWritable;
import constantes.Constantes;
import ejercicios.histograma.jobdos.MapJobDos;
import ejercicios.histograma.jobdos.RedJobDos;
/** @author Álvaro Sánchez Blasco */
public class HistogramaJobDos extends Configured implements Tool {

	public int run(String[] args) throws Exception {

		if (args.length != 3) {
			System.out.println(Constantes.HELP_HISTOGRAMA);
			return -1;
		}

		String input = args[0];
		String output = args[1];
		String numero_barras = args[2];

		Path outPath = new Path(output);
		FileSystem.get(outPath.toUri(), getConf()).delete(outPath, true);

		getConf().set(Constantes.N, numero_barras);

		Job job = Job.getInstance(getConf(), "Histograma Segundo Job");
		job.setJarByClass(HistogramaJobDos.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(HistogramaWritable.class);

		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(HistogramaWritable.class);

		job.setMapperClass(MapJobDos.class);
		job.setCombinerClass(RedJobDos.class);
		job.setReducerClass(RedJobDos.class);

		FileInputFormat.addInputPath(job, new Path(input));
		FileOutputFormat.setOutputPath(job, outPath);

		job.waitForCompletion(true);

		System.out.println(Constantes.FIN
				+ " para el job que calcula el histograma");

		return 0;
	}

	public static void main(String args[]) throws Exception {
		ToolRunner.run(new HistogramaJobDos(), args);
	}
}
