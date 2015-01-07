package org.utad.analisisLogs.partitioner;

import org.apache.hadoop.mapreduce.Partitioner;
import org.utad.analisisLogs.util.AnalisisLogsConstantes;
import org.utad.analisisLogs.writables.KeyFechaHoraWritableComparable;
import org.utad.analisisLogs.writables.ProcesoContadorWritable;

/**
 * @author Álvaro Sánchez Blasco
 * 
 * */
public class AnalisisLogsPartitioner extends Partitioner<KeyFechaHoraWritableComparable, ProcesoContadorWritable> {

	// El partitioner te permite que las Key del mismo valor vayan al mismo
	// Reducer, es decir, divide y distribuye el espacio de claves.
	
	@Override
	public int getPartition(KeyFechaHoraWritableComparable key, ProcesoContadorWritable value, int numPartitions) {
		// multiply by 127 to perform some mixing
		if(value.getProceso().toString().contains(AnalisisLogsConstantes.PROCESO_KERNEL)){
			return 0;
		} else {
			return 1;
		}
	}
}
