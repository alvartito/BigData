package org.utad.analisisLogs.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.WritableComparable;

/**
 * @author Álvaro Sánchez Blasco
 * 
 * */
public class KeyRJWritableComparable implements WritableComparable<KeyRJWritableComparable> {
	BooleanWritable isEmployee;
	IntWritable id;

	public int compareTo(KeyRJWritableComparable o) {
		int cmp = id.compareTo(o.getId());
		if (cmp != 0) {
			return cmp;
		} else
			return -isEmployee.compareTo(o.getIsEmployee());
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		isEmployee.readFields(in);
		id.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		isEmployee.write(out);
		id.write(out);
	}

	public KeyRJWritableComparable() {
		isEmployee = new BooleanWritable();
		id = new IntWritable();
	}

	public KeyRJWritableComparable(BooleanWritable isEmployee, IntWritable id) {
		this.isEmployee = isEmployee;
		this.id = id;
	}

	public BooleanWritable getIsEmployee() {
		return isEmployee;
	}

	public void setIsEmployee(BooleanWritable isEmployee) {
		this.isEmployee = isEmployee;
	}

	public IntWritable getId() {
		return id;
	}

	public void setId(IntWritable id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof KeyRJWritableComparable))
			return false;
		KeyRJWritableComparable other = (KeyRJWritableComparable) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isEmployee == null) {
			if (other.isEmployee != null)
				return false;
		} else if (!isEmployee.equals(other.isEmployee))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[id=" + id + "]";
	}

}
