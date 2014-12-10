package ejemplos.pd0.wordcount;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/*
 * Esta clase extiende la clase "Mapper". 
 * Los 4 generics representan los tipos respectivos de:
 * la clave de entrada (siempre es LongWritable para el TextInputFormat)
 * el valor de entrada (siempre es Text para el TextInputFormat)
 * la clave intermedia
 * el valor intermedio
 */
public class WordcountMapper extends
		Mapper<LongWritable, Text, Text, IntWritable> {

	@Override
	/*
	 * Los 4 tipos del método map() deben corresponder a los 4 tipos definidos más arriba
	 */
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		/*
		 * Se recibe como valor una línea del fichero de entrada
		 */
		String line = value.toString();
		/*
		 * De esta línea, sacamos la lista de las palabras
		 */
		for (String word : line.split("\\W+")) {
			if (word.length() > 0) {
				/* 
				 * por cada palabra, se cuenta "1"
				 */
				context.write(new Text(word), new IntWritable(1));
			}
		}
	}
}
