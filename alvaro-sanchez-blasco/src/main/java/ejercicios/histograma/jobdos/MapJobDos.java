package ejercicios.histograma.jobdos;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import constantes.Constantes;
/** @author Álvaro Sánchez Blasco */
public class MapJobDos extends Mapper<LongWritable, Text, IntWritable, IntWritable> {

	IntWritable outKey = new IntWritable();
	IntWritable outValue = new IntWritable();
	
	protected void map(LongWritable claveInicial, Text line, Context context) throws IOException, InterruptedException{
		
		//Recuperamos el valor con el que vamos a trabajar
		String row[] = line.toString().split("\t");
		Double number = new Double(row[0]);

		// Recuperamos del context el minimo, el maximo que calculamos en el job
		// anterior, y el numero de barras (n) que recibimos como parámetro 
		Double min = new Double(context.getConfiguration().get(Constantes.MIN));
		Double max = new Double(context.getConfiguration().get(Constantes.MAX));
		int n = Integer.parseInt(context.getConfiguration().get(Constantes.N));
		
		//Calculamos la barra a la que pertenece el número que estamos tratando.
		Double bar = Math.floor((number-min)/((max-min)/n));
			
		if(number.equals(max)){
			outKey = new IntWritable(n-1);
			outValue = new IntWritable(1);
		} else {
			outKey = new IntWritable(bar.intValue());
			outValue = new IntWritable(1);
		}
		context.write(outKey, outValue);
	}
		
}
