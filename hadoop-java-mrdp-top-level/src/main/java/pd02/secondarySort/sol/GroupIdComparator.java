package pd02.secondarySort.sol;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class GroupIdComparator extends WritableComparator {

	public GroupIdComparator() {
		super(CkIdNumWritableComparable.class, true);
	}

	@Override
	public int compare(WritableComparable w1, WritableComparable w2) {
		CkIdNumWritableComparable cin1 = (CkIdNumWritableComparable) w1;
		CkIdNumWritableComparable cin2 = (CkIdNumWritableComparable) w2;
		return (cin1.getId()).compareTo(cin2.getId());
	}

}
