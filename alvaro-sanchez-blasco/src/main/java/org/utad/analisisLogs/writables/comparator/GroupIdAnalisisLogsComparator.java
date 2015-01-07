package org.utad.analisisLogs.writables.comparator;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.utad.analisisLogs.writables.KeyFechaHoraWritableComparable;

/**
 * @author Álvaro Sánchez Blasco
 * 
 * */
public class GroupIdAnalisisLogsComparator extends WritableComparator {

	public GroupIdAnalisisLogsComparator() {
		super(KeyFechaHoraWritableComparable.class, true);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int compare(WritableComparable w1, WritableComparable w2) {
		KeyFechaHoraWritableComparable cin1 = (KeyFechaHoraWritableComparable) w1;
		KeyFechaHoraWritableComparable cin2 = (KeyFechaHoraWritableComparable) w2;
		return (cin1.getFecha()).compareTo(cin2.getFecha());
	}

}
