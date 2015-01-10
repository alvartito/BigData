package alvaro.sanchez.blasco.jobs.job1wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import alvaro.sanchez.blasco.writables.FechaHoraProcesoWritableComparable;

/**
 * @author cloudera
 * 
 * */
public class WordcountDriver extends Configured implements Tool {
	
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.printf("Usage: AnalisisLogs <input dir> <output dir>\n");
			System.exit(-1);
		}

		ToolRunner.run(new WordcountDriver(), args);
		
	}

	public int run(String[] args) throws Exception {
		
		Path inPath = new Path(args[0]);
		Path outPath = new Path(args[1]);
		
		Configuration config = getConf();

		Job job1wc = Job.getInstance(config, "Analisis Logs WC");
		job1wc.setJarByClass(WordcountDriver.class);
		
		// Borramos todos los directorios que puedan existir
		FileSystem.get(outPath.toUri(), config).delete(outPath, true);
		
		// Recuperamos los datos del path origen
		FileStatus[] glob = inPath.getFileSystem(getConf()).globStatus(inPath);
		
		
		//TODO Añadir map, reducer, combiner, partitioner, comparator
		for (FileStatus fileStatus : glob) {
			String fs = fileStatus.getPath().getName();
			String sbFicheros = "data/"+fs;
			MultipleInputs.addInputPath(job1wc, new Path(sbFicheros), TextInputFormat.class);
			
			//sFileInputFormat.setInputPaths(job, );	
		}

		FileOutputFormat.setOutputPath(job1wc, new Path(args[1]));

		/* Se definen la clase Map y la clase Reduce del job
		 * Si os olvidais específicar uno de ellos, no significa que esta fase
		 * no existirá, sino más bien que esta fase se ejecutará con la clase
		 * por defecto (=clase Identidad)
		 */
		job1wc.setMapperClass(AnalisisLogsWordCountMapper.class);
		job1wc.setReducerClass(AnalisisLogsWordCountReducer.class);
		
		/* No se puede elegir el número de tareas Map que ejecutará el job.
		 * Sin embargo, sí podemos elegir el número de tareas Reduce.
		 * Aquí, como ejemplo se han elegido 2 reducers. Si ponemos 0, entonces 
		 * no se ejecuta la fase Reduce: tendremos un job Map-Only.
		 */
		//job.setNumReduceTasks(2);

		/* Definición de las clases de las claves y valores.
		 * En el caso de la clave del Map no es necesario de especificarlo
		 * ya que se trata de la clase que se usa por defecto (Text).
		 */
		job1wc.setMapOutputKeyClass(FechaHoraProcesoWritableComparable.class);
		job1wc.setMapOutputValueClass(IntWritable.class);
		job1wc.setOutputKeyClass(FechaHoraProcesoWritableComparable.class);
		job1wc.setOutputValueClass(IntWritable.class);
		
		boolean success = job1wc.waitForCompletion(true);
		return (success ? 0 : 1);
	}
}
