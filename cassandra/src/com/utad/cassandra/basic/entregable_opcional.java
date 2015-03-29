package com.utad.cassandra.basic;

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
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
import com.utad.cassandra.util.Utils;

/**
 * Ejercicios opcionales de Cassandra.
 * 
 * @author Álvaro Sánchez Blasco.
 */
public class entregable_opcional {

	private static List<User> users = new ArrayList<User>();


	private static String addres = "Alcala";
	private static String cpCode = "28001";

	
	public static void main(String args[]) throws ConnectionException {

		String[] calles_28001 = { "Alcala", "Preciados", "Gran Via", "Princesa" };
		String[] calles_28002 = { "Castellana", "Goya", "Serrano", "Velazquez" };

		int index_28001 = 0;
		int index_28002 = 0;

		for (int i = 0; i < 100; i++) {

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

		compositeKeys();
		usigJDriver();

	}

	private static void usigJDriver() {

		System.out.println("\n\n Using JDriver Opcional");

		// conectar y crear column family
		Cluster cluster;
		String node = "127.0.0.1";

		cluster = Cluster.builder().addContactPoint(node).build();

		Session session;
		session = cluster.connect();

		session.execute("drop keyspace utad_cql");

		session.execute("CREATE KEYSPACE utad_cql WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};");

		// Creo la tabla con la PK compuesta por CP, Calle, e Id Usuario, para
		// asegurarme que cada registro que inserto es único (lo discrimino por
		// id_usuario)
		session.execute("CREATE TABLE utad_cql.usersByCPAddress (id_usuario int, cp int, nombre text, email text, calle text, primary key(cp, calle, id_usuario));");

		PreparedStatement ps1 = session
				.prepare("Insert into utad_cql.usersByCPAddress(id_usuario, cp, nombre, email, calle) values (?, ?, ?, ?, ?)");

		BatchStatement batch = new BatchStatement();

		// Realizamos las inserciones en BD
		System.out.println("\nJDriver - Escribir datos.");
		for (User user : users) {
			int id = Integer.parseInt(user.id);
			int cp = Integer.parseInt(user.cp);
			String nombre = user.nombre;
			String email = user.email;
			String calle = user.calle;

			batch.add(ps1.bind(id, cp, nombre, email, calle));
		}

		session.execute(batch);
		System.out.println("\nRegistros insertados en Cassandra");

		// Muestro el resultado correspondiente, pero comento los campos que no
		// se piden en el resultado, para confirmar, descomentandolos, que los
		// datos son los correctos. Limitamos la muestra resultado a 20
		// registros.
		StringBuilder sbQuery = new StringBuilder();
		sbQuery.append("select id_usuario, nombre, email ");
		// sbQuery.append(",cp, calle  ");
		sbQuery.append("from utad_cql.usersByCPAddress where calle='").append(addres);
		sbQuery.append("' and cp=").append(cpCode);
		sbQuery.append(" limit 20");

		ResultSet results = session.execute(sbQuery.toString());

		// Leemos los datos recuperados.
		System.out.println("\nJDriver - Leer el resultado");
		for (Row row : results) {
			System.out.println("\nid : " + row.getInt("id_usuario"));
			System.out.println("nombre : " + row.getString("nombre"));
			System.out.println("email : " + row.getString("email"));
			// System.out.println("cp : " + row.getInt("cp"));
			// System.out.println("calle : " + row.getString("calle"));
		}

		cluster.close();
		System.out.println("\nFin de la ejecución");
	}

	private static void compositeKeys() throws ConnectionException {
		System.out.println("\n\n Composite Keys Opcional");

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

		System.out.println("\nComposite Keys - Escribimos los datos");
		for (User user : users) {
			String id = user.id;
			String cp = user.cp;
			String nombre = user.nombre;
			String email = user.email;
			String calle = user.calle;

			// escribir
			String key = cp + ":" + calle + ":" + id;
			String value = id + ":" + nombre + ":" + email;
			clm.putColumn(key, value);
			ksUsers.prepareColumnMutation(cfUsers, rowKey, key).putValue(value, null).execute();
		}

		// leer el resultado
		System.out.println("\nComposite Keys - Leer el resultado");
		RowQuery<String, String> query = ksUsers.prepareQuery(cfUsers).getKey(rowKey)
				.withColumnRange(new RangeBuilder().setStart(cpCode+":"+addres).setEnd(cpCode+":"+addres+"~").build()).autoPaginate(true);

		ColumnList<String> columns = query.execute().getResult();

		for (Column<String> c : columns) {
			String key = c.getName();
			String value = c.getStringValue();

			System.out.println("\nclave");
			String[] sKey = key.split(":");
				System.out.println("\tcp: " + sKey[0]);
				System.out.println("\tcalle: " + sKey[1]);
				System.out.println("\tid_usuario: " + sKey[2]);

			System.out.println("valor");
			String[] sValue = value.split(":");
				System.out.println("\tid_usuario: " + sValue[0]);
				System.out.println("\tnombre: " + sValue[1]);
				System.out.println("\temail: " + sValue[2]);

		}
	}
}
