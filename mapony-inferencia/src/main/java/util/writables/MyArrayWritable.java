package util.writables;

import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Writable;

class MyArrayWritable extends ArrayWritable {

	public MyArrayWritable(Class<? extends Writable> valueClass, Writable[] values) {
		super(valueClass, values);
	}
 
	public MyArrayWritable(Class<? extends Writable> valueClass) {
		super(valueClass);
	}

	@Override
	public RawDataWritable[] get() {
		return (RawDataWritable[]) super.get();
	}

	@Override
	public void write(DataOutput arg0) throws IOException {
		for (RawDataWritable i : get()) {
			i.write(arg0);
		}
	}
}