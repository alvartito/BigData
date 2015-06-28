package proyecto.utad.mapony.pruebas.map;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MaponyPruebasMap extends Mapper<Text, Text, Text, Text> {

	protected void map(Text offset, Text line, Context context) throws IOException, InterruptedException {
		context.write(offset, line);
	}
}
