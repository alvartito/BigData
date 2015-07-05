package proyecto.utad.mapony.pruebas.map;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import util.beans.GeoHashBean;
import util.constantes.MaponyCte;

public class MaponyPruebasMap extends Mapper<LongWritable, Text, Text, Text> {

	protected void map(LongWritable offset, Text line, Context context) throws IOException, InterruptedException {
		String[] datoPais = line.toString().split("\t");
		if (datoPais.length == 19 && null != datoPais[17] && datoPais[17].compareTo(MaponyCte.VACIO) != 0) {
			GeoHashBean ghb = new GeoHashBean(datoPais);

			context.write(new Text(ghb.getGeoHash()), new Text(ghb.toString()));
		}
	}
}
