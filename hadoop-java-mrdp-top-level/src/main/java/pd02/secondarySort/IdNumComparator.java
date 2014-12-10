package pd02.secondarySort;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public  class IdNumComparator extends WritableComparator {
	protected IdNumComparator() {
		super(CkIdNum.class, true);
	}

	@Override
	public int compare(WritableComparable w1, WritableComparable w2) {
		return 0;
		// TODO completar
	}
}
