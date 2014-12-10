package pd03.reduceJoin.sol;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SolReduceJoinReducer extends
		Reducer<SolKeyRJ, SolUnionDatos, SolKeyRJ, SolUnionDatos> {

	private Text nombre = new Text();
	private Text apellido = new Text();
	private Text fechaInc = new Text();

	@Override
	protected void reduce(SolKeyRJ key, Iterable<SolUnionDatos> values,
			Context context) throws IOException, InterruptedException {

		boolean hasDescribedEmployee = false;

		for (SolUnionDatos v : values) {
			if (v.getIsEmployee().get()) {
				nombre.set(v.getNombre());
				apellido.set(v.getApellido());
				fechaInc.set(v.getFechaInc());
				hasDescribedEmployee = true;
			} else {
				if (hasDescribedEmployee) {
					v.setNombre( new Text(nombre));
					v.setApellido( new Text(apellido));
					v.setFechaInc( new Text(fechaInc));
					context.write(key, v);
				} else {
					return;
				}
			}
		}
	}

}
