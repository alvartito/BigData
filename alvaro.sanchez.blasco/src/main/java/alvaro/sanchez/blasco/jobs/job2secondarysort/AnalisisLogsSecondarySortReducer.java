package alvaro.sanchez.blasco.jobs.job2secondarysort;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import alvaro.sanchez.blasco.writables.FechaHoraNumWritableComparable;
import alvaro.sanchez.blasco.writables.ProcesoNumWritable;

public class AnalisisLogsSecondarySortReducer extends
		Reducer<FechaHoraNumWritableComparable, ProcesoNumWritable, Text, Text> {
	Text outputValue = new Text();

	@Override
	protected void reduce(FechaHoraNumWritableComparable key, Iterable<ProcesoNumWritable> values, Context context)
			throws IOException, InterruptedException {
		StringBuilder sb = new StringBuilder("");
		for (ProcesoNumWritable sn: values){
			sb = sb.append(sn.toString()).append(",");
		}
		outputValue.set(sb.toString());
		context.write(new Text(key.toString()), outputValue);
	}

}
