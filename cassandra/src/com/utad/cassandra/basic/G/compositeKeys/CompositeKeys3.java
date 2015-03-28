package com.utad.cassandra.basic.G.compositeKeys;

import java.util.ArrayList;
import java.util.List;

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
import com.netflix.astyanax.util.RangeBuilder;
import com.utad.cassandra.basic.User;
import com.utad.cassandra.util.Utils;

/**
 * Claves Compuestas 3 Diseñar e implementar un column family para buscar
 * usuarios a partir de su código postal y nombre de la calle de residencia.
 * <p>
 * Los datos a obtener son, para el CP 28001 y la calle Gran Via:
 * <li>Id del usuario
 * <li>email
 * <li>Nombre
 */
public class CompositeKeys3 {

	public static void main(String args[]) throws ConnectionException {

		String[] calles_28001 = { "Alcala", "Preciados", "Gran Via", "Princesa" };
		String[] calles_28002 = { "Castellana", "Goya", "Serrano", "Velazquez" };

		int index_28001 = 0;
		int index_28002 = 0;

		List<User> users = new ArrayList<User>();

		for (int i = 0; i < 10; i++) {

			String id = (i + 1) + "";
			String email = "user" + id + "@void.com";
			String nombre = "nombre_" + id;
			String cp;
			String calle;
			if (i % 2 == 0) {
				cp = "28001";
				calle = calles_28001[index_28001];
				index_28001++;
				index_28001 = index_28001 % 4;
			} else {
				cp = "28002";
				calle = calles_28002[index_28002];
				index_28002++;
				index_28002 = index_28002 % 4;
			}

			User user = new User(id, email, nombre, cp, calle);
			users.add(user);
		}

		// conectar y crear column family
		Keyspace ksUsers = Utils.getKeyspace("utad");

		String columnFamily = "compositeKeys";

		ColumnFamily<String, String> cfUsers = new ColumnFamily<String, String>(columnFamily, StringSerializer.get(),
				StringSerializer.get());

		try {
			ksUsers.dropColumnFamily(columnFamily);
		} catch (Exception e) {
			System.out.println("No existe el column family a borrar: " + columnFamily);
		}

		try {
			ksUsers.createColumnFamily(
					cfUsers,
					ImmutableMap.<String, Object> builder().put("key_validation_class", "BytesType")
							.put("comparator_type", "BytesType").build());
		} catch (Exception e) {
			System.out.println("Error creando el  column family: " + columnFamily + " " + e.getMessage());
		}

		MutationBatch m = ksUsers.prepareMutationBatch();
		String rowKey = "usersByCPAddress";

		ColumnListMutation<String> clm = m.withRow(cfUsers, rowKey);

		System.out.println("\nEscribimos los datos");
		for (User user : users) {
			String id = user.id;
			String cp = user.cp;
			String nombre = user.nombre;
			String email = user.email;
			String calle = user.calle;

			// escribir
			String key = id + ":" + cp + ":" + calle;
			String value = id + ":" + nombre + ":" + email;
			clm.putColumn(key, value);
			ksUsers.prepareColumnMutation(cfUsers, rowKey, key).putValue(value, null).execute();
		}

		// leer el resultado
		System.out.println("\nLeer el resultado");
		RowQuery<String, String> query = ksUsers.prepareQuery(cfUsers).getKey(rowKey)
				.withColumnRange(new RangeBuilder().build()).autoPaginate(true);

		ColumnList<String> columns = query.execute().getResult();

		for (Column<String> c : columns) {
			String key = c.getName();
			String value = c.getStringValue();

			System.out.println("\nclave");
			String[] ksplit = key.split(":");
			for (String string : ksplit) {
				System.out.println("\t"+string);
			}
			
			System.out.println("valor");
			String[] kvalue = value.split(":");

			for (String string : kvalue) {
				System.out.println("\t"+string);
			}
		}
	}
}
