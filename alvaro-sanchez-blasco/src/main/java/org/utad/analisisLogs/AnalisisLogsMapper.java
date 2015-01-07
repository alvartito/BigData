package org.utad.analisisLogs;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.utad.analisisLogs.util.AnalisisLogsConstantes;
import org.utad.analisisLogs.writables.KeyFechaHoraWritableComparable;
import org.utad.analisisLogs.writables.ProcesoContadorWritable;

import alvaro.sanchez.blasco.util.ParseMeses;


/** 
 * TODO para el tema de los contadores, @see WordCounterMapper
 * @author Álvaro Sánchez Blasco. 
 * 
 * */
public class AnalisisLogsMapper extends
		Mapper<LongWritable, Text, KeyFechaHoraWritableComparable, ProcesoContadorWritable> {

	private Text fecha = new Text();
	private Text hora = new Text();
	private Text proceso = new Text();
	private IntWritable contador = new IntWritable();

	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		// Input line:
		// Nov 29 06:07:36 ktalina anacron[2532]: Job `cron.daily' terminated
		// Nov 29 06:07:36 ktalina anacron[2532]: Normal exit (1 job run)
		// Nov 29 06:14:51 ktalina NetworkManager[1070]: <warn> nl_recvmsgs() error: (-33) Dump inconsistency detected, interrupted
		// Nov 29 06:17:01 ktalina CRON[4540]: (root) CMD (   cd / && run-parts --report /etc/cron.hourly)

				
		String[] word = value.toString().split(" ");

		String proc = word[4];
		
		// Primero, descartar todos los procesos que contengan vmet
		if(!proc.contains(AnalisisLogsConstantes.PROCESOS_DESCARTADOS)) {
			// Nos quedamos unicamente con el nombre del proceso.
			if (proc.contains("[")) {
				StringTokenizer st = new StringTokenizer(
						proc, "[");
				proc = st.nextToken();
			}
			
			if (proc.contains(":")) {
				StringTokenizer st = new StringTokenizer(
						proc, ":");
				proc = st.nextToken();
			}
			
			//Ya tengo el proceso. Tengo que montar la fecha.
			String date = parseaFecha(word[0], word[1]);
			fecha.set(date);
			
			//Hay que recuperar la hora.
			String sHora = getSoloHora(word[2]);
			hora.set(sHora);
			
			// Ya tengo los datos necesarios para montar la clave.
			proceso.set(proc);
			contador.set(1);
		}

		// La clave
		KeyFechaHoraWritableComparable myKey = new KeyFechaHoraWritableComparable(fecha, hora);
		// El valor
		ProcesoContadorWritable val = new ProcesoContadorWritable(proceso, contador);
		context.write(myKey, val);
	}
	
	/**
	 * Metodo que, recibiendo un literal del mes, y un dia, monta la fecha con
	 * el formato indicado, parseando el literal a numero y añadiendo la
	 * constante de año 2014.
	 * 
	 * @param literal mes
	 * @param dia
	 * @return dd-mm-2014
	 */
	private String parseaFecha(String mes, String dia) {
		StringBuilder date = new StringBuilder();
		
		ParseMeses pm = new ParseMeses();
		
		
		date.append(dia).append("-").append(pm.getMonthFromStringAsString(mes)).append("-").append(AnalisisLogsConstantes.YEAR_2014);

		return date.toString();
	}
	
	/**
	 * Metodo que recibe una hora en formato HH:MM:SS y devuelve un entero con
	 * la hora unicamente.
	 * @param hora (HH:MM:SS)
	 * @return hora (HH)
	 */
	private String getSoloHora(String hora) {
		String[] horaDescompuesta = hora.split(":");
		return horaDescompuesta[0];		
	}
	
}
