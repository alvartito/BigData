package pd04.reduceJoinYMapJoin.sol;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SolReduceJoinReducerMJ extends
		Reducer<SolKeyRJMJ, SolUnionDatosMJ, SolKeyRJMJ, SolUnionDatosMJ> {

	private Text nombre = new Text();
	private Text apellido = new Text();
	private Text fechaInc = new Text();
	private Text dptName = new Text();

	@Override
	protected void reduce(SolKeyRJMJ key, Iterable<SolUnionDatosMJ> values,
			Context context) throws IOException, InterruptedException {

		boolean hasDescribedEmployee = false;

		for (SolUnionDatosMJ v : values) {
			if (v.getIsEmployee().get()) {
				nombre.set(v.getNombre());
				apellido.set(v.getApellido());
				fechaInc.set(v.getFechaInc());
				dptName.set(v.getDptName());
				hasDescribedEmployee = true;
			} else {
				if (hasDescribedEmployee) {
					v.setNombre( new Text(nombre));
					v.setApellido( new Text(apellido));
					v.setFechaInc( new Text(fechaInc));
					v.setDptName( new Text(dptName));
					context.write(key, v);
				} else {
					return;
				}
			}
		}
	}

}
