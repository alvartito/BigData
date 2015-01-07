package org.utad.analisisLogs.ejemplos.partitioner;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class PersonaScorePartitioner extends Partitioner<Text, Text> {

	/*
	 * 	El partitioner te permite que las Key del mismo valor vayan al mismo Reducer, es decir, divide y distribuye el espacio de claves.
	 * 	Hadoop tiene un Partitioner por defecto, el HashPartitioner, que a través de su método hashCode() determina a qué partición pertenece una determinada Key y por tanto a qué Reducer va a ser enviado el registro. El número de particiones es igual al número de tareas reduce del job.
	 * 	A veces, por ciertas razones necesitamos implementar nuestro propio Partitioner para controlar que una serie de Keys vayan al mismo Reducer, es por esta razón que crearíamos nuestra propia clase MyPartitioner que heredaría de la interfaz Partitioner y que implementará el método getPartition.
	 * 	public class MyPartitioner<K2, V2> extends Partitioner<KEY, VALUE> implements Configurable { public int getPartition(KEY key, VALUE value, int numPartitions){} }
	 * 	getPartition recibe una key, un valor y el número de particiones en el que se deben dividir los datos cuyo rango está entre 0 y "numPartitions -1" y devolverá un número entre 0 y numPartitions indicando a qué partición pertenecen esos datos recibidos.
	 * 	Para configurar un partitioner, basta con añadir en el Driver la línea:
	 * 	job.setPartitionerClass(MyPartitioner.class);
	 * 	También nos tenemos que acordar de configurar el número de Reducer al número de particiones que vamos a realizar:
	 * 	job.setNumReduceTasks(numPartitions);
	 *  
	 * */

	@Override
	public int getPartition(Text key, Text value, int numPartitions) {
		if (key.toString().endsWith("2012")) {
			return 0;
		} else {
			return 1;
		}
	}
}
