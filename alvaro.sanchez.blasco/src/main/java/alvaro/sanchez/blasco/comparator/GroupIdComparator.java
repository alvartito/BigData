package alvaro.sanchez.blasco.comparator;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import alvaro.sanchez.blasco.writables.FechaHoraNumWritableComparable;

/**
 * @author cloudera
 * 
 * 
 * */

public class GroupIdComparator extends WritableComparator {

	public GroupIdComparator() {
		super(FechaHoraNumWritableComparable.class, true);
	}

	@Override
	public int compare(WritableComparable w1, WritableComparable w2) {
		FechaHoraNumWritableComparable cin1 = (FechaHoraNumWritableComparable) w1;
		FechaHoraNumWritableComparable cin2 = (FechaHoraNumWritableComparable) w2;
		return (cin1.getFechaHora()).compareTo(cin2.getFechaHora());
	}

}
