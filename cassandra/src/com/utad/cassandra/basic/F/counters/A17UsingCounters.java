package com.utad.cassandra.basic.F.counters;

import com.google.common.collect.ImmutableMap;
import com.netflix.astyanax.ColumnListMutation;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.query.RowQuery;
import com.netflix.astyanax.serializers.StringSerializer;
import com.utad.cassandra.util.Utils;

public class A17UsingCounters {

	private static final String keySpaceName = "utad";
	private static final String columnFamilyName = "UserVisitsProduct";

	public static void main(String args[]) throws ConnectionException {

		// productos visitados por el usuario 1
		String[] products1 = { "1", "2", "3", "1", "2" };
		// productos visitados por el usuario 2
		String[] products2 = { "1", "1", "3", "1", "6" };
		// productos visitados por el usuario 3
		String[] products3 = { "1", "2", "5", "7", "8" };
		// productos visitados por el usuario 4
		String[] products4 = { "1", "2", "2", "8", "9" };
		// productos visitados por el usuario 5
		String[] products5 = { "4", "5", "6", "7", "7" };

		String[][] userVisitsProduct = { products1, products2, products3,
				products4, products5 };

		// conectar
		Keyspace ksUsers = Utils.getKeyspace(keySpaceName);

		ksUsers.dropColumnFamily(columnFamilyName);

		ColumnFamily<String, String> cfUsers = new ColumnFamily<String, String>(
				columnFamilyName, StringSerializer.get(),
				StringSerializer.get());

		ksUsers.createColumnFamily(
				cfUsers,
				ImmutableMap.<String, Object> builder()
						.put("default_validation_class", "CounterColumnType")
						.put("key_validation_class", "IntegerType")
						.put("comparator_type", "IntegerType").build());

		for (int i = 0; i < userVisitsProduct.length; i++) {
			String user = (i + 1) + "";

			// Usaremos rowKey = id del usuario
			String rowKey = user;

			// escribir los datos en el row key
			MutationBatch m = ksUsers.prepareMutationBatch();
			ColumnListMutation<String> clm = m.withRow(cfUsers, rowKey);
			clm = m.withRow(cfUsers, rowKey);
			for (String p : userVisitsProduct[i]) {
				String key = p;
				clm.incrementCounterColumn(key, 1);
			}

			m.execute();

			// leer e imprimir los datos del row key
			RowQuery<String, String> query = ksUsers.prepareQuery(cfUsers)
					.getKey(rowKey);

			for (Column<String> c : query.execute().getResult()) {
				System.out.println("user " + rowKey + " has visited product "
						+ c.getName() + " " + c.getLongValue() + " times");
			}
		}
	}
}
