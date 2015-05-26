package writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

/** @author Álvaro Sánchez Blasco*/
public class HistogramaWritable implements Writable {

	private Double numero1;
	private Double numero2;

	public HistogramaWritable() {
	}

	public HistogramaWritable(Double n1, Double n2) {
		set(n1, n2);
	}

	public void set(Double n1, Double n2) {
		this.numero1 = n1;
		this.numero2 = n2;
		setNumero1(n1);
		setNumero2(n2);
	}

	public String get() {
		return numero1 + "," + numero2;
	}

	public String toString() {
		return numero1 + "," + numero2;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeDouble(numero1);
		out.writeDouble(numero2);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		numero1 = in.readDouble();
		numero2 = in.readDouble();
	}

	public Double getNumero1() {
		return numero1;
	}

	private void setNumero1(Double numero1) {
		this.numero1 = numero1;
	}

	public Double getNumero2() {
		return numero2;
	}

	private void setNumero2(Double numero2) {
		this.numero2 = numero2;
	}

	public Double getMax() {
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

	public Double getMin() {
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
}