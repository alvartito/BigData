package org.utad.analisisLogs.jobWordCount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.utad.analisisLogs.writables.FechaHoraProcesoWritableComparable;

/*
 * Esta clase extiende la clase "Reducer". 
 * Los 4 generics representan los tipos respectivos de:
 * la clave intermedia
 * el valor intermedio
 * la clave de salida
 * el valor de salida
 */
public class WordcountReducer extends
		Reducer<FechaHoraProcesoWritableComparable, IntWritable, FechaHoraProcesoWritableComparable, IntWritable> {

	@Override
	/*
	 * Los 4 tipos del método map() deben corresponder a los 4 tipos definidos más arriba
	 */
	public void reduce(FechaHoraProcesoWritableComparable key, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException {
		int wordCount = 0;
		/*
		 * el key es una palabra. Por cada palabra, se suma el número de veces que la hemos visto
		 */
		for (IntWritable value : values) {
			wordCount += value.get();
		}
		context.write(key, new IntWritable(wordCount));
	}
}
