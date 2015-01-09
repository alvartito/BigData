package org.utad.analisisLogs.jobSecondarySort;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class SongNumWritable implements Writable {
	private Text song;
	private IntWritable num;
	
	public SongNumWritable(){
		song = new Text();
		num = new IntWritable();
	}

	public SongNumWritable(Text song, IntWritable num) {
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SongNumWritable other = (SongNumWritable) obj;
		if (num == null) {
			if (other.num != null)
				return false;
		} else if (!num.equals(other.num))
			return false;
		if (song == null) {
			if (other.song != null)
				return false;
		} else if (!song.equals(other.song))
			return false;
		return true;
	}

	public Text getSong() {
		return song;
	}

	public void setSong(Text song) {
		this.song = song;
	}

	public IntWritable getNum() {
		return num;
	}

	public void setNum(IntWritable num) {
		this.num = num;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		song.readFields(in);
		num.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		song.write(out);
		num.write(out);
	}

	@Override
	public String toString() {
		return "[" + song + " - " + num + "]";
	}

}
