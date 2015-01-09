package org.utad.analisisLogs.jobSecondarySort;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public  class IdNumComparator extends WritableComparator {
	protected IdNumComparator() {
		super(FechaHoraNumWritableComparable.class, true);
	}

	@Override
	public int compare(WritableComparable w1, WritableComparable w2) {
		FechaHoraNumWritableComparable cin1 = (FechaHoraNumWritableComparable) w1;
		FechaHoraNumWritableComparable cin2 = (FechaHoraNumWritableComparable) w2;
		
		int cmp = cin1.getFechaHora().compareTo(cin2.getFechaHora());
		if (cmp != 0) {
			return cmp;
		}
		// negative because we want most popular songs first
		return -(cin1.getNum()).compareTo(cin2.getNum());
	}
}
