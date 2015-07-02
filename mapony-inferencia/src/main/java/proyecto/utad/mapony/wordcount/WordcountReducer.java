package proyecto.utad.mapony.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import util.constantes.MaponyCte;

/*
 * Esta clase extiende la clase "Reducer". 
 * Los 4 generics representan los tipos respectivos de:
 * la clave intermedia
 * el valor intermedio
 * la clave de salida
 * el valor de salida
 */
public class WordcountReducer extends
		Reducer<Text, Text, Text, Text> {

	@Override
	/*
	 * Los 4 tipos del método map() deben corresponder a los 4 tipos definidos más arriba
	 */
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		Text wordCount = new Text();
		StringBuilder sbOut = new StringBuilder();
		/*
		 * el key es una palabra. Por cada palabra, se suma el número de veces que la hemos visto
		 */
		for (Text value : values) {
			sbOut.append(value.toString()).append(MaponyCte.PIPE);
		}
		wordCount = new Text(sbOut.toString());
		context.write(key, wordCount);
//		context.write(key, wordCount);
	}
}
