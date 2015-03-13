package com.utad.cassandra.basic;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.serializers.StringSerializer;
import com.utad.cassandra.util.Utils;


public class UseDataTypes3 {

	// Leemos los datos del column family users2, que tiene como key un entero, y los representamos como String.
	public static void main(String args[]) throws Exception {
		String keyspaceName = "utad";
		String columnFamilyName = "users2";
		String rowKeyUsersById = "usersById";
		
		Keyspace ksUsers = Utils.getKeyspace(keyspaceName);

		// tipos para el
		// 1. row key
		// 2. column key
		ColumnFamily<String, String> cfUsers = new ColumnFamily<String, String>(columnFamilyName, StringSerializer.get(), StringSerializer.get());

		ColumnList<String> result = ksUsers.prepareQuery(cfUsers).getKey(rowKeyUsersById).execute().getResult();
		
		if (!result.isEmpty()) {
			for (int i = 0; i < result.size(); i++) {
				String value = result.getColumnByIndex(i).getStringValue();
				String key = result.getColumnByIndex(i).getName();
				System.out.println("email for user " + key + " is: " + value);
			}
		}
	}
}
