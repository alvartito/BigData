package proyecto.utad.mapony.inferencia.map;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.GeoHashCiudad;
import util.beans.GeoHashBean;
import util.beans.InferenciaBean;
import util.beans.RawDataWritable;
import util.writables.CustomWritable;

public class MaponyInferenciaMap extends Mapper<Text, RawDataWritable, Text, CustomWritable> {

	private Text outKey;
	private HashMap<String, GeoHashBean> ciudades;
	private final Logger logger = LoggerFactory.getLogger(MaponyInferenciaMap.class);

	protected void map(Text geoHash, RawDataWritable rawDataWritable, Context context) throws IOException, InterruptedException {

		try {
			if (null == ciudades) {
				ciudades = GeoHashCiudad.getDatos();
			}
		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}

		try {
			InferenciaBean rdBean = new InferenciaBean(rawDataWritable);
			Text ciudad = new Text();
			Text pais = new Text();
			Text continente = new Text();
			if (ciudades.containsKey(rdBean.getGeoHashCiudad())) {
				GeoHashBean temp = ciudades.get(rdBean.getGeoHashCiudad());
				ciudad = new Text(temp.getName());
				pais = new Text(temp.getPais());
				continente = new Text(temp.getContinente());
			}

			rdBean.setCiudad(ciudad);
			rdBean.setPais(pais);
			rdBean.setContinente(continente);
			
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
		} catch (Exception e) {
			getLogger().error(e.getMessage());
		}
	}

	/**
	 * @return the logger
	 */
	private final Logger getLogger() {
		return logger;
	};
}
