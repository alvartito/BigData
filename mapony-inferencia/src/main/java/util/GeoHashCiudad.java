package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import util.beans.GeoHashBean;

public class GeoHashCiudad {

	private static HashMap<String, GeoHashBean> datos;
	
	private String archivo;

	public GeoHashCiudad(final String archivo) {
		setArchivo(archivo);
		cargaGeoHashCiudades();
	}

	public HashMap<String, GeoHashBean> cargaGeoHashCiudades() {
		// TODO Leer el fichero de ciudades, y por cada registro, cargar en mi HashMap los datos
		// correspondientes.
		datos = new HashMap<String, GeoHashBean>();

		String cadena;
		FileReader f;
		try {
			f = new FileReader(getArchivo());
			BufferedReader b = new BufferedReader(f);
			while ((cadena = b.readLine()) != null) {
				GeoHashBean bean = new GeoHashBean(cadena.split("\t"));
				String geoHaString = bean.getGeoHash();
				datos.put(geoHaString, bean);
			}
			b.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return datos;
	}

	/**
	 * @return the archivo
	 */
	private final String getArchivo() {
		return archivo;
	}

	/**
	 * @param archivo
	 *            the archivo to set
	 */
	private final void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	/**
	 * @return the datos
	 * @throws Exception 
	 */
	public static final HashMap<String, GeoHashBean> getDatos() throws Exception {
		if(null == datos){
			throw new Exception("Datos de ciudades no cargados");
		}
		return datos;
	}

}
