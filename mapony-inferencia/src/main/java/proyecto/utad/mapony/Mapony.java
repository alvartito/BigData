package proyecto.utad.mapony;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import proyecto.utad.mapony.maps.MaponyMap;
import proyecto.utad.mapony.reducers.MaponyRed;
import writables.CustomWritable;
import writables.RawDataWritable;

public class Mapony extends Configured implements Tool {

	public int run(String[] args) throws Exception {

		if (args.length != 2) {
			System.out.println("Entrada y salida");
			return -1;
		}

		Configuration config = getConf();

		Path inputPath = new Path(args[0]);
		Path outPath = new Path(args[1]);

		// Borramos todos los directorios que puedan existir
		FileSystem.get(outPath.toUri(), config).delete(outPath, true);

		Job jobMapony = Job.getInstance(config, "Mapony Job");
		jobMapony.setJarByClass(Mapony.class);

		jobMapony.setInputFormatClass(TextInputFormat.class);
		jobMapony.setOutputFormatClass(TextOutputFormat.class);

		jobMapony.setMapOutputKeyClass(Text.class);
		jobMapony.setMapOutputValueClass(CustomWritable.class);

		jobMapony.setOutputKeyClass(Text.class);
		jobMapony.setOutputValueClass(CustomWritable.class);

		jobMapony.setMapperClass(MaponyMap.class);
		jobMapony.setReducerClass(MaponyRed.class);

		jobMapony.setNumReduceTasks(6);

		FileInputFormat.addInputPath(jobMapony, inputPath);
		FileOutputFormat.setOutputPath(jobMapony, outPath);

		jobMapony.waitForCompletion(true);

		System.out.println("Fin ejecucion para el job mapony");

		return 0;
	}

	public static void main(String args[]) throws Exception {
		ToolRunner.run(new Mapony(), args);
	}
}
