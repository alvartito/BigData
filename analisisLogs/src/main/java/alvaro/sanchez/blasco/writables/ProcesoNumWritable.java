package alvaro.sanchez.blasco.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class ProcesoNumWritable implements Writable {
	private Text proceso;
	private IntWritable num;
	
	public ProcesoNumWritable(){
		proceso = new Text();
		num = new IntWritable();
	}

	public ProcesoNumWritable(Text song, IntWritable num) {
		this.proceso = song;
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcesoNumWritable other = (ProcesoNumWritable) obj;
		if (num == null) {
			if (other.num != null)
				return false;
		} else if (!num.equals(other.num))
			return false;
		if (proceso == null) {
			if (other.proceso != null)
				return false;
		} else if (!proceso.equals(other.proceso))
			return false;
		return true;
	}

	public IntWritable getNum() {
		return num;
	}

	public void setNum(IntWritable num) {
		this.num = num;
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
		return proceso + ":" + num;
	}

	public Text getProceso() {
		return proceso;
	}

	public void setProceso(Text proceso) {
		this.proceso = proceso;
	}

}
