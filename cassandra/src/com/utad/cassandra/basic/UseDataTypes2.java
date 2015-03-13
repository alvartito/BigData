package com.utad.cassandra.basic;

import com.google.common.collect.ImmutableMap;
import com.netflix.astyanax.ColumnListMutation;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.serializers.IntegerSerializer;
import com.netflix.astyanax.serializers.StringSerializer;
import com.utad.cassandra.util.Utils;

public class UseDataTypes2 {

	public static void main(String args[]) throws Exception {
		String keyspaceName = "utad";
		String columnFamilyName = "users2";
		String rowKeyUsersById = "usersById";
		
		Keyspace ksUsers = Utils.getKeyspace(keyspaceName);

		// tipos para el
		// 1. row key
		// 2. column key
		ColumnFamily<String, Integer> cfUsers = new ColumnFamily<String, Integer>(columnFamilyName, StringSerializer.get(), IntegerSerializer.get());

		try {
			ksUsers.createColumnFamily(cfUsers, ImmutableMap.<String, Object> builder()
			// cómo almacenará cassandra internamente las column keys
					.put("key_validation_class", "BytesType")
					// cómo se ordenarán las column keys
					.put("comparator_type", "BytesType").build());

			MutationBatch m = ksUsers.prepareMutationBatch();

			ColumnListMutation<Integer> clm = m.withRow(cfUsers, rowKeyUsersById);

			for (int i = 0; i < 20; i++) {
				clm.putColumn(i, "user" + i + "@void.com");
			}

			m.execute();

		} catch (Exception e) {
			System.out.println("ya existe el column family "+columnFamilyName);
		}

		ColumnList<Integer> result = ksUsers.prepareQuery(cfUsers).getKey(rowKeyUsersById).execute().getResult();
		if (!result.isEmpty()) {
			for (int i = 0; i < result.size(); i++) {
				String value = result.getColumnByIndex(i).getStringValue();
				System.out.println("email for user " + result.getColumnByIndex(i).getName() + " is: " + value);
			}
		}
	}
}
