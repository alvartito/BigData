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

public class Reading4 {

	public static void main(String args[]) throws ConnectionException {
		String keyspaceName = "utad";
		String columnFamilyName = "users";
		String rowKeyUsersById = "usersById";
		
		Keyspace ksUsers = Utils.getKeyspace(keyspaceName);

		ColumnFamily<String, String> cfUsers = new ColumnFamily<String, String>(columnFamilyName, StringSerializer.get(), StringSerializer.get());

		// Si necesitamos borrar el column family
		// ksUsers.dropColumnFamily(columnFamilyName);

		RowQuery<String, String> query = ksUsers.prepareQuery(cfUsers).getKey(rowKeyUsersById).withColumnRange(new RangeBuilder().setStart("50").setEnd("60").build());

		ColumnList<String> columns = query.execute().getResult();

		for (Column<String> column : columns) {
			System.out.println("Email for user " + column.getName() + " is: " + column.getStringValue());
		}
	}
}
