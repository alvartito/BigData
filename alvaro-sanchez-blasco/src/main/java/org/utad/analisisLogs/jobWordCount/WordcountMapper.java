package org.utad.analisisLogs.jobWordCount;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.utad.analisisLogs.util.AnalisisLogsConstantes;
import org.utad.analisisLogs.util.UtilAnalisisLogs;
import org.utad.analisisLogs.writables.FechaHoraProcesoWritableComparable;

/*
 * Esta clase extiende la clase "Mapper". 
 * Los 4 generics representan los tipos respectivos de:
 * la clave de entrada (siempre es LongWritable para el TextInputFormat)
 * el valor de entrada (siempre es Text para el TextInputFormat)
 * la clave intermedia
 * el valor intermedio
 */
public class WordcountMapper
		extends
		Mapper<LongWritable, Text, FechaHoraProcesoWritableComparable, IntWritable> {

	FechaHoraProcesoWritableComparable keyOut = new FechaHoraProcesoWritableComparable();

	@Override
	/*
	 * Los 4 tipos del método map() deben corresponder a los 4 tipos definidos
	 * más arriba
	 */
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		/*
		 * Se recibe como valor una línea del fichero de entrada
		 */
		String[] word = value.toString().split(" ");

		String proc = word[4];

		// Primero, descartar todos los procesos que contengan vmet
		if (!proc.contains(AnalisisLogsConstantes.PROCESOS_DESCARTADOS)) {
			// Nos quedamos unicamente con el nombre del proceso.
			if (proc.contains("[")) {
				StringTokenizer st = new StringTokenizer(proc, "[");
				proc = st.nextToken();
				if(proc.contains("]")){
					proc = proc.replace("]", "");
				}
			}

			if (proc.contains(":")) {
				StringTokenizer st = new StringTokenizer(proc, ":");
				proc = st.nextToken();
			}

			// Ya tengo el proceso. Tengo que montar la fecha.
			String date = UtilAnalisisLogs.parseaFecha(word[0], word[1]);
			keyOut.setFecha(new Text(date));

			// Hay que recuperar la hora.
			String sHora = UtilAnalisisLogs.getSoloHora(word[2]);
			keyOut.setHora(new Text(sHora));

			// Ya tengo los datos necesarios para montar la clave.
			keyOut.setProceso(new Text(proc));

			/*
			 * por cada palabra, se cuenta "1"
			 */
			context.write(keyOut, new IntWritable(1));
		}
	}
}
