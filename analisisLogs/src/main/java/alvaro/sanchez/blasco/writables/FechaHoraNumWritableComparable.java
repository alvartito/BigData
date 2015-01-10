package alvaro.sanchez.blasco.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class FechaHoraNumWritableComparable implements WritableComparable<FechaHoraNumWritableComparable> {
	private Text fechaHora;
	private IntWritable num;

	public FechaHoraNumWritableComparable() {
		fechaHora = new Text();
		num = new IntWritable();
	}

	public FechaHoraNumWritableComparable(Text id, IntWritable num) {
		this.fechaHora = id;
		this.num = num;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fechaHora == null) ? 0 : fechaHora.hashCode());
		result = prime * result + ((num == null) ? 0 : num.hashCode());
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
		FechaHoraNumWritableComparable other = (FechaHoraNumWritableComparable) obj;
		if (fechaHora == null) {
			if (other.fechaHora != null)
				return false;
		} else if (!fechaHora.equals(other.fechaHora))
			return false;
		if (num == null) {
			if (other.num != null)
				return false;
		}
		return num.equals(other.num);
	}

	public IntWritable getNum() {
		return num;
	}

	public void setNum(IntWritable num) {
		this.num = num;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		fechaHora.readFields(in);
		num.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		fechaHora.write(out);
		num.write(out);
	}

	@Override
	public int compareTo(FechaHoraNumWritableComparable o) {
		int cmp = fechaHora.compareTo(o.getFechaHora());
		if (cmp != 0) {
			return cmp;
		} else
			return num.compareTo(o.getNum());
	}

	@Override
	public String toString() {
		return "["+fechaHora+"]";
		//[29/11/2014-08]		gnome-session:1,colord:2
	}

	public Text getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Text fechaHora) {
		this.fechaHora = fechaHora;
	}

}
