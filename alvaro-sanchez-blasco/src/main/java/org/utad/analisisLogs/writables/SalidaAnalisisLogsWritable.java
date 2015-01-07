package org.utad.analisisLogs.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

/**
 * @author Álvaro Sánchez Blasco
 * 
 * */
public class SalidaAnalisisLogsWritable implements Writable {

	private KeyFechaHoraWritableComparable proceso;
	private ProcesoContadorWritable num;

	public SalidaAnalisisLogsWritable() {
		proceso = new KeyFechaHoraWritableComparable();
		num = new ProcesoContadorWritable();
	}

	public SalidaAnalisisLogsWritable(KeyFechaHoraWritableComparable proceso, ProcesoContadorWritable num) {
		this.proceso = proceso;
		this.num = num;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((num == null) ? 0 : num.hashCode());
		result = prime * result + ((proceso == null) ? 0 : proceso.hashCode());
		return result;
	}
	
	@Override
	public void readFields(DataInput in) throws IOException {
		proceso.readFields(in);
		num.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		proceso.write(out);
		num.write(out);
	}

	@Override
	public String toString() {
		return proceso.toString() + "\t" + num.toString();
	}
	
}
