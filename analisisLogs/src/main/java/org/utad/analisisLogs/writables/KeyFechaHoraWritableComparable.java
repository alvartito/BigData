package org.utad.analisisLogs.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

/**
 * @author Álvaro Sánchez Blasco
 * 
 * */
public class KeyFechaHoraWritableComparable implements WritableComparable<KeyFechaHoraWritableComparable> {

	private Text fecha;
	private Text hora;
	
	public KeyFechaHoraWritableComparable() {
		fecha = new Text();
		hora = new Text();
	}

	public KeyFechaHoraWritableComparable(Text fecha, Text hora) {
		this.fecha = fecha;
		this.hora = hora;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((hora == null) ? 0 : hora.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KeyFechaHoraWritableComparable other = (KeyFechaHoraWritableComparable) obj;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (hora == null) {
			if (other.hora != null)
				return false;
		}
		return hora.equals(other.hora);
	}


	@Override
	public void readFields(DataInput in) throws IOException {
		fecha.readFields(in);
		hora.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		fecha.write(out);
		hora.write(out);
	}

	@Override
	public int compareTo(KeyFechaHoraWritableComparable o) {
		int cmp = fecha.compareTo(o.getFecha());
		if (cmp != 0) {
			return cmp;
		} else
			return hora.compareTo(o.getHora());
	}

	@Override
	public String toString() {
		return "["+fecha+" "+hora+"]";
	}

	public Text getFecha() {
		return fecha;
	}

	public void setFecha(Text fecha) {
		this.fecha = fecha;
	}

	public Text getHora() {
		return hora;
	}

	public void setHora(Text hora) {
		this.hora = hora;
	}

}
