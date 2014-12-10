package ejemplos.pd0.wordcount;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;

public class WordcountDriver {
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.printf("Usage: WordCount <input dir> <output dir>\n");
			System.exit(-1);
		}
		Job job = new Job();
		job.setJarByClass(WordcountDriver.class);
		job.setJobName("Wordcount");

		/* Indicamos las carpetas HDFS de entrada y de salida del job
		 * Importante: la carpeta de resultados (salida) no debe existir
		 * cuando lanzamos el job
		 */
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		/* Se definen la clase Map y la clase Reduce del job
		 * Si os olvidais específicar uno de ellos, no significa que esta fase
		 * no existirá, sino más bien que esta fase se ejecutará con la clase
		 * por defecto (=clase Identidad)
		 */
		job.setMapperClass(WordcountMapper.class);
		job.setReducerClass(WordcountReducer.class);
		
		/* No se puede elegir el número de tareas Map que ejecutará el job.
		 * Sin embargo, sí podemos elegir el número de tareas Reduce.
		 * Aquí, como ejemplo se han elegido 2 reducers. Si ponemos 0, entonces 
		 * no se ejecuta la fase Reduce: tendremos un job Map-Only.
		 */
		job.setNumReduceTasks(2);

		/* Definición de las clases de las claves y valores.
		 * En el caso de la clave del Map no es necesario de especificarlo
		 * ya que se trata de la clase que se usa por defecto (Text).
		 */
		// job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		boolean success = job.waitForCompletion(true);
		System.exit(success ? 0 : 1);
	}
}
