package alvaro.sanchez.blasco.util.analisis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

public class Contador {
	private static String PROC_NO_CONTEMPLABLE = "vmnet";
	private static String LLAVE = "[";
	private static String DOS_PUNTOS = ":";

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		if (args != null && args.length > 0) {
			for (String fichero : args) {
				HashMap<String, Integer> contadores = new HashMap<String, Integer>();
				BufferedReader bf = new BufferedReader(new FileReader(fichero));
				if (null != fichero) {
					System.out.println(fichero + "\n");
					String sCadena = "";
					while ((sCadena = bf.readLine()) != null) {
						String[] splits = sCadena.split(" ");
						String fecha = splits[0] + splits[1];
						String proceso = splits[4];
						if (!proceso.contains(PROC_NO_CONTEMPLABLE)) {
							if (proceso.contains(LLAVE)) {
								StringTokenizer st = new StringTokenizer(
										proceso, LLAVE);
								proceso = st.nextToken();
							}
							if (proceso.contains(DOS_PUNTOS)) {
								StringTokenizer st = new StringTokenizer(
										proceso, DOS_PUNTOS);
								proceso = st.nextToken();
							}
							if (contadores.containsKey(proceso)) {
								int valor = contadores.get(proceso);
								int nuevoValor = new Integer(valor++);
								valor = valor++;
								contadores.put(proceso, valor);
							} else {
								contadores.put(proceso, 1);
							}
						}
					}
				}
				bf.close();
				Set<String> claves = contadores.keySet();
				for (String clave : claves) {
					System.out.println(clave + " - " + contadores.get(clave));
				}
				System.out.println("\nFin proceso fichero " + fichero);
			}
		}
	}

}
