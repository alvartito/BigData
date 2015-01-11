package alvaro.sanchez.blasco.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

/**
 * @author Álvaro Sánchez Blasco
 * 
 * */
public class FechaHoraProcesoWritableComparable implements
		WritableComparable<FechaHoraProcesoWritableComparable> {

	private Text fecha;
	private Text hora;
	private Text proceso;

	public FechaHoraProcesoWritableComparable() {
		fecha = new Text();
		hora = new Text();
		proceso = new Text();
	}

	public FechaHoraProcesoWritableComparable(Text fecha, Text hora,
			Text proceso) {
		this.fecha = fecha;
		this.hora = hora;
		this.proceso = proceso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((hora == null) ? 0 : hora.hashCode());
		result = prime * result + ((proceso == null) ? 0 : proceso.hashCode());
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
		FechaHoraProcesoWritableComparable other = (FechaHoraProcesoWritableComparable) obj;
		if (fecha == null) {
			if (other.getFecha() != null)
				return false;
		} else if (!fecha.equals(other.getFecha()))
			return false;
		if (hora == null) {
			if (other.getHora() != null)
				return false;
		}
		return hora.equals(other.getHora());
		// TODO hacer lo mismo con el proceso
	}

	public void readFields(DataInput in) throws IOException {
		fecha.readFields(in);
		hora.readFields(in);
		proceso.readFields(in);
	}

	public void write(DataOutput out) throws IOException {
		fecha.write(out);
		hora.write(out);
		proceso.write(out);
	}

	public int compareTo(FechaHoraProcesoWritableComparable o) {
		int cmp = fecha.compareTo(o.getFecha());
		if (cmp != 0) {
			return cmp;
		} else {
			cmp = hora.compareTo(o.getHora());
			if (cmp != 0) {
				return cmp;
			} else {
				return proceso.compareTo(o.getProceso());
			}
		}
	}

	@Override
	public String toString() {
		return fecha + " " + hora + " " + proceso;
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

	public Text getProceso() {
		return proceso;
	}

	public void setProceso(Text proceso) {
		this.proceso = proceso;
	}

}
