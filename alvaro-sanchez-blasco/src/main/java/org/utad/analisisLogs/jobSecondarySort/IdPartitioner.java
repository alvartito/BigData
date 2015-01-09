package org.utad.analisisLogs.jobSecondarySort;

import org.apache.hadoop.mapreduce.Partitioner;

public class IdPartitioner extends Partitioner<CkIdNumWritableComparable, SongNumWritable> {

	@Override
	public int getPartition(CkIdNumWritableComparable key, SongNumWritable value, int numPartitions) {
		// multiply by 127 to perform some mixing
		return Math.abs(key.getId().get() * 127) % numPartitions;
	}
}
