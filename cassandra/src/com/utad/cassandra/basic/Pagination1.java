package com.utad.cassandra.basic;

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

public class Pagination1 {
	public static void main(String args[]) throws ConnectionException {

		String keyspaceName = "utad";
		String columnFamilyName = "users";
		String rowKeyUsersById = "usersById";

		int pageSize = 10000;

		Keyspace ksUsers = Utils.getKeyspace(keyspaceName);

		ksUsers.dropColumnFamily(columnFamilyName);

		ColumnFamily<String, Integer> cfUsers = new ColumnFamily<String, Integer>(columnFamilyName, StringSerializer.get(), IntegerSerializer.get());

		ksUsers.createColumnFamily(cfUsers, ImmutableMap.<String, Object> builder().put("key_validation_class", "IntegerType").put("comparator_type", "IntegerType").build());

		// crear mutation
		MutationBatch m = ksUsers.prepareMutationBatch();
		ColumnListMutation<Integer> clm = m.withRow(cfUsers, rowKeyUsersById);
		for (int i = 1; i <= 1000000; i++) {

			// escribir en el mutation
			clm.putColumn(i, "user" + i + "@void.com");

			// contador%tamaño_pag==0 -> execute
			if (i % pageSize == 0) {
				// si hemos completado una página, ejecutar el mutation
				// m.withRow(cfUsers, rowKeyUsersById);
				m.execute();
				clm = m.withRow(cfUsers, rowKeyUsersById);
				// y volver a crear un mutation de nuevo! si no, la siguiente vez
				// volverá a escribir los datos anteriores
			}
		}
		m.execute();

		// ejecutar el mutation una última vez para asegurar la escritura de
		// la última página

		System.out.println("finished writing");

		// leer los datos
		ColumnList<Integer> result = ksUsers.prepareQuery(cfUsers).getKey(rowKeyUsersById).execute().getResult();
		if (!result.isEmpty()) {
			for (int i = 0; i < result.size(); i++) {
				String value = result.getColumnByIndex(i).getStringValue();
				if (i % 10000 == 0) {
					System.out.println("email for user " + i + " is: " + value);
				}
			}
		}
		System.out.println("finished reading paginated values");
	}
}
