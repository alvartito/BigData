package org.utad.analisisLogs.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

/**
 * @author Álvaro Sánchez Blasco
 * 
 * */
public class ProcesoContadorWritable implements Writable {
	private Text proceso;
	private IntWritable contador;
	
	public ProcesoContadorWritable(){
		proceso = new Text();
		contador = new IntWritable();
	}

	public ProcesoContadorWritable(Text proceso, IntWritable num) {
		this.proceso = proceso;
		this.contador = num;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contador == null) ? 0 : contador.hashCode());
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
		ProcesoContadorWritable other = (ProcesoContadorWritable) obj;
		if (contador == null) {
			if (other.contador != null)
				return false;
		} else if (!contador.equals(other.contador))
			return false;
		if (proceso == null) {
			if (other.proceso != null)
				return false;
		} else if (!proceso.equals(other.proceso))
			return false;
		return true;
	}

	public Text getProceso() {
		return proceso;
	}

	public void setProceso(Text proceso) {
		this.proceso = proceso;
	}

	public IntWritable getContador() {
		return contador;
	}

	public void setContador(IntWritable num) {
		this.contador = num;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		proceso.readFields(in);
		contador.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		proceso.write(out);
		contador.write(out);
	}

	@Override
	public String toString() {
		return proceso + ":" + contador;
	}

}
