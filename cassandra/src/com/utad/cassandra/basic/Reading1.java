package com.utad.cassandra.basic;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.query.RowQuery;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.util.RangeBuilder;
import com.utad.cassandra.util.Utils;

public class Reading1 {

	public static void main(String args[]) throws ConnectionException {

		String keyspaceName = "utad";
		String columnFamilyName = "users";
		String rowKeyUsersById = "usersById";
		
		Keyspace ksUsers = Utils.getKeyspace(keyspaceName);

		ColumnFamily<String, String> cfUsers = new ColumnFamily<String, String>(columnFamilyName, StringSerializer.get(), StringSerializer.get());

		// Si necesitamos borrar el column family
		// ksUsers.dropColumnFamily(columnFamilyName);

		/*
		 * A una RowQuery se le puede dar un inicio, un nº de elementos a buscar, un final, y un conjunto de claves. Se pueden combinar. Realizamos una consulta sobre la rowKey, y le decimos que sólo
		 * lea 20 elementos. Como no tiene inicio ni fin, asumimos que comienza por el principio del row, sea el que sea.
		 */
		RowQuery<String, String> query = ksUsers.prepareQuery(cfUsers).getKey(rowKeyUsersById).withColumnRange(new RangeBuilder().setLimit(20).build());

		ColumnList<String> columns = query.execute().getResult();
		int i=1;
		for (Column<String> column : columns) {
			System.out.println(i+" Email for user " + column.getName() + " is: " + column.getStringValue());
			i++;
		}
	}
}
