package pd02.secondarySort.sol;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public  class IdNumComparator extends WritableComparator {
	protected IdNumComparator() {
		super(CkIdNumWritableComparable.class, true);
	}

	@Override
	public int compare(WritableComparable w1, WritableComparable w2) {
		CkIdNumWritableComparable cin1 = (CkIdNumWritableComparable) w1;
		CkIdNumWritableComparable cin2 = (CkIdNumWritableComparable) w2;
		
		int cmp = cin1.getId().compareTo(cin2.getId());
		if (cmp != 0) {
			return cmp;
		}
		// negative because we want most popular songs first
		return -(cin1.getNum()).compareTo(cin2.getNum());
	}
}
