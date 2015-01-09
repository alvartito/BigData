package org.utad.analisisLogs.util;

public class UtilAnalisisLogs {

	/**
	 * Metodo que, recibiendo un literal del mes, y un dia, monta la fecha con
	 * el formato indicado, parseando el literal a numero y añadiendo la
	 * constante de año 2014.
	 * 
	 * @param literal mes
	 * @param dia
	 * @return dd-mm-2014
	 */
	public static String parseaFecha(String mes, String dia) {
		StringBuilder date = new StringBuilder();
		
		ParseMeses pm = new ParseMeses();
		
		
		date.append(dia).append("-").append(pm.getMonthFromStringAsString(mes)).append("-").append(AnalisisLogsConstantes.YEAR_2014);

		return date.toString();
	}
	
	/**
	 * Metodo que recibe una hora en formato HH:MM:SS y devuelve un entero con
	 * la hora unicamente.
	 * @param hora (HH:MM:SS)
	 * @return hora (HH)
	 */
	public static String getSoloHora(String hora) {
		String[] horaDescompuesta = hora.split(":");
		return horaDescompuesta[0];		
	}
	
}
