package proyecto.utad.mapony.groupNear.map;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.MaponyUtil;
import util.beans.RawDataWritable;
import util.constantes.MaponyCte;

public class MaponyGroupNearMap extends Mapper<LongWritable, Text, Text, RawDataWritable> {

	private static final Logger logger = LoggerFactory.getLogger(MaponyGroupNearMap.class);

	protected void map(LongWritable offset, Text line, Context context) throws IOException, InterruptedException {

		String[] dato = line.toString().split(MaponyCte.TAB);

		// Comenzamos por limpiar las referencias de videos, por lo que el campo [22] del String[] ha de ser
		// '0' para que lo procesemos.
		// Además, si no tiene informados los campos de longitud y latitud, también descartamos el registro.
		if ("1".toString().compareTo(dato[22]) != 0 && (MaponyCte.VACIO.compareTo(dato[10]) != 0 && MaponyCte.VACIO.compareTo(dato[11]) != 0)) {
			try {
				final RawDataWritable rdBean = new RawDataWritable(new Text(dato[0]), new Text(dato[3]), new Text(MaponyUtil.cleanString(dato[5])),
						new Text(MaponyUtil.cleanString(dato[6])), new Text(MaponyUtil.cleanString(dato[7])), new Text(
								MaponyUtil.cleanString(dato[8])), new Text(MaponyUtil.cleanString(dato[9])), new Text(dato[10]), new Text(dato[11]),
						new Text(dato[14]));

				context.write(rdBean.getGeoHash(), rdBean);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}
}
