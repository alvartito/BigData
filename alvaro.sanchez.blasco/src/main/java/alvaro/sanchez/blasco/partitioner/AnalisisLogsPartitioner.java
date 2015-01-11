package alvaro.sanchez.blasco.partitioner;

import org.apache.hadoop.mapreduce.Partitioner;

import alvaro.sanchez.blasco.util.AnalisisLogsConstantes;
import alvaro.sanchez.blasco.writables.FechaHoraNumWritableComparable;
import alvaro.sanchez.blasco.writables.ProcesoNumWritable;

/**
 * @author Álvaro Sánchez Blasco
 * 
 *         Clase Partitioner para el Job MR de Analisis de Logs. Dependiendo del
 *         proceso que se esté procesando, se utilizará un reducer u otro.
 * */
public class AnalisisLogsPartitioner extends
		Partitioner<FechaHoraNumWritableComparable, ProcesoNumWritable> {

	@Override
	public int getPartition(FechaHoraNumWritableComparable key,
			ProcesoNumWritable value, int numPartitions) {
		if (value.getProceso().toString()
				.contains(AnalisisLogsConstantes.PROCESO_KERNEL)) {
			return 0;
		} else {
			return 1;
		}
	}
}
