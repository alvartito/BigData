package org.utad.analisisLogs.ejemplos.multipleOutput;

public class MultipleOutputsHadoop {

	/*
	 * Output Formats 

Los Output Formats son muy parecidos a los tipos vistos en la entrada anterior de los Input Formats.
Pero esta vez la interfaz OutputFormat va a determinar c�mo ser� la salida del Job que vamos a ejecutar.
Para establecer el output format se configura en el Driver a trav�s de:

job.setOutputFormatClass(Tipo.class); 

La clase base de salida en Hadoop es FileOutputFormat (que hereda de OutputFormat), y a partir de aqu� existen diferentes tipos para poder implementar esa salida, estos son algunos:

TextOutputFormat
SequenceFileOutputFormat

SequenceFileAsBinaryOutputFormat

MultipleOutputFormat

El tipo por defecto de salida es el TextOutputFormat, que escribe cada registro (un par key/value) en l�neas de texto separadas, y los tipos de los pares key/value pueden ser de cualquier tipo, siempre y cuando implementen el m�todo toString().

Tambi�n podr�a ser posible eliminar la key o el value de la salida a trav�s del tipo NullWritable o los dos, que ser�a mejor definir la salida del Job con el tipo NullOutputFormat.
Si queremos que la salida sea nula, en el Driver definir�amos:

job.setOutputFormatClass(NullOutputFormat.class); 

Si s�lo queremos eliminar la key o el value se har�a con:

job.setOutputKeyClass(NullWritable.class);

o con: 

job.setOutputValueClass(NullWritable.class); 

Como en los Input Formats, el output tambi�n dispone de salidas binarias como el 
SequenceFileOutputFormat, que como indica, escribe ficheros de tipo Sequence Files (ficheros binarios en los que se escriben pares key/value) y su subclase SequenceFileAsBinaryOutputFormat, que escribe sequence files en los que las key y values est�n codificados en binario.

En los tipos FileOutputFormat, por defecto se escribe un fichero por cada reducer, que normalmente est� predeterminado a uno, pero se puede cambiar ese n�mero de reducers. El nombre de cada fichero es de la forma 

part-r-00000, part-r-00001..., siendo la �ltima parte el n�mero de reducer. As� que una de las formas de dividir las salidas puede ser usando varios reducers, pero esta no es la soluci�n que nos interesa ver aqu�.

La soluci�n ser�a utilizando la clase MultipleOutputs, se crear�a un objeto de este tipo en el reducer y en vez de llamar al write del Context, se har�a al write del MultipleOutputs.

Tambi�n la ventaja de este tipo de Output es que puedes definir el nombre que deseas darle al fichero 

name-r-00000

Ejemplo de MultipleOutputs:

A partir de nuestro fichero score.txt queremos un programa que separe a los jugadores y sus puntuaciones por fecha agrupados en ficheros separados.

Recordamos que el fichero es de este tipo

01-11-2012 Pepe Perez Gonzalez 21 01-11-2012 Ana Lopez Fernandez 14 15-11-2012 John Smith 13 01-12-2012 Pepe Perez Gonzalez 25 ...

El Driver es como lo configuramos normalmente, no tiene ninguna configuraci�n especial para hacer el MultipleOutput: 

public class TestMultipleOutputDriver { public static void main(String[] args) throws Exception { Configuration conf = new Configuration(); Job job = new Job(conf); job.setJarByClass(TestMultipleOutputDriver.class); job.setJobName("Word Count"); job.setMapperClass(TestMultipleOutputMapper.class); job.setReducerClass(TestMultipleOutputReducer.class); job.setInputFormatClass(KeyValueTextInputFormat.class); FileInputFormat.setInputPaths(job, new Path(args[0])); FileOutputFormat.setOutputPath(job, new Path(args[1])); job.setOutputKeyClass(Text.class); job.setOutputValueClass(Text.class); boolean success = job.waitForCompletion(true); System.exit(success ? 0:1); } } 

En el Mapper lo �nico que hacemos es emitir el par key/value que recibimos, ya que no estamos haciendo ning�n tratamiento de los datos como tal para este ejemplo.

public class TestMultipleOutputMapper extends Mapper<Text, Text, Text, Text> { public void map(Text key, Text values, Context context) throws IOException, InterruptedException{ context.write(key, values); } } 

En el Reducer creamos un objeto de tipo MultipleOutputs que vamos a inicializar en el m�todo setup y es el que vamos a utilizar para escribir la salida.

name ser� el prefijo del fichero. 

public class TestMultipleOutputReducer extends Reducer<Text, Text, Text, Text> { private MultipleOutputs<Text, Text> multipleOut; @Override protected void cleanup(Context context) throws IOException, InterruptedException { multipleOut.close(); } @Override protected void setup(Context context) throws IOException, InterruptedException { multipleOut = new MultipleOutputs<Text, Text>(context); } @Override public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException { String name = key.toString().replace("-", ""); for(Text value:values){ multipleOut.write( key, value, name); //Podr�a a�adir m�s salidas seg�n mis necesidades // a trav�s de cl�usulas if, o porque un par key/value // traiga diversas informaciones que quiero subdividir // en diferentes ficheros // if(caso1) multipleOut.write( key, value, name2); // multipleOut.write( key, value, name3); } } } 

En este ejemplo con el MultipleOutputs te obliga que aunque quieras que las salidas sean en distintos ficheros, los pares key/value que emites sean todos del mismo tipo del que has definido la clase MultipleOutputs<Text, Text>, es decir, la key debe ser de tipo Text, y el valor tambi�n debe ser de tipo Text.

Tambi�n es posible emitir m�ltiples salidas en ficheros diferentes y que cada salida sea con tipos distintos para cada fichero.

En el siguiente ejemplo recibo como entrada un fichero con un listado de papers, en los que cada l�nea contiene la publicaci�n del paper, los autores y el t�tulo del paper de la forma:

paper-id:::author1::author2::...::authorN:::title

journals/cl/SantoNR90:::Michele Di Santo::Libero Nigro::Wilma Russo:::Programmer-Defined Control Abstractions in Modula-2. 

Quiero 3 ficheros en la salida de este algoritmo:

- Un fichero paper que contenga: String paper-id, String Title

- Un fichero autor que contenga: Int autor-id (se crea en el algoritmo), String nombre autor

- Un fichero paper/autor que los relacione: String paper-id, Int autor-id

Como vemos necesitamos que una salida sea <Text, Text>, otra salida sea <IntWritable, Text> y la �ltima salida sea <Text, IntWritable>. 

Esto se har�a a�adiendo en el Driver las siguientes l�neas, donde se asigna un ID a la salida y de qu� tipos son esas salidas:

(Podr�is encontrar el c�digo fuente completo en este enlace)

MultipleOutputs.addNamedOutput(job, "Autor", TextOutputFormat.class,  IntWritable.class, Text.class); MultipleOutputs.addNamedOutput(job, "Paper", TextOutputFormat.class,  Text.class, Text.class); MultipleOutputs.addNamedOutput(job, "PaperAutor", TextOutputFormat.class,  Text.class, IntWritable.class);

Posteriormente, en el Reducer se har�a de la forma:

for (PaperWritable value : values) { //Output tabla Autor multipleOut.write("Autor", new IntWritable(contador), key); //Output tabla Paper multipleOut.write("Paper", value.getIdPaper(),  value.getTituloPaper()); //Output tabla paper/autor multipleOut.write("PaperAutor", value.getIdPaper(),  new IntWritable(contador)); contador ++; } 

	 * */
	
	
	
}
