package proyecto.utad.mapony.groupNear.map;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import util.beans.RawDataBean;
import util.constantes.MaponyCte;
import ch.hsr.geohash.GeoHash;

public class MaponyGroupNearMap extends Mapper<LongWritable, Text, Text, Text> {

	protected void map(LongWritable offset, Text line, Context context) throws IOException, InterruptedException {
		String[] dato = line.toString().split("\t");

		// Comenzamos por limpiar las referencias de videos, por lo que el campo [22] del String[] ha de ser
		// '0' para que lo procesemos.
		final RawDataBean rdBean = new RawDataBean(dato[0], dato[1], dato[2], dato[3], dato[4], dato[5], dato[6], dato[7], dato[8], dato[9],
				dato[10], dato[11], dato[12], dato[13], dato[14], dato[15], dato[16], dato[17], dato[18], dato[19], dato[20], dato[21], dato[22]);

		// Además, si no tiene informados los campos de longitud y latitud, también descartamos el registro.
		if ("1".toString().compareTo(rdBean.getMarker()) != 0
				&& (MaponyCte.VACIO.compareTo(rdBean.getLatitude()) != 0 && MaponyCte.VACIO.compareTo(rdBean.getLongitude()) != 0)) {

			double dLatitude = new Double(rdBean.getLatitude());
			double dLongitude = new Double(rdBean.getLongitude());

			String geoHash = GeoHash.geoHashStringWithCharacterPrecision(dLatitude, dLongitude, MaponyCte.precisionGeoHashAgrupar);
			rdBean.setGeoHash(geoHash);

			context.write(new Text(rdBean.getGeoHash()), new Text(rdBean.toSequenceFileString()));
		}
	}
}
