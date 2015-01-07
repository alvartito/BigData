package org.utad.analisisLogs.ejemplos.inMapperCombining;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * @see http://maps.google.es/gwt/x/e?wsc=wg&source=wax&u=http://www.google.es/url%3Fq%3Dhttps://chandramanitiwary.wordpress.com/2012/08/19/map-reduce-design-patterns-inmapper-combining/%26sa%3DU%26ei%3DwE6qVOC6FciBU6T3gaAL%26ved%3D0CB4QFjAC%26usg%3DAFQjCNFYBwYvcX8G8K4SPZaW9I6Pa_q7jg&ei=yE6qVKXHBoeo8gOb2oCwCw&whp=1direct_link%3B2https://github.com/Chandramani/MR_Design_Patterns/blob/master/wordcount/WordCountInMapperCombining.java
 */
public class InMapperCombiningMapper extends
		Mapper<LongWritable, Text, Text, IntWritable> {

	private static Map<String, Integer> myWordMap;
	private static final int FLUSH_COUNTER = 1000;
	
	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		if (myWordMap == null) {
			myWordMap = new HashMap<String, Integer>();
		}
	}

	@Override
	public void map(LongWritable key, Text values, Context context)
			throws IOException, InterruptedException {
		String linea = values.toString();
		for (String word : linea.split(" ")) {
			if (myWordMap.containsKey(word)) {
				myWordMap.put(word, myWordMap.get(word) + 1);
			} else {
				myWordMap.put(word, 1);
			}
		}
		if (myWordMap.size() >= FLUSH_COUNTER)
			flush(context);
	}

	private void flush(Context context) throws IOException,
			InterruptedException {
		Iterator<String> iterator = myWordMap.keySet().iterator();
		IntWritable cuenta = new IntWritable();
		Text palabra = new Text();
		while (iterator.hasNext()) {
			palabra.set(iterator.next());
			cuenta.set(myWordMap.get(palabra.toString()));
			context.write(palabra, cuenta);
		}
		myWordMap.clear();
	}

	@Override
	protected void cleanup(Context context) throws IOException,
			InterruptedException {
		flush(context);
	}

	/*
	 * La meta de esta técnica es, como ya he dicho, el reducir el número de
	 * pares key/value que salen del Mapper y se envían al Reducer.
	 * 
	 * La ventaja del in-mapper combining sobre el Combiner tradicional que
	 * vimos en la entrada anterior, es que el primero puede que se ejecute o
	 * puede que no, no tenemos control sobre ello. Mientras que el in-mapper
	 * combining podemos hacer que se ejecute siempre. La segunda ventaja de
	 * este patrón de diseño es controlar cómo se lleva a cabo exactamente.
	 * 
	 * Otra ventaja es que con el Combiner se reduce el número de datos que
	 * llegan al Shuffle and Sort para luego enviarlos al Reducer, pero
	 * realmente no reduce el número de pares key/value emitidos por el Mapper.
	 * 
	 * Al in-mapper combining más bien se le considera como un patrón de diseño
	 * a la hora de desarrollar algoritmos MapReduce en Hadoop, es decir, una
	 * técnica a la hora de programar ampliando el algoritmo implementado en el
	 * Mapper para reducir el número de pares key/value que emite.
	 * 
	 * Esta técnica se puede dividir en dos formas: una local y una global.
	 * 
	 * Volviendo al ejemplo más básico que tenemos, el WordCount, en el que
	 * tenemos una clase Mapper y una Reducer, con esta técnica el Reducer va a
	 * ser exactamente igual, pero en el Mapper vamos a introducir directamente
	 * la funcionalidad del Combiner, más bien en lo que se basa. Y esto lo hace
	 * utilizando un objeto de tipo Map. Este se refiere al algoritmo local, ya
	 * que es local respecto al método. Con esta técnica hemos reducido desde el
	 * principio el número de pares key/value que emite el Mapper y que llegan
	 * al Shuffle and Sort para posteriormente ser enviados al Reducer.
	 * 
	 * public class WordCountMapper extends Mapper <LongWritable, Text, Text,
	 * IntWritable>{ private IntWritable cuenta = new IntWritable(); private
	 * Text palabra = new Text(); @Override public void map(LongWritable key,
	 * Text values, Context context) throws IOException, InterruptedException{
	 * String linea = values.toString(); Map<String, Integer> myWordMap = new
	 * HashMap<String, Integer>(); for(String word : linea.split(" ")){ int sum
	 * = 1; if(myWordMap.containsKey(word)){ sum += myWordMap.get(word); }
	 * myWordMap.put(word, sum); } Iterator<String> iterator =
	 * myWordMap.keySet().iterator(); while (iterator.hasNext()){
	 * palabra.set(iterator.next());
	 * cuenta.set(myWordMap.get(palabra.toString())); context.write(palabra,
	 * cuenta); } } }
	 * 
	 * Este método todavía conlleva a que el Mapper cree y destruya múltiples
	 * objetos o realice otras tareas que ocupan memoria de una forma
	 * considerable, y es por ello que se debe optimizar llegando a la forma
	 * global, de tal forma que el Mapper sólo emitirá los pares key/value
	 * necesarios al Shuffle and Sort y al Reducer.
	 * 
	 * Esa optimización se hace utilizando los métodos setup() y cleanup(), que
	 * como ya comenté en la entrada
	 * 
	 * sobre estos métodos, se van a ejecutar sólo una vez, el primero antes de
	 * que todas las tareas map comiencen y el segundo justo cuando todas las
	 * tareas map finalizan y antes de que el Mapper sea destruído.
	 * 
	 * public class WordCountMapper extends Mapper <LongWritable, Text, Text,
	 * IntWritable> { Map<String, Integer> myWordMap; @Override protected void
	 * setup(Context context) throws IOException, InterruptedException {
	 * if(myWordMap == null ) myWordMap = new HashMap<String, Integer>(); }
	 * 
	 * @Override public void map(LongWritable key, Text values, Context context)
	 * throws IOException, InterruptedException{ String linea =
	 * values.toString(); for(String word : linea.split(" ")){ int sum = 1;
	 * if(myWordMap.containsKey(word)){ sum += myWordMap.get(word); }
	 * myWordMap.put(word, sum); } } @Override protected void cleanup(Context
	 * context) throws IOException, InterruptedException { Iterator<String>
	 * iterator = myWordMap.keySet().iterator(); IntWritable cuenta = new
	 * IntWritable(); Text palabra = new Text(); while (iterator.hasNext()){
	 * palabra.set(iterator.next());
	 * cuenta.set(myWordMap.get(palabra.toString())); context.write(palabra,
	 * cuenta); } } }
	 * 
	 * Desarrollar la clase Mapper de esta forma mejora la opción anterior pero
	 * sigue teniendo problemas de memoria.
	 * 
	 * Recordemos que cuando usamos el Combiner, primero el Mapper va a tratar
	 * los datos y va a emitir los pares key/value, los va a escribir en el
	 * disco local y después va a ser el Combiner quien va a actuar.
	 * 
	 * Si implementamos el algoritmo anterior nos puede surgir un problema con
	 * la memoria, ya que si estamos hablando de TB de datos, o no hay
	 * coincidencia en las palabras y se genera un registro para cada palabra,
	 * estos se van a cargar en el HashMap en memoria y no se van a liberar
	 * hasta que todas las tareas map han finalizado y el objeto HashMap se
	 * destruya.
	 * 
	 * La solución para limitar la memoria es "bloquear" la entrada de pares
	 * key/value y liberar la estructura HashMap cada cierto tiempo.
	 * 
	 * Así que la idea es en vez de emitir los datos intermedios una vez que
	 * todos los pares key/value han sido tratados, emitir los datos intermedios
	 * después de haber tratado N pares key/value.
	 * 
	 * Esto tendría una fácil solución, que es poner un contador FLUSH_COUNTER,
	 * y cuando el número de pares key/value tratados llegue a ese contador,
	 * emitir los datos y vaciar el HashMap.
	 * 
	 * public class WordCountMapper extends Mapper <LongWritable, Text, Text,
	 * IntWritable> { private static Map<String, Integer> myWordMap; private
	 * static final int FLUSH_COUNTER = 1000; @Override protected void
	 * setup(Context context) throws IOException, InterruptedException {
	 * if(myWordMap == null ) myWordMap = new HashMap<String, Integer>(); }
	 * 
	 * @Override public void map(LongWritable key, Text values, Context context)
	 * throws IOException, InterruptedException{ String linea =
	 * values.toString(); for(String word : linea.split(" ")){
	 * if(myWordMap.containsKey(word)){ myWordMap.put(word,
	 * myWordMap.get(word)+1); }else{ myWordMap.put(word, 1); } }
	 * if(myWordMap.size() >= FLUSH_COUNTER) flush(context); } private void
	 * flush(Context context) throws IOException, InterruptedException{
	 * Iterator<String> iterator = myWordMap.keySet().iterator(); IntWritable
	 * cuenta = new IntWritable(); Text palabra = new Text(); while
	 * (iterator.hasNext()){ palabra.set(iterator.next());
	 * cuenta.set(myWordMap.get(palabra.toString())); context.write(palabra,
	 * cuenta); } myWordMap.clear(); } @Override protected void cleanup(Context
	 * context) throws IOException, InterruptedException { flush(context); } }
	 * 
	 * Estamos en un punto donde podemos seguir haciéndonos preguntas una tras
	 * otra para optimizar este algoritmo y aplicar este patrón de diseño
	 * correctamente, y la siguiente cuestión sería ¿cómo sabemos cuál es el
	 * número FLUSH_COUNT de pares que hay que tratar antes de que la memoria
	 * falle?.
	 * 
	 * El libro de referencia propone que más que crear un contador de pares
	 * key/value, poner un límite a la memoria que se utiliza y en el momento
	 * que el HashMap ha llegado a ese límite se liberaría. El problema es que
	 * en Java no es fácil determinar de forma rápida el tamaño de la memoria
	 * ocupada por un objeto, por eso, es mejor elegir un FLUSH_COUNT lo más
	 * alto posible y teniendo cuidado que el HashMap no provoque un Out Of
	 * Memory.
	 */
}
