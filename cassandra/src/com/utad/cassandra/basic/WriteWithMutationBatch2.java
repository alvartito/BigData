package com.utad.cassandra.basic;

import java.util.Date;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.StringSerializer;
import com.utad.cassandra.util.Constantes;
import com.utad.cassandra.util.Utils;

public class WriteWithMutationBatch2 {

	/**
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
	public static void main(String args[]) throws ConnectionException {

		// Conectamos y usamos un keyspace. Normalmente se usará un keyspace por
		// aplicación
		Keyspace ksUsers = Utils.getKeyspace(Constantes.keyspaceName);

		ColumnFamily<String, String> cfUsers = new ColumnFamily<String, String>(Constantes.columnFamilyName, StringSerializer.get(), StringSerializer.get());

		// escribimos de uno en uno
		System.out.println("empezando a escribir ... " + new Date());
		for (int i = 0; i <= 100000; i++) {
			// Escribir un valor en Cassandra
			ksUsers.prepareColumnMutation(cfUsers, Constantes.rowKey, i + "").putValue("user" + i + "@void.com", null).execute();
		}
		System.out.println("terminado!" + new Date());

		// leemos los resultados de uno en uno
		System.out.println("empezando a leer ..." + new Date());

		for (int i = 0; i <= 100000; i++) {
			Column<String> result = ksUsers.prepareQuery(cfUsers).getKey(Constantes.rowKey).getColumn(i + "").execute().getResult();
			String value = result.getStringValue();
			if (i % 100 == 0) {
				System.out.println("email for user " + i + " is: " + value);
			}
		}

		System.out.println("terminado!" + new Date());
	}
}
