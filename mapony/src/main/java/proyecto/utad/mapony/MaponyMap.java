package proyecto.utad.mapony;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import writables.RawDataWritable;

public class MaponyMap extends Mapper<LongWritable, Text, Text, RawDataWritable> {

	private Text outKey = new Text();
	private RawDataWritable rdw = new RawDataWritable();

	protected void map(LongWritable offset, Text line, Context context) throws IOException, InterruptedException {
		String[] dato = line.toString().split("\t");

		int j = 0;

		// Comenzamos por limpiar las referencias de videos, por lo que el campo [22] del String[] ha de ser '0' para
		// que lo procesemos.
		if ("1".toString().compareTo(dato[22]) != 0) {
			rdw = new RawDataWritable(dato[0], dato[1], dato[2], dato[3], dato[4], dato[5], dato[6], dato[7], dato[8],
					dato[9], dato[10], dato[11], dato[12], dato[13], dato[14], dato[15], dato[16], dato[17], dato[18],
					dato[19], dato[20], dato[21], dato[22]);
		}		

		outKey.set((j++) + "");
		context.write(outKey, rdw);
	};
}
