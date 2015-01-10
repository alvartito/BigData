package alvaro.sanchez.blasco.comparator;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import alvaro.sanchez.blasco.writables.FechaHoraNumWritableComparable;

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
			//Queremos las horas más altas al principio.
			return -cmp;
		}
		//Queremos los procesos con menos apariciones al principio.
		return (cin1.getNum()).compareTo(cin2.getNum());
	}
}
