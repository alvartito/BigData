package proyecto.utad.mapony.inferencia.map;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.GeoHashCiudad;
import util.beans.GeoHashBean;
import util.beans.RawDataBean;
import util.constantes.MaponyCte;
import util.writables.CustomWritable;
import ch.hsr.geohash.GeoHash;

public class MaponyInferenciaBz2Map extends Mapper<Text, Text, Text, CustomWritable> {

	private Text outKey;
	private HashMap<String, GeoHashBean> ciudades;
	private final Logger logger = LoggerFactory.getLogger(MaponyInferenciaBz2Map.class);

	protected void map(Text geoHash, Text line, Context context) throws IOException, InterruptedException {

		try {
			if (null == ciudades) {
				ciudades = GeoHashCiudad.getDatos();
			}
		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}

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

			String geoHashCiudad = GeoHash.geoHashStringWithCharacterPrecision(dLatitude, dLongitude, MaponyCte.precisionGeoHashCiudad);
			rdBean.setGeoHash(geoHashCiudad);

			if (ciudades.containsKey(geoHashCiudad)) {
//				GeoHashBean temp = ciudades.get(geoHashCiudad);

//				getLogger().info("Dato Encontrado para " + rdBean.getIdentifier() + ": " + temp.toString());
				
				outKey = new Text(rdBean.getIdentifier());

				CustomWritable cwDescripcion = new CustomWritable("descripcion");
				cwDescripcion.setTexto(rdBean.getDescription());

				CustomWritable cwFoto = new CustomWritable("foto");
				String url = rdBean.getDownloadUrl();
				cwFoto.setTexto(url);

				CustomWritable cwGeo = new CustomWritable("location");
				StringBuilder posicion = new StringBuilder();
				posicion.append(rdBean.getLatitude());
				posicion.append(",");
				posicion.append(rdBean.getLongitude());
				cwGeo.setTexto(posicion.toString());

				CustomWritable cwTags = new CustomWritable("tags");
				cwTags.setTexto(rdBean.getMachineTags() + " " + rdBean.getUserTags());

				CustomWritable cwTitulo = new CustomWritable("titulo");
				cwTitulo.setTexto(rdBean.getTitle());

				context.write(outKey, cwDescripcion);
				context.write(outKey, cwFoto);
				context.write(outKey, cwGeo);
				context.write(outKey, cwTags);
				context.write(outKey, cwTitulo);
			}
		}
	}

	/**
	 * @return the logger
	 */
	private final Logger getLogger() {
		return logger;
	};
}
