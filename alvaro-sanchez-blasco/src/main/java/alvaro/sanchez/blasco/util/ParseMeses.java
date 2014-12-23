/**
 * 
 */
package alvaro.sanchez.blasco.util;

import java.util.HashMap;

/**
 * @author Alvaro
 *
 */
public final class ParseMeses {

	private final String sEnero = "01";
	private final String sFebrero = "02";
	private final String sMarzo = "03";
	private final String sAbril = "04";
	private final String sMayo = "05";
	private final String sJunio = "06";
	private final String sJulio = "07";
	private final String sAgosto = "08";
	private final String sSeptiembre = "09";
	private final String sOctubre = "10";
	private final String sNoviembre = "11";
	private final String sDiciembre = "12";

	private final HashMap<String, String> relacionMesesTextoNumero = new HashMap<String, String>();

	public ParseMeses(boolean load) {
		if (load) {
			relacionMesesTextoNumero.put("Jan", sEnero);
			relacionMesesTextoNumero.put("Ene", sEnero);
			relacionMesesTextoNumero.put("Feb", sFebrero);
			relacionMesesTextoNumero.put("Mar", sMarzo);
			relacionMesesTextoNumero.put("Apr", sAbril);
			relacionMesesTextoNumero.put("Abr", sAbril);
			relacionMesesTextoNumero.put("May", sMayo);
			relacionMesesTextoNumero.put("Jun", sJunio);
			relacionMesesTextoNumero.put("Jul", sJulio);
			relacionMesesTextoNumero.put("Aug", sAgosto);
			relacionMesesTextoNumero.put("Ago", sAgosto);
			relacionMesesTextoNumero.put("Sep", sSeptiembre);
			relacionMesesTextoNumero.put("Oct", sOctubre);
			relacionMesesTextoNumero.put("Nov", sNoviembre);
			relacionMesesTextoNumero.put("Dic", sDiciembre);
		}
	}

	public int getMonthFromStringAsInt(String month) {
		int retorno = 0;
		if (relacionMesesTextoNumero.containsKey(month)) {
			retorno = Integer.parseInt(relacionMesesTextoNumero.get(month));
		}
		return retorno;
	}

	public String getMonthFromStringAsString(String month) {
		String retorno = "";
		if (relacionMesesTextoNumero.containsKey(month)) {
			retorno = relacionMesesTextoNumero.get(month);
		}

		return retorno;
	}

	public String getHourFromString(String time) {
		return "";
		

	}

}
