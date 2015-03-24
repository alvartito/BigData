package com.utad.cassandra.basic.E.pagination;

import com.google.common.collect.ImmutableMap;
import com.netflix.astyanax.ColumnListMutation;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.serializers.IntegerSerializer;
import com.netflix.astyanax.serializers.StringSerializer;
import com.utad.cassandra.util.Utils;

public class A15Pagination1 {
	public static void main(String args[]) throws ConnectionException {

		int pageSize = 10000;
		
		Keyspace ksUsers = Utils.getKeyspace("utad");
		
		ksUsers.dropColumnFamily("users");
		
		ColumnFamily<String, Integer> cfUsers = new ColumnFamily<String, Integer>(
				"users", StringSerializer.get(), IntegerSerializer.get());
		
		ksUsers.createColumnFamily(
				cfUsers,
				ImmutableMap.<String, Object> builder()
						.put("key_validation_class", "IntegerType")
						.put("comparator_type", "IntegerType").build());
		
		MutationBatch m = ksUsers.prepareMutationBatch();
		String rowKey = "usersById";
		ColumnListMutation<Integer> clm = m.withRow(cfUsers, rowKey);
		// crear mutation
		
		for (int i = 1; i <= 5000000; i++) {
			
			// escribir en el mutation			
			clm.putColumn(i, "user" + i + "@void.com");
			
			if (i % pageSize == 0) {
				m.execute();
				clm = m.withRow(cfUsers, rowKey);
				// si hemos completado una página, ejecutar el mutation
				
				// y volver a crear un mutation de nuevo! si no, la siguiente vez
				// volverá a escribir los datos anteriores
			}
		}
		
		m.execute();
		
		// ejecutar el mutation una última vez para asegurar la escritura de
		// la última página

		System.out.println("finished writing");

		// leer los datos
		

		ColumnList<Integer> result = ksUsers.prepareQuery(cfUsers)
				.getKey(rowKey).execute().getResult();
		if (!result.isEmpty()) {
			for (int i = 0; i < result.size(); i++) {
				String value = result.getColumnByIndex(i).getStringValue();
				// System.out.println("email for user " + i + " is: " + value);
			}
		}

		System.out.println("finished reading paginated values");
	}
}
