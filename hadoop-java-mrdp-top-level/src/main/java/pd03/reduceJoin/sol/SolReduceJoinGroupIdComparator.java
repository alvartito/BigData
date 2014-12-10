package pd03.reduceJoin.sol;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class SolReduceJoinGroupIdComparator extends WritableComparator {

	public SolReduceJoinGroupIdComparator() {
		super(SolKeyRJ.class, true);
	}

	@Override
	public int compare(WritableComparable w1, WritableComparable w2) {
		SolKeyRJ cin1 = (SolKeyRJ) w1;
		SolKeyRJ cin2 = (SolKeyRJ) w2;
		return (cin1.getId()).compareTo(cin2.getId());
	}

}
