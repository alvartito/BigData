package alvaro.sanchez.blasco.jobs.job2secondarysort;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import alvaro.sanchez.blasco.writables.FechaHoraNumWritableComparable;
import alvaro.sanchez.blasco.writables.ProcesoNumWritable;

public class AnalisisLogsSecondarySortMapper extends
		Mapper<LongWritable, Text, FechaHoraNumWritableComparable, ProcesoNumWritable> {

	private Text fecha = new Text();
	private Text hora = new Text();
	private Text fechaHora = new Text();
	private Text proceso = new Text();
	private IntWritable contador = new IntWritable();

	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		// input line is :
		// 248,"All my loving",13
		String[] line = value.toString().split("\t");

		String[] fechaHoraProceso = line[0].split(" ");

		
//		fecha.set(fechaHoraProceso[0]);
//		hora.set(fechaHoraProceso[1]);
		fechaHora.set(fechaHoraProceso[0]+"-"+fechaHoraProceso[1]);
		proceso.set(fechaHoraProceso[2]);
		contador.set(Integer.parseInt(line[1]));

		FechaHoraNumWritableComparable myKey = new FechaHoraNumWritableComparable(fechaHora, contador);
		ProcesoNumWritable val = new ProcesoNumWritable(proceso, contador);
		
		context.write(myKey, val);
	}
}
