package ejercicios.histograma;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
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
import writables.HistogramaWritable2;
import constantes.Constantes;
import ejercicios.histograma.jobdos.MapJobDos;
import ejercicios.histograma.jobdos.RedJobDos;
import ejercicios.histograma.jobuno.MapLlimitesHistograma;
import ejercicios.histograma.jobuno.RedLimitesHistograma;

/** @author Álvaro Sánchez Blasco */
public class JobHistograma extends Configured implements Tool {
	public int run(String[] args) throws Exception {

		// Ojo que te lias.

		// Crear dos Job con la misma configuración. Cuando acaba el primero, se
		// pasan por parametro los datos de fin a la configuración, y luego, se
		// llama al segundo.

		if (args.length != 3) {
			System.out.println(Constantes.HELP_HISTOGRAMA);
			return -1;
		}

		Configuration config = getConf();

		Path inputPath = new Path(args[0]);
		Path outPath = new Path(args[1]);
		String numero_barras = args[2];
		config.set(Constantes.N, numero_barras);

		Path tempPath = new Path(Constantes.PATH_TEMPORAL);
		
		// Borramos todos los directorios que puedan existir
		FileSystem.get(tempPath.toUri(), config).delete(tempPath, true);
		FileSystem.get(outPath.toUri(), config).delete(outPath, true);

		Job jobLimites = Job.getInstance(config, "Limites Histograma Job");
		jobLimites.setJarByClass(JobHistograma.class);

		jobLimites.setInputFormatClass(TextInputFormat.class);
		jobLimites.setOutputFormatClass(TextOutputFormat.class);

		jobLimites.setMapOutputKeyClass(IntWritable.class);
		jobLimites.setMapOutputValueClass(HistogramaWritable2.class);

		jobLimites.setOutputKeyClass(IntWritable.class);
		jobLimites.setOutputValueClass(HistogramaWritable2.class);

		jobLimites.setMapperClass(MapLlimitesHistograma.class);
		jobLimites.setCombinerClass(RedLimitesHistograma.class);
		jobLimites.setReducerClass(RedLimitesHistograma.class);

		FileInputFormat.addInputPath(jobLimites, inputPath);
		FileOutputFormat.setOutputPath(jobLimites, tempPath);

		jobLimites.waitForCompletion(true);

		System.out.println(Constantes.FIN
				+ " para el job que calcula los límites");

		// Recuperar los limites del directorio temporal, y pasarlos al context.
		config = getLimites(config);

		Job jobHistograma = Job.getInstance(config, "Histograma Segundo Job");
		jobHistograma.setJarByClass(JobHistograma.class);

		jobHistograma.setInputFormatClass(TextInputFormat.class);
		jobHistograma.setOutputFormatClass(TextOutputFormat.class);

		jobHistograma.setMapOutputKeyClass(IntWritable.class);
		jobHistograma.setMapOutputValueClass(IntWritable.class);

		jobHistograma.setOutputKeyClass(IntWritable.class);
		jobHistograma.setOutputValueClass(IntWritable.class);

		jobHistograma.setMapperClass(MapJobDos.class);
		jobHistograma.setCombinerClass(RedJobDos.class);
		jobHistograma.setReducerClass(RedJobDos.class);

		FileInputFormat.addInputPath(jobHistograma, inputPath);
		FileOutputFormat.setOutputPath(jobHistograma, outPath);

		jobHistograma.waitForCompletion(true);

		System.out.println(Constantes.FIN
				+ " para el job que calcula el histograma");

		return 0;
	}

	public static void main(String args[]) throws Exception {
		ToolRunner.run(new JobHistograma(), args);
	}

	private static Configuration getLimites(Configuration config) throws IOException {
		// http://stackoverflow.com/questions/19379083/hadoop-map-task-read-the-content-of-a-specified-input-file
		Path pt = new Path(Constantes.PATH_TEMPORAL+"/part-r-00000");
		FileSystem fs = FileSystem.get(config);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				fs.open(pt)));
		String line;
		line = br.readLine();
		while (line != null) {
			String row[] = line.toString().split("\t");
			String minMax[] = row[1].split(",");
			System.out.println(line);
			// recuperar los datos de minimo y maximo y añadirselos a la
			// configuracion
			config.set(Constantes.MIN, minMax[0]);
			config.set(Constantes.MAX, minMax[1]);
			line = br.readLine();
		}

		return config;

	}
}