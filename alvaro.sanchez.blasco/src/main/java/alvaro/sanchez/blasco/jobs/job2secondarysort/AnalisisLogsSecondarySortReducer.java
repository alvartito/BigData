package alvaro.sanchez.blasco.jobs.job2secondarysort;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import alvaro.sanchez.blasco.writables.FechaHoraNumWritableComparable;
import alvaro.sanchez.blasco.writables.ProcesoNumWritable;

/**
 * @author Álvaro Sánchez Blasco
 *
 *         Reducer del Job Secondary Sort del proceso MR para Análisis de Logs.
 * */
public class AnalisisLogsSecondarySortReducer
		extends
		Reducer<FechaHoraNumWritableComparable, ProcesoNumWritable, FechaHoraNumWritableComparable, Text> {
	Text outputValue = new Text();

	private MultipleOutputs<FechaHoraNumWritableComparable, Text> multipleOut;

	@Override
	protected void reduce(FechaHoraNumWritableComparable key,
			Iterable<ProcesoNumWritable> values, Context context)
			throws IOException, InterruptedException {
		StringBuilder sb = new StringBuilder("");
		for (ProcesoNumWritable sn : values) {
			sb = sb.append(sn.toString()).append(",");
		}
		outputValue.set(sb.toString());
		multipleOut.write(key, outputValue, generateFileName(key));
	}

	@Override
	protected void cleanup(Context context) throws IOException,
			InterruptedException {
		multipleOut.close();
	}

	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		multipleOut = new MultipleOutputs<FechaHoraNumWritableComparable, Text>(
				context);
	}

	private String generateFileName(FechaHoraNumWritableComparable k) {
		String dir = k.getFechaHora().toString();
		String[] splits = dir.split("/");
		return splits[0];
	}

}
