package org.utad.analisisLogs;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.utad.analisisLogs.writables.KeyFechaHoraWritableComparable;
import org.utad.analisisLogs.writables.ProcesoContadorWritable;

public class AnalisisLogsReducer extends
		Reducer<KeyFechaHoraWritableComparable, ProcesoContadorWritable, KeyFechaHoraWritableComparable, ProcesoContadorWritable> {
	int sum = 0;
	ProcesoContadorWritable procesoContadorWritable = new ProcesoContadorWritable();
	@Override
	protected void reduce(KeyFechaHoraWritableComparable key, Iterable<ProcesoContadorWritable> values, Context context)
			throws IOException, InterruptedException {
		
		for (ProcesoContadorWritable data : values) {
			sum += data.getContador().get();
			data.setContador(new IntWritable(sum));
			procesoContadorWritable.setProceso(data.getProceso());
			procesoContadorWritable.setContador(data.getContador());
		}
		
		context.write(key, procesoContadorWritable);
	}
}
