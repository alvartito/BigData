package alvaro.sanchez.blasco.jobs.job2secondarysort;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

import alvaro.sanchez.blasco.util.AnalisisLogsConstantes;
import alvaro.sanchez.blasco.writables.FechaHoraNumWritableComparable;
import alvaro.sanchez.blasco.writables.FechaHoraProcesoWritableComparable;
import alvaro.sanchez.blasco.writables.ProcesoNumWritable;

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
		String[] line = value.toString().split("\t");
		String[] fechaHoraProceso = line[0].split(" ");
		fechaHora.set(fechaHoraProceso[0] + "-" + fechaHoraProceso[1]);
		proceso.set(fechaHoraProceso[2]);
		contador.set(Integer.parseInt(line[1]));

		FechaHoraNumWritableComparable myKey = new FechaHoraNumWritableComparable(
				fechaHora, contador);
		
		long valorProceso = context.getCounter(GRUPO_PROCESOS, proceso.toString()).getValue();
		long nuevoValor = contador.get()+valorProceso;
		context.getCounter(GRUPO_PROCESOS, proceso.toString()).setValue(nuevoValor);
		
		ProcesoNumWritable val = new ProcesoNumWritable(proceso, contador);

		context.write(myKey, val);
	}
}
