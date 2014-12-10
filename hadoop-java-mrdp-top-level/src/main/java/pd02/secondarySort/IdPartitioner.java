package pd02.secondarySort;

import org.apache.hadoop.mapreduce.Partitioner;

public class IdPartitioner extends Partitioner<CkIdNum, SongNum> {

	@Override
	public int getPartition(CkIdNum key, SongNum value, int numPartitions) {
		return numPartitions;
		// TODO completar
	}
}
