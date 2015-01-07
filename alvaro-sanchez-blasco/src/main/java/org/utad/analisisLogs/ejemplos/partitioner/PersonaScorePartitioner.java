package org.utad.analisisLogs.ejemplos.partitioner;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class PersonaScorePartitioner extends Partitioner<Text, Text> {

	/*
	 * 	El partitioner te permite que las Key del mismo valor vayan al mismo Reducer, es decir, divide y distribuye el espacio de claves.
	 * 	Hadoop tiene un Partitioner por defecto, el HashPartitioner, que a trav�s de su m�todo hashCode() determina a qu� partici�n pertenece una determinada Key y por tanto a qu� Reducer va a ser enviado el registro. El n�mero de particiones es igual al n�mero de tareas reduce del job.
	 * 	A veces, por ciertas razones necesitamos implementar nuestro propio Partitioner para controlar que una serie de Keys vayan al mismo Reducer, es por esta raz�n que crear�amos nuestra propia clase MyPartitioner que heredar�a de la interfaz Partitioner y que implementar� el m�todo getPartition.
	 * 	public class MyPartitioner<K2, V2> extends Partitioner<KEY, VALUE> implements Configurable { public int getPartition(KEY key, VALUE value, int numPartitions){} }
	 * 	getPartition recibe una key, un valor y el n�mero de particiones en el que se deben dividir los datos cuyo rango est� entre 0 y "numPartitions -1" y devolver� un n�mero entre 0 y numPartitions indicando a qu� partici�n pertenecen esos datos recibidos.
	 * 	Para configurar un partitioner, basta con a�adir en el Driver la l�nea:
	 * 	job.setPartitionerClass(MyPartitioner.class);
	 * 	Tambi�n nos tenemos que acordar de configurar el n�mero de Reducer al n�mero de particiones que vamos a realizar:
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
