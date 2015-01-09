package org.utad.analisisLogs.jobSecondarySort;

import org.apache.hadoop.mapreduce.Partitioner;
import org.utad.analisisLogs.util.AnalisisLogsConstantes;

public class IdPartitioner extends Partitioner<FechaHoraNumWritableComparable, ProcesoNumWritable> {

	@Override
	public int getPartition(FechaHoraNumWritableComparable key, ProcesoNumWritable value, int numPartitions) {
		// multiply by 127 to perform some mixing
		if(value.getProceso().toString().contains(AnalisisLogsConstantes.PROCESO_KERNEL)){
			return 0;
		} else {
			return 1;
		}
	}
}
