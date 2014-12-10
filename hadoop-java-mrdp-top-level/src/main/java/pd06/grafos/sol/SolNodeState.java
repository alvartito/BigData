package pd06.grafos.sol;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class SolNodeState implements Writable {
	BooleanWritable hasListNeighboors;
	IntWritable distance;
	Text listNeighboors;

	public SolNodeState() {
		hasListNeighboors = new BooleanWritable();
		distance = new IntWritable();
		listNeighboors = new Text();
	}

	public SolNodeState(BooleanWritable hasListNeighboors,
			IntWritable distance, Text listNeighboors) {
		this.hasListNeighboors = hasListNeighboors;
		this.distance = distance;
		this.listNeighboors = listNeighboors;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		hasListNeighboors.readFields(in);
		distance.readFields(in);
		listNeighboors.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		hasListNeighboors.write(out);
		distance.write(out);
		listNeighboors.write(out);
	}

	public BooleanWritable getHasListNeighboors() {
		return hasListNeighboors;
	}

	public void setHasListNeighboors(BooleanWritable hasListNeighboors) {
		this.hasListNeighboors = hasListNeighboors;
	}

	public IntWritable getDistance() {
		return distance;
	}

	public void setDistance(IntWritable distance) {
		this.distance = distance;
	}

	public Text getListNeighboors() {
		return listNeighboors;
	}

	public void setListNeighboors(Text listNeighboors) {
		this.listNeighboors = listNeighboors;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((distance == null) ? 0 : distance.hashCode());
		result = prime
				* result
				+ ((hasListNeighboors == null) ? 0 : hasListNeighboors
						.hashCode());
		result = prime * result
				+ ((listNeighboors == null) ? 0 : listNeighboors.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof SolNodeState))
			return false;
		SolNodeState other = (SolNodeState) obj;
		if (distance == null) {
			if (other.distance != null)
				return false;
		} else if (!distance.equals(other.distance))
			return false;
		if (hasListNeighboors == null) {
			if (other.hasListNeighboors != null)
				return false;
		} else if (!hasListNeighboors.equals(other.hasListNeighboors))
			return false;
		if (listNeighboors == null) {
			if (other.listNeighboors != null)
				return false;
		} else if (!listNeighboors.equals(other.listNeighboors))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return distance.toString() + "\t" + listNeighboors.toString();
	}
}
