package com.utad.cassandra.basic;

import java.util.Date;

import com.netflix.astyanax.ColumnListMutation;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.serializers.StringSerializer;
import com.utad.cassandra.util.Constantes;
import com.utad.cassandra.util.Utils;

public class WriteWithMutationBatch1 {

	/**
	 * <p>
	 * empezando a escribir ...Fri Mar 13 09:04:11 PDT 2015
	 * <p>
	 * terminado!Fri Mar 13 09:04:11 PDT 2015
	 * <p>
	 * empezando a leer ...Fri Mar 13 09:04:11 PDT 2015
	 * <p>
	 * terminado!Fri Mar 13 09:04:11 PDT 2015
	 * */

	public static void main(String args[]) throws ConnectionException {

		// Conectamos y usamos un keyspace. Normalmente se usará un keyspace por aplicación
		Keyspace ksUsers = Utils.getKeyspace(Constantes.keyspaceName);

		ColumnFamily<String, String> cfUsers = new ColumnFamily<String, String>(Constantes.columnFamilyName, StringSerializer.get(), StringSerializer.get());

		// Creamos un mutation, para escribir en batch
		// ¡Mucho más eficiente que escribir uno a uno!
		MutationBatch m = ksUsers.prepareMutationBatch();

		// Preparamos el mutation para trabajar con la partition key y el column family
		ColumnListMutation<String> clm = m.withRow(cfUsers, Constantes.rowKey);

		// Escribimos datos en el mutation
		System.out.println("empezando a escribir ..." + new Date());
		for (int i = 1; i <= 100000; i++) {
			// Escribir datos (putColumn
			clm.putColumn(i + "", "user" + i + "@void.com");
		}
		System.out.println("terminado!" + new Date());

		// Escribimos los datos del mutation en Cassandra
		m.execute();

		// Leemos los resultados y los guardamos en un objeto
		System.out.println("empezando a leer ..." + new Date());

		// Para un rowkey siempre
		ColumnList<String> result = ksUsers.prepareQuery(cfUsers).getKey(Constantes.rowKey).execute().getResult();
		System.out.println("terminado!" + new Date());

		// Iteramos por los resultados para imprimirlos
		if (!result.isEmpty()) {
			for (int i = 0; i < result.size(); i++) {
				String value = result.getColumnByIndex(i).getStringValue();
				if (i % 1000 == 0) {
					System.out.println("email for user " + result.getColumnByIndex(i).getName() + " is: " + value);
				}
			}
		}
	}
}
