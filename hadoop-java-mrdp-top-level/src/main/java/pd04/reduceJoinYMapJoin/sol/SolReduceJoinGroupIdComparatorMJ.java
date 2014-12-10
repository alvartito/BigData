package pd04.reduceJoinYMapJoin.sol;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class SolReduceJoinGroupIdComparatorMJ extends WritableComparator {

	public SolReduceJoinGroupIdComparatorMJ() {
		super(SolKeyRJMJ.class, true);
	}

	@Override
	public int compare(WritableComparable w1, WritableComparable w2) {
		SolKeyRJMJ cin1 = (SolKeyRJMJ) w1;
		SolKeyRJMJ cin2 = (SolKeyRJMJ) w2;
		return (cin1.getId()).compareTo(cin2.getId());
	}

}
