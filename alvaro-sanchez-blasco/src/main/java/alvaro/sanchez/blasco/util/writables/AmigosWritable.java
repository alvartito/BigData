package alvaro.sanchez.blasco.util.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
/** @author Álvaro Sánchez Blasco */
public class AmigosWritable implements
Writable {

	private String amigo;
	private boolean relacion;
	
	public AmigosWritable(){}
	
	public AmigosWritable(boolean relacion, String amigo) {
		set(relacion, amigo);
	}

	public void set(boolean relacion, String amigo) {
		this.amigo = amigo;
		this.relacion = relacion;
		setRelacion(relacion);
		setAmigo(amigo);
	}
	
	public String get() {
		return amigo+ "," + relacion;
	}
	
	public String toString() {
		return amigo + "|" + relacion;
	}
	
	public void write(DataOutput out) throws IOException {
		Text.writeString(out, amigo);
		out.writeBoolean(relacion);
	}

	public void readFields(DataInput in) throws IOException {
		amigo = Text.readString(in);
		relacion = in.readBoolean();
	}

	public String getAmigo() {
		return amigo;
	}

	private void setAmigo(String amigo) {
		this.amigo = amigo;
	}

	public boolean isRelacion() {
		return relacion;
	}

	private void setRelacion(boolean relacion) {
		this.relacion = relacion;
	}

}
