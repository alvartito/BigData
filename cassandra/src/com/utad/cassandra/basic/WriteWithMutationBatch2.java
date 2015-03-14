package com.utad.cassandra.basic;

import java.util.Date;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.StringSerializer;
import com.utad.cassandra.util.Utils;

public class WriteWithMutationBatch2 {

	public static void main(String args[]) throws ConnectionException {

		// Conectamos y usamos un keyspace. Normalmente se usará un keyspace por
		// aplicación
		String keyspaceName = "utad";
		String columnFamilyName = "users";
		String rowKeyUsersById = "usersById";
		
		Keyspace ksUsers = Utils.getKeyspace(keyspaceName);

		ColumnFamily<String, String> cfUsers = new ColumnFamily<String, String>(columnFamilyName, StringSerializer.get(), StringSerializer.get());

		// escribimos de uno en uno
		System.out.println("empezando a escribir ... " + new Date());
		for (int i = 0; i <= 100000; i++) {
			// Escribir un valor en Cassandra
			ksUsers.prepareColumnMutation(cfUsers, rowKeyUsersById, i + "").putValue("user" + i + "@void.com", null).execute();
		}
		System.out.println("terminado!" + new Date());

		// leemos los resultados de uno en uno
		System.out.println("empezando a leer ..." + new Date());

		for (int i = 0; i <= 100000; i++) {
			Column<String> result = ksUsers.prepareQuery(cfUsers).getKey(rowKeyUsersById).getColumn(i + "").execute().getResult();
			String value = result.getStringValue();
			if (i % 100 == 0) {
				System.out.println("email for user " + i + " is: " + value);
			}
		}

		System.out.println("terminado!" + new Date());
		
		/**
		 * Resultado de la ejecución
		 * <p>
		 * empezando a escribir ... Fri Mar 13 09:22:03 PDT 2015
		 * <p>
		 * terminado!Fri Mar 13 09:22:12 PDT 2015
		 * <p>
		 * empezando a leer ...Fri Mar 13 09:22:12 PDT 2015
		 * <p>
		 * terminado!Fri Mar 13 09:22:20 PDT 2015
		 * 
		 * */

	}
}
