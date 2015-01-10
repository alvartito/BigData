package alvaro.sanchez.blasco.partitioner;

import org.apache.hadoop.mapreduce.Partitioner;

import alvaro.sanchez.blasco.util.AnalisisLogsConstantes;
import alvaro.sanchez.blasco.writables.FechaHoraNumWritableComparable;
import alvaro.sanchez.blasco.writables.ProcesoNumWritable;

public class AnalisisLogsPartitioner extends Partitioner<FechaHoraNumWritableComparable, ProcesoNumWritable> {

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
