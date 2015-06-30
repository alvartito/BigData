package util;

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

}
