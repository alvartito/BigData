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

public class Pagination2 {
	public static void main(String args[]) throws ConnectionException {

		int pageSize = 10000;

		String keyspaceName = "utad";
		String columnFamilyName = "users";
		String rowKeyUsersById = "usersById";

		Keyspace ksUsers = Utils.getKeyspace(keyspaceName);

		ColumnFamily<String, String> cfUsers = new ColumnFamily<String, String>(columnFamilyName, StringSerializer.get(), StringSerializer.get());

		/*
		 * Para evitar errores de lectura, añadimos .autoPaginate(true) a la consulta.
		 */
		RowQuery<String, String> query = ksUsers.prepareQuery(cfUsers).getKey(rowKeyUsersById).autoPaginate(true).withColumnRange(new RangeBuilder().setLimit(pageSize).build());

		ColumnList<String> columns = query.execute().getResult();

		while (!(columns = query.execute().getResult()).isEmpty()) {
			for (Column<String> column : columns) {
				System.out.println("email for user " + column.getName() + " is: " + column.getStringValue());
			}
		}

		System.out.println("finished reading paginated values");
	}
}
