package writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

/** @author Álvaro Sánchez Blasco*/
public class HistogramaWritable2 implements Writable {

	private IntWritable numero1;
	private IntWritable numero2;

	public HistogramaWritable2() {
		numero1 = new IntWritable();
		numero2 = new IntWritable();
	}

	public HistogramaWritable2(IntWritable n1, IntWritable n2) {
		this.numero1 = n1;
		this.numero2 = n2;
	}

	public String get() {
		return numero1 + "," + numero2;
	}

	public String toString() {
		return numero1 + "," + numero2;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		numero1.write(out);
		numero2.write(out);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		numero1.readFields(in);
		numero2.readFields(in);;
	}

	public IntWritable getNumero1() {
		return numero1;
	}

	public void setNumero1(IntWritable numero1) {
		this.numero1 = numero1;
	}

	public IntWritable getNumero2() {
		return numero2;
	}

	public void setNumero2(IntWritable numero2) {
		this.numero2 = numero2;
	}

	public IntWritable getMax() {
		int flag = getNumero1().compareTo(getNumero2());

		if (flag > 0) {
			// El primero es mayor
			return getNumero1();
		} else if (flag < 0) {
			// El segundo es mayor
			return getNumero2();
		} else {
			// Los dos son iguales
			return getNumero1();
		}
	}

	public IntWritable getMin() {
		int flag = getNumero1().compareTo(getNumero2());
		if (flag > 0) {
			// El segundo es menor
			return getNumero2();
		} else if (flag < 0) {
			// El primero es menor
			return getNumero1();
		} else {
			// Son iguales
			return getNumero1();
		}

	}
	
	@Override
	public int hashCode() {
		final int prime = 7;
		int result = 1;
		result = prime * result
				+ ((numero1 == null) ? 0 : numero1.hashCode());
		result = prime
				* result
				+ ((numero2 == null) ? 0 : numero2.hashCode());
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistogramaWritable2 other = (HistogramaWritable2) obj;
		if (getNumero1() == null) {
			if (other.getNumero1() != null) {
				return false;
			}
		} else if (getNumero1() != other.getNumero1()) {
			return false;
		}
		if (getNumero2() == null) {
			if (other.getNumero2() != null) {
				return false;
			}
		} else if (getNumero2() != other.getNumero2()) {
			return false;
		}
		return true;
	}
}