package com.utad.cassandra.basic.G.compositeKeys;

import com.google.common.collect.ImmutableMap;
import com.netflix.astyanax.ColumnListMutation;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.query.RowQuery;
import com.netflix.astyanax.serializers.StringSerializer;
import com.utad.cassandra.util.Utils;

public class CompositeKeys1 {

	private static final String keySpaceName = "utad";
	private static final String rowKey = "usersById";
	private static final String columnFamilyName = "UserVisitsProduct2";

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

		Keyspace ksUsers = Utils.getKeyspace(keySpaceName);

		// ksUsers.dropColumnFamily("UserVisitsProduct2");

		ColumnFamily<String, String> cfUsers = new ColumnFamily<String, String>(
				columnFamilyName, StringSerializer.get(),
				StringSerializer.get());

		// Inserción de los datos en Cassandra
		try {
			ksUsers.createColumnFamily(
					cfUsers,
					ImmutableMap
							.<String, Object> builder()
							.put("default_validation_class",
									"CounterColumnType")
							.put("replicate_on_write", true).build());

			MutationBatch m = ksUsers.prepareMutationBatch();

			ColumnListMutation<String> clm = m.withRow(cfUsers, rowKey);

			for (int i = 0; i < userVisitsProduct.length; i++) {
				String user = (i + 1) + "";
				for (String p : userVisitsProduct[i]) {
					String key = user + ":" + p;
					clm.incrementCounterColumn(key, 1);
				}
			}
			m.execute();
		} catch (Exception e) {
			System.out
					.println("Ya existe el column family " + columnFamilyName);
		}

		// Lectura de datos insertados.
		RowQuery<String, String> query = ksUsers.prepareQuery(cfUsers)
				.getKey(rowKey).autoPaginate(true);

		ColumnList<String> columns = query.execute().getResult();

		for (Column<String> c : columns) {
			String key = c.getName();
			Long value = c.getLongValue();

			String user = key.split(":")[0];
			String prod = key.split(":")[1];

			System.out.println("user " + user + " has visited product " + prod
					+ " " + value + " times");
		}

	}
}
