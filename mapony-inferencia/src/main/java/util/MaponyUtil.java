package util;

import org.apache.hadoop.io.Text;

import ch.hsr.geohash.GeoHash;
import util.constantes.MaponyCte;

public class MaponyUtil {

	/**
	 * Método que compara el String que llega como parámetro con null y '' para verificar si tiene valor o no.
	 * 
	 * @param cadena
	 * @return true en caso de que no sea nulo ni vacío.
	 */
	public static boolean tieneValor(String cadena) {
		boolean tieneValor = false;
		if (null != cadena && !MaponyCte.VACIO.equals(cadena)) {
			tieneValor = true;
		}
		return tieneValor;
	}

	/**
	 * The first step removes all characters that are not a letter or a space and replaces them with a space.
	 * The second step removes multiple spaces by only one space.
	 * 
	 * @param cadena
	 * @return la cadena limpia de caracteres extraños
	 */
	public static final String cleanString(String cadena) {
		return cadena.replaceAll("[\\d[^\\w\\s]]+", " ").replaceAll("(\\s{2,})", " ");
	}
	
	public static final Text getGeoHashPorPrecision(Text longitude, Text latitude, int precision) {
		double dLatitude = new Double(latitude.toString());
		double dLongitude = new Double(longitude.toString());
		return new Text(GeoHash.geoHashStringWithCharacterPrecision(dLatitude, dLongitude, precision));
	}
}
