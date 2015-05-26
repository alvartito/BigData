package writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

/** @author Álvaro Sánchez Blasco */
public class AmigosWritable2 implements Writable {

	private Text amigo;
	private BooleanWritable relacion;

	public AmigosWritable2() {
		amigo = new Text();
		relacion = new BooleanWritable();
	}

	@Override
	public int hashCode() {
		final int prime = 7;
		int result = 1;
		result = prime * result
				+ ((amigo == null) ? 0 : amigo.hashCode());
		result = prime
				* result
				+ ((relacion == null) ? 0 : relacion.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AmigosWritable2 other = (AmigosWritable2) obj;
		if (amigo == null) {
			if (other.amigo != null) {
				return false;
			}
		} else if (!amigo.equals(other.amigo)) {
			return false;
		}
		if (relacion == null) {
			return false;
		} else {
			return this.relacion == other.relacion;
		}
	}

	public AmigosWritable2(BooleanWritable relacion, Text amigo) {
		set(relacion, amigo);
	}

	public void set(BooleanWritable relacion, Text amigo) {
		setRelacion(relacion);
		setAmigo(amigo);
	}

	public String toString() {
		return "[" + amigo + "|" + relacion + "]";
	}

	public void write(DataOutput out) throws IOException {
		amigo.write(out);
		relacion.write(out);
	}

	public void readFields(DataInput in) throws IOException {
		amigo.readFields(in);
		relacion.readFields(in);
	}

	public Text getAmigo() {
		return amigo;
	}

	public void setAmigo(Text amigoWritable) {
		this.amigo = amigoWritable;
	}

	public BooleanWritable getRelacion() {
		return relacion;
	}

	public void setRelacion(BooleanWritable relacionWritable) {
		this.relacion = relacionWritable;
	}

}
