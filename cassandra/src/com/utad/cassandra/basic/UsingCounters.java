package com.utad.cassandra.basic;

import java.util.Date;

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

public class UsingCounters {
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

		String[][] userVisitsProduct = { products1, products2, products3, products4, products5 };

		String keyspaceName = "utad";
		String columnFamilyName = "users_visits_product";
		//Para consultas
		String rowKeyUsersById = "usersById";

		// conectar
		Keyspace ksUsers = Utils.getKeyspace(keyspaceName);

		ksUsers.dropColumnFamily(columnFamilyName);

		ColumnFamily<String, String> cfUsers = new ColumnFamily<String, String>(columnFamilyName, StringSerializer.get(), StringSerializer.get());

		ksUsers.createColumnFamily(cfUsers, ImmutableMap.<String, Object> builder().put("default_validation_class", "CounterColumnType").put("replicate_on_write", true).build());

		
		MutationBatch m = ksUsers.prepareMutationBatch();
		// escribimos de uno en uno
		System.out.println("empezando a escribir ... " + new Date());

		for (int i = 0; i < userVisitsProduct.length; i++) {
			String user = (i + 1) + "";
			String rowKey = user;
			ColumnListMutation<String> clm = m.withRow(cfUsers, rowKey);
			for(String product : userVisitsProduct[i]) {
				String key = product;
				clm.incrementCounterColumn(key, 1);
			}
			m.execute();
		}
		System.out.println("Fin de la ejecucion " + new Date());
		
		//Leemos los datos
		ColumnList<String> columns;

		RowQuery<String, String> query = ksUsers.prepareQuery(cfUsers).getKey(rowKeyUsersById).autoPaginate(true);

		columns = query.execute().getResult();
		for (Column<String> column : columns) {
			String key = column.getName();
			String value = column.getStringValue();
			
//			String user = key.split(":")[0];
//			String product = key.split(":")[1];
			
			System.out.println("user "+key+" visited product "+key+" "+value+" times");
		}
	}
}
