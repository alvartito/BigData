package com.utad.cassandra.basic;

import com.google.common.collect.ImmutableMap;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.query.RowQuery;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.util.RangeBuilder;
import com.utad.cassandra.util.Utils;

public class CompositeKeys2 {
	public static void main(String args[]) throws ConnectionException {
		String keyspaceName = "utad";
		String columnFamilyName = "users_visits_product";
		// Para consultas
		String rowKeyUsersById = "usersById";

		// conectar
		Keyspace ksUsers = Utils.getKeyspace(keyspaceName);

		// ksUsers.dropColumnFamily(columnFamilyName);

		ColumnFamily<String, String> cfUsers = new ColumnFamily<String, String>(columnFamilyName, StringSerializer.get(), StringSerializer.get());

		try {
			//Rodeamos con un try catch para evitar errores si el columnFamily ya existe.
			ksUsers.createColumnFamily(cfUsers, ImmutableMap.<String, Object> builder().put("default_validation_class", "CounterColumnType").put("replicate_on_write", true).build());
		} catch (Exception e) {
			System.out.println("El column family "+ columnFamilyName + " ya existe.");
		}
		
		// Leemos los datos
		ColumnList<String> columns;

		RowQuery<String, String> query = ksUsers.prepareQuery(cfUsers).getKey(rowKeyUsersById).autoPaginate(true).withColumnRange(new RangeBuilder().setStart("3:\u00000").setEnd("3:\uffff").build());
		//Esta solución de límites es más correcta que la propuesta en la consulta anterior
		RowQuery<String, String> query2 = ksUsers.prepareQuery(cfUsers).getKey(rowKeyUsersById).autoPaginate(true).withColumnRange(new RangeBuilder().setStart("3:").setEnd("3:~").build());

		columns = query.execute().getResult();
		for (Column<String> column : columns) {
			String key = column.getName();
			Long value = column.getLongValue();

			String user = key.split(":")[0];
			String product = key.split(":")[1];

			System.out.println("user " + user + " visited product " + product + " " + value + " times");
		}
	}
}
