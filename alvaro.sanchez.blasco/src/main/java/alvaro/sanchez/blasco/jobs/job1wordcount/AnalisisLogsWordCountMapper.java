package alvaro.sanchez.blasco.jobs.job1wordcount;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import alvaro.sanchez.blasco.util.AnalisisLogsConstantes;
import alvaro.sanchez.blasco.util.AnalisisLogsUtil;
import alvaro.sanchez.blasco.writables.FechaHoraProcesoWritableComparable;

/**
 * @author Álvaro Sánchez Blasco
 *
 *         Mapper del Job Word Count del proceso MR para Análisis de Logs.
 * 
 *         Se ha utilizado el patrón InMapperCombining para reducir el numero de
 *         pares key/value que salen del Mapper y se envian al Reducer.
 * 
 * */
public class AnalisisLogsWordCountMapper
		extends
		Mapper<LongWritable, Text, FechaHoraProcesoWritableComparable, IntWritable> {

	private static Map<FechaHoraProcesoWritableComparable, Integer> myWordMap;
	FechaHoraProcesoWritableComparable keyOut = new FechaHoraProcesoWritableComparable();

	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		if (myWordMap == null) {
			myWordMap = new HashMap<FechaHoraProcesoWritableComparable, Integer>();
		}
	}

	@Override
	public void map(LongWritable key, Text values, Context context)
			throws IOException, InterruptedException {

		String[] word = values.toString().split(" ");

		String proc = word[4];

		// Primero, descartar todos los procesos que contengan vmnet
		if (!proc.contains(AnalisisLogsConstantes.PROCESOS_DESCARTADOS)) {
			// Nos quedamos unicamente con el nombre del proceso.
			if (proc.contains("[")) {
				StringTokenizer st = new StringTokenizer(proc, "[");
				proc = st.nextToken();
				if (proc.contains("]")) {
					proc = proc.replace("]", "");
				}
			}

			if (proc.contains(":")) {
				StringTokenizer st = new StringTokenizer(proc, ":");
				proc = st.nextToken();
			}

			// Ya tengo el proceso. Tengo que montar la fecha.
			String date = AnalisisLogsUtil.parseaFecha(word[0], word[1]);
			keyOut.setFecha(new Text(date));

			// Hay que recuperar la hora.
			String sHora = AnalisisLogsUtil.getSoloHora(word[2]);
			keyOut.setHora(new Text(sHora));

			// Ya tengo los datos necesarios para montar la clave.
			keyOut.setProceso(new Text(proc));

			if (myWordMap.containsKey(keyOut)) {
				myWordMap.put(keyOut, myWordMap.get(keyOut) + 1);
			} else {
				myWordMap.put(keyOut, 1);
			}
		}
		flush(context);
	}

	private void flush(Context context) throws IOException,
			InterruptedException {
		Iterator<FechaHoraProcesoWritableComparable> iterator = myWordMap
				.keySet().iterator();
		while (iterator.hasNext()) {
			FechaHoraProcesoWritableComparable key = iterator.next();
			context.write(key, new IntWritable(myWordMap.get(key)));
		}
		myWordMap.clear();
	}

	@Override
	protected void cleanup(Context context) throws IOException,
			InterruptedException {
		flush(context);
	}
}
