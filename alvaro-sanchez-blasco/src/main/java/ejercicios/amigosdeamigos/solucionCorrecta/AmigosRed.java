package ejercicios.amigosdeamigos.solucionCorrecta;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import alvaro.sanchez.blasco.util.writables.AmigosWritable;
/** @author Álvaro Sánchez Blasco */
public class AmigosRed extends Reducer<Text, AmigosWritable, Text, Text> {

	Text amigoA = new Text();
	Text amigoB = new Text();

	protected void reduce(Text amigo, Iterable<AmigosWritable> relaciones,
			Context context) throws IOException, InterruptedException {
		ArrayList<String> directRel = new ArrayList<String>();
		ArrayList<String> reverseRel = new ArrayList<String>();

		for (AmigosWritable relacion : relaciones) {
			if (relacion.isRelacion()) {
				if(!directRel.contains(relacion.getAmigo()))
				directRel.add(relacion.getAmigo());
			} else {
				reverseRel.add(relacion.getAmigo());
			}
		}

		if (reverseRel.size() > 0 && directRel.size() > 0) {
			for (int i = 0; i < reverseRel.size(); i++) {
				for (int j = 0; j < directRel.size(); j++) {
					context.write(new Text(reverseRel.get(i)), new Text(
							directRel.get(j)));
				}
			}
		}
	};
}