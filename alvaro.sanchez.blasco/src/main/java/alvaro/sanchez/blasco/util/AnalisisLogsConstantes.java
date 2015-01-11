package alvaro.sanchez.blasco.util;

/**
 * 
 * @author Álvaro Sánchez Blasco
 * 
 *         Clase de constantes para el Job MR de Análisis de logs.
 * 
 * */
public final class AnalisisLogsConstantes {

	public static final String HELP_ANALISIS_LOGS = "Invalid number of arguments\n\n"
			+ "Usage: AnalisisLogs <input_path> <output_path>\n\n";

	public static final String CONTADORES = "COUNT_PROC";
	public static final String GRUPO_PROCESOS = "procesos";
	public static final String GRUPO_PROCESOS_VMNET = "proc_vmnet";

	public static final String FIN = "Procesado de datos finalizado";

	public static final String CTE_PATH_TEMPORAL = "out_wc";

	public static final String PROCESO_KERNEL = "kernel";
	public static final String PROCESOS_DESCARTADOS = "vmnet";

	public static final int FLUSH_COUNTER = 1000;
	public static final String YEAR_2014 = "2014";

}
