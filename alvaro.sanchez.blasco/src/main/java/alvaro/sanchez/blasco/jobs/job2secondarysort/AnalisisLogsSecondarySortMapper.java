package alvaro.sanchez.blasco.jobs.job2secondarysort;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import alvaro.sanchez.blasco.util.AnalisisLogsConstantes;
import alvaro.sanchez.blasco.writables.FechaHoraNumWritableComparable;
import alvaro.sanchez.blasco.writables.ProcesoNumWritable;

/**
 * @author Álvaro Sánchez Blasco
 *
 *         Mapper del Job Secondary Sort del proceso MR para Análisis de Logs.
 * */
public class AnalisisLogsSecondarySortMapper
		extends
		Mapper<LongWritable, Text, FechaHoraNumWritableComparable, ProcesoNumWritable> {

	private Text fechaHora = new Text();
	private Text proceso = new Text();
	private IntWritable contador = new IntWritable();

	private String GRUPO_PROCESOS;

	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		Configuration conf = context.getConfiguration();
		GRUPO_PROCESOS = conf.getStrings(AnalisisLogsConstantes.GRUPO_PROCESOS)[0];
	}

	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		// Procesamos las lineas de entrada
		String[] line = value.toString().split("\t");

		String[] fechaHoraProceso = line[0].split(" ");
		fechaHora.set(fechaHoraProceso[0] + "-" + fechaHoraProceso[1]);
		proceso.set(fechaHoraProceso[2]);
		contador.set(Integer.parseInt(line[1]));

		// Creamos la key de nuestro Map
		FechaHoraNumWritableComparable myKey = new FechaHoraNumWritableComparable(
				fechaHora, contador);

		// Creamos el value de nuestro Map
		ProcesoNumWritable val = new ProcesoNumWritable(proceso, contador);

		// Para el control de lineas de log asociadas a cada proceso.
		long valorProceso = context.getCounter(GRUPO_PROCESOS,
				proceso.toString()).getValue();
		
		// Sumamos, por cada proceso, el numero de veces que ha aparecido.
		long nuevoValor = contador.get() + valorProceso;
		
		// Actualizamos el grupo de contadores de procesos.
		context.getCounter(GRUPO_PROCESOS, proceso.toString()).setValue(
				nuevoValor);

		context.write(myKey, val);
	}
}
