package constantes;


/** @author Álvaro Sánchez Blasco */
public final class Constantes {

	public static final String HELP_MAPONY = "Invalid number of arguments\n\n";
	public static final String HELP_HISTOGRAMA = "Invalid number of arguments\n\n" +
			"Usage: histograma <input_path> <output_path> <Nº de barras>\n\n";
	
	public static final String HELP_AMIGOS = "Invalid number of arguments\n\n" +
			"Usage: amigos-de-amigos <input_path> <output_path>\n\n";

	
	public static final String MIN = "min";
	
	public static final String MAX = "max";
	
	public static final String N = "N";
	
	public static final String FIN = "Procesado de datos finalizado";
	
	public static final String CTE_PATH_INICIO = "pIni";
	public static final String CTE_PATH_TEMPORAL = "pTemp";
	public static final String CTE_PATH_FINAL = "pFin";
	
	public static final String PATH_TEMPORAL = "tmpJobs";
	
	public static final int precisionGeoHashCiudad = 2;
	/**
	 *
	 * Geohash Level Dimensions 
	 * <p>g 1 ~ 5,004km x 5,004km 
	 * <p>gc 2 ~ 1,251km x 625km 
	 * <p>gcp 3 ~ 156km x 156km 
	 * <p>gcpu 4 ~39km x 19.5km 
	 * <p>gcpuu 5 ~ 4.9km x 4.9km 
	 * <p>gcpuuz 6 ~ 1.2km x 0.61km 
	 * <p>gcpuuz9 7 ~ 152.8m x 152.8m 
	 * <p>gcpuuz94 8 ~ 38.2m x 19.1m 
	 * <p>gcpuuz94k 9 ~ 4.78m x 4.78m 
	 * <p>gcpuuz94kk 10 ~ 1.19m x 0.60m 
	 * <p>gcpuuz94kkp 11 ~ 14.9cm x 14.9cm 
	 * <p>gcpuuz94kkp5 12 ~ 3.7cm x 1.8cm
	 *
	 */
	public static final int precisionGeoHashAgrupar = 6;
	
	public static final String MSG_PROPIEDADES_CARGADAS = "\nPropiedades cargadas\n";
	
	public static final String MSG_FIN_JOB = "\nJob +"+Constantes.jobName +" finalizado con éxito\n";
	
	public static final String propiedades = "job.properties";
	
	public static final String datos = "ruta_ficheros";
	public static final String paises = "ruta_paises";
	
	public static final String indice = "index_name";
	public static final String tipo = "type_name";
	public static final String cluster = "clusterName";
	
	public static final String ip = "ip";
	public static final String puerto = "port";
	
	public static final String reducers = "numero_reducer";
	
	public static final String jobName = "Flickrjob";
	
}
