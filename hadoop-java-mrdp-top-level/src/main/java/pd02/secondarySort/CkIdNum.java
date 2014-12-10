package pd02.secondarySort;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class CkIdNum implements WritableComparable<CkIdNum> {
	private IntWritable id;
	private IntWritable num;

	// TODO escribir constructores, getters y setters
	public CkIdNum() {
		id = new IntWritable();
		num = new IntWritable();
	}

	public CkIdNum(IntWritable id, IntWritable num) {
		set(id, num);
	}

	public void set(IntWritable id, IntWritable num) {
		setId(id);
		setNum(num);
	}

	// TODO escribir los metodos de Writable

	// TODO escribir los metodos de Comparable

	@Override
	public String toString() {
		return "IdSongNum [id=" + id + ", num=" + num + "]";
	}

	@Override
	public void write(DataOutput out) throws IOException {
		id.write(out);
		num.write(out);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		id.readFields(in);
		num.readFields(in);
	}

	@Override
	public int compareTo(CkIdNum o) {
		int cmp = id.compareTo(o.getId());
		if (cmp != 0) {
			return cmp;
		} else
			return num.compareTo(o.getNum());
	}

	public IntWritable getId() {
		return id;
	}

	private void setId(IntWritable id) {
		this.id = id;
	}

	public IntWritable getNum() {
		return num;
	}

	private void setNum(IntWritable num) {
		this.num = num;
	}

}
