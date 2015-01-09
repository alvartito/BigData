package org.utad.analisisLogs.writables.comparator;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.utad.analisisLogs.writables.KeyFechaHoraWritableComparable;

/**
 * @author Álvaro Sánchez Blasco
 * 
 * */
public  class AnalisisLogsComparator extends WritableComparator {
	protected AnalisisLogsComparator() {
		super(KeyFechaHoraWritableComparable.class, true);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int compare(WritableComparable w1, WritableComparable w2) {
		KeyFechaHoraWritableComparable cin1 = (KeyFechaHoraWritableComparable) w1;
		KeyFechaHoraWritableComparable cin2 = (KeyFechaHoraWritableComparable) w2;
		
		int cmp = cin1.getFecha().compareTo(cin2.getFecha());
		if (cmp != 0) {
			return cmp;
		}
		return -(cin1.getHora()).compareTo(cin2.getHora());
	}
}
