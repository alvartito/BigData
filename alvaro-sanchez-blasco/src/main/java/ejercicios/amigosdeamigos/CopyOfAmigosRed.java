package ejercicios.amigosdeamigos;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import writables.AmigosWritable2;

/** @author Álvaro Sánchez Blasco */
public class CopyOfAmigosRed extends Reducer<Text, AmigosWritable2, Text, Text> {

	Text amigoDirecto;
	Text amigoInverso;
	
	protected void reduce(Text amigo, Iterable<AmigosWritable2> relaciones,
			Context context) throws IOException, InterruptedException {
		ArrayList<Text> directRel = new ArrayList<Text>();
		ArrayList<Text> reverseRel = new ArrayList<Text>();

		/*
		 * In the LineRecordReader the same key and value objects are used in
		 * each call to the nextKeyValue method, only their contents are
		 * changed. A copy of the object has to be made in the user code if the
		 * key and value data should be hold.
		 */
		for (AmigosWritable2 relacion : relaciones) {
			if (relacion.getRelacion().equals(new BooleanWritable(true))) {
				amigoDirecto = new Text(relacion.getAmigo());
				if (!directRel.contains(amigoDirecto))
					directRel.add(amigoDirecto);
			} else {
				amigoInverso = new Text(relacion.getAmigo()); 
				reverseRel.add(amigoInverso);
			}
		}

		if (reverseRel.size() > 0 && directRel.size() > 0) {
			for (int i = 0; i < reverseRel.size(); i++) {
				for (int j = 0; j < directRel.size(); j++) {
					context.write(reverseRel.get(i), directRel.get(j));
				}
			}
		}
	};
}