package proyecto.utad.mapony.inferencia.map;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.GeoHashCiudad;
import util.MaponyUtil;
import util.beans.GeoHashBean;
import util.constantes.MaponyCte;
import util.writables.CustomWritable;
import util.writables.RawDataWritable;
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
		// Además, si no tiene informados los campos de longitud y latitud, también descartamos el registro.
		if ("1".toString().compareTo(dato[22]) != 0
				&& (MaponyCte.VACIO.compareTo(dato[10]) != 0 && MaponyCte.VACIO.compareTo(dato[11]) != 0)) {

			RawDataWritable rdBean;
			try {
				rdBean = new RawDataWritable(new Text(dato[0]), new Text(dato[3]), new Text(dato[5]), new Text(dato[6]), new Text(dato[7]), new Text(dato[8]), new Text(dato[9]),
						new Text(dato[10]), new Text(dato[11]), new Text(dato[14]));
				rdBean.setGeoHash(MaponyUtil.getGeoHashPorPrecision(rdBean.getLongitude(), rdBean.getLatitude(), MaponyCte.precisionGeoHashCiudad));
				if (ciudades.containsKey(rdBean.getGeoHash().toString())) {
//					GeoHashBean temp = ciudades.get(geoHashCiudad);

//					getLogger().info("Dato Encontrado para " + rdBean.getIdentifier() + ": " + temp.toString());
					
					outKey = new Text(rdBean.getIdentifier());

					CustomWritable cwDescripcion = new CustomWritable("descripcion");
					cwDescripcion.setTexto(rdBean.getDescription().toString());

					CustomWritable cwFoto = new CustomWritable("foto");
					String url = rdBean.getDownloadUrl().toString();
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
					cwTitulo.setTexto(rdBean.getTitle().toString());

					context.write(outKey, cwDescripcion);
					context.write(outKey, cwFoto);
					context.write(outKey, cwGeo);
					context.write(outKey, cwTags);
					context.write(outKey, cwTitulo);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
