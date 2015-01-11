package alvaro.sanchez.blasco.jobs.job1wordcount;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import alvaro.sanchez.blasco.util.AnalisisLogsConstantes;
import alvaro.sanchez.blasco.util.AnalisisLogsUtil;
import alvaro.sanchez.blasco.writables.FechaHoraProcesoWritableComparable;

/**
 * @see http://maps.google.es/gwt/x/e?wsc=wg&source=wax&u=http://www.google.es/url%3Fq%3Dhttps://chandramanitiwary.wordpress.com/2012/08/19/map-reduce-design-patterns-inmapper-combining/%26sa%3DU%26ei%3DwE6qVOC6FciBU6T3gaAL%26ved%3D0CB4QFjAC%26usg%3DAFQjCNFYBwYvcX8G8K4SPZaW9I6Pa_q7jg&ei=yE6qVKXHBoeo8gOb2oCwCw&whp=1direct_link%3B2https://github.com/Chandramani/MR_Design_Patterns/blob/master/wordcount/WordCountInMapperCombining.java
 */
public class AnalisisLogsWordCountMapper extends
		Mapper<LongWritable, Text, FechaHoraProcesoWritableComparable, IntWritable> {

	private static Map<FechaHoraProcesoWritableComparable, Integer> myWordMap;
	FechaHoraProcesoWritableComparable keyOut = new FechaHoraProcesoWritableComparable();
	
	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		if (myWordMap == null) {
			myWordMap = new HashMap<FechaHoraProcesoWritableComparable, Integer>();
		}
	}

	@Override
	public void map(LongWritable key, Text values, Context context)
			throws IOException, InterruptedException {
		
		/*
		 * Se recibe como valor una línea del fichero de entrada
		 */
		String[] word = values.toString().split(" ");

		String proc = word[4];

		// Primero, descartar todos los procesos que contengan vmet
		if (!proc.contains(AnalisisLogsConstantes.PROCESOS_DESCARTADOS)) {
			// Nos quedamos unicamente con el nombre del proceso.
			if (proc.contains("[")) {
				StringTokenizer st = new StringTokenizer(proc, "[");
				proc = st.nextToken();
				if(proc.contains("]")){
					proc = proc.replace("]", "");
				}
			}

			if (proc.contains(":")) {
				StringTokenizer st = new StringTokenizer(proc, ":");
				proc = st.nextToken();
			}

			// Ya tengo el proceso. Tengo que montar la fecha.
			String date = AnalisisLogsUtil.parseaFecha(word[0], word[1]);
			keyOut.setFecha(new Text(date));

			// Hay que recuperar la hora.
			String sHora = AnalisisLogsUtil.getSoloHora(word[2]);
			keyOut.setHora(new Text(sHora));

			// Ya tengo los datos necesarios para montar la clave.
			keyOut.setProceso(new Text(proc));

			context.getCounter(AnalisisLogsConstantes.GRUPO_PROCESOS, proc).increment(1);
			
			if (myWordMap.containsKey(keyOut)) {
				myWordMap.put(keyOut, myWordMap.get(keyOut) + 1);
			} else {
				myWordMap.put(keyOut, 1);
			}
		}
		flush(context);
	}

	private void flush(Context context) throws IOException,
			InterruptedException {
		Iterator<FechaHoraProcesoWritableComparable> iterator = myWordMap.keySet().iterator();
		while (iterator.hasNext()) {
			FechaHoraProcesoWritableComparable fhpwc = iterator.next();
			context.write(fhpwc, new IntWritable(myWordMap.get(fhpwc)));
		}
		myWordMap.clear();
	}

	@Override
	protected void cleanup(Context context) throws IOException,
			InterruptedException {
		flush(context);
	}

	/*
	 * La meta de esta t�cnica es, como ya he dicho, el reducir el n�mero de
	 * pares key/value que salen del Mapper y se env�an al Reducer.
	 * 
	 * La ventaja del in-mapper combining sobre el Combiner tradicional que
	 * vimos en la entrada anterior, es que el primero puede que se ejecute o
	 * puede que no, no tenemos control sobre ello. Mientras que el in-mapper
	 * combining podemos hacer que se ejecute siempre. La segunda ventaja de
	 * este patr�n de dise�o es controlar c�mo se lleva a cabo exactamente.
	 * 
	 * Otra ventaja es que con el Combiner se reduce el n�mero de datos que
	 * llegan al Shuffle and Sort para luego enviarlos al Reducer, pero
	 * realmente no reduce el n�mero de pares key/value emitidos por el Mapper.
	 * 
	 * Al in-mapper combining m�s bien se le considera como un patr�n de dise�o
	 * a la hora de desarrollar algoritmos MapReduce en Hadoop, es decir, una
	 * t�cnica a la hora de programar ampliando el algoritmo implementado en el
	 * Mapper para reducir el n�mero de pares key/value que emite.
	 * 
	 * Esta t�cnica se puede dividir en dos formas: una local y una global.
	 * 
	 * Volviendo al ejemplo m�s b�sico que tenemos, el WordCount, en el que
	 * tenemos una clase Mapper y una Reducer, con esta t�cnica el Reducer va a
	 * ser exactamente igual, pero en el Mapper vamos a introducir directamente
	 * la funcionalidad del Combiner, m�s bien en lo que se basa. Y esto lo hace
	 * utilizando un objeto de tipo Map. Este se refiere al algoritmo local, ya
	 * que es local respecto al m�todo. Con esta t�cnica hemos reducido desde el
	 * principio el n�mero de pares key/value que emite el Mapper y que llegan
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
	 * Este m�todo todav�a conlleva a que el Mapper cree y destruya m�ltiples
	 * objetos o realice otras tareas que ocupan memoria de una forma
	 * considerable, y es por ello que se debe optimizar llegando a la forma
	 * global, de tal forma que el Mapper s�lo emitir� los pares key/value
	 * necesarios al Shuffle and Sort y al Reducer.
	 * 
	 * Esa optimizaci�n se hace utilizando los m�todos setup() y cleanup(), que
	 * como ya coment� en la entrada
	 * 
	 * sobre estos m�todos, se van a ejecutar s�lo una vez, el primero antes de
	 * que todas las tareas map comiencen y el segundo justo cuando todas las
	 * tareas map finalizan y antes de que el Mapper sea destru�do.
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
	 * Desarrollar la clase Mapper de esta forma mejora la opci�n anterior pero
	 * sigue teniendo problemas de memoria.
	 * 
	 * Recordemos que cuando usamos el Combiner, primero el Mapper va a tratar
	 * los datos y va a emitir los pares key/value, los va a escribir en el
	 * disco local y despu�s va a ser el Combiner quien va a actuar.
	 * 
	 * Si implementamos el algoritmo anterior nos puede surgir un problema con
	 * la memoria, ya que si estamos hablando de TB de datos, o no hay
	 * coincidencia en las palabras y se genera un registro para cada palabra,
	 * estos se van a cargar en el HashMap en memoria y no se van a liberar
	 * hasta que todas las tareas map han finalizado y el objeto HashMap se
	 * destruya.
	 * 
	 * La soluci�n para limitar la memoria es "bloquear" la entrada de pares
	 * key/value y liberar la estructura HashMap cada cierto tiempo.
	 * 
	 * As� que la idea es en vez de emitir los datos intermedios una vez que
	 * todos los pares key/value han sido tratados, emitir los datos intermedios
	 * despu�s de haber tratado N pares key/value.
	 * 
	 * Esto tendr�a una f�cil soluci�n, que es poner un contador FLUSH_COUNTER,
	 * y cuando el n�mero de pares key/value tratados llegue a ese contador,
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
	 * Estamos en un punto donde podemos seguir haci�ndonos preguntas una tras
	 * otra para optimizar este algoritmo y aplicar este patr�n de dise�o
	 * correctamente, y la siguiente cuesti�n ser�a �c�mo sabemos cu�l es el
	 * n�mero FLUSH_COUNT de pares que hay que tratar antes de que la memoria
	 * falle?.
	 * 
	 * El libro de referencia propone que m�s que crear un contador de pares
	 * key/value, poner un l�mite a la memoria que se utiliza y en el momento
	 * que el HashMap ha llegado a ese l�mite se liberar�a. El problema es que
	 * en Java no es f�cil determinar de forma r�pida el tama�o de la memoria
	 * ocupada por un objeto, por eso, es mejor elegir un FLUSH_COUNT lo m�s
	 * alto posible y teniendo cuidado que el HashMap no provoque un Out Of
	 * Memory.
	 */
}
