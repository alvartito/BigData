package util.writables;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Writable;

public class MaponyArrayWritable extends ArrayWritable {

	public MaponyArrayWritable() {
		super(Writable.class);
	}
}
