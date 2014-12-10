package pd02.secondarySort;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class SongNum implements Writable {
	private Text song;
	private IntWritable num;
	
	// TODO escribir constructores, getters y setters

	public SongNum() {
		song = new Text();
		num = new IntWritable();
	}

	public SongNum(Text song, IntWritable num) {
		this.song = song;
		this.num = num;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((num == null) ? 0 : num.hashCode());
		result = prime * result + ((song == null) ? 0 : song.hashCode());
		return result;
	}
	
	// TODO escribir los metodos de Writable
	
	@Override
	public String toString() {
		return "[" + song + " - " + num + "]";
	}

	@Override
	public void write(DataOutput out) throws IOException {
		song.write(out);
		num.write(out);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		song.readFields(in);
		num.readFields(in);
	}

}
