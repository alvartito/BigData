package com.utad.cassandra.java_driver;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

public class UsingJDriver {

	public static void main(String args[]) {

		Cluster cluster;
		String node = "127.0.0.1";

		cluster = Cluster.builder().addContactPoint(node).build();

		Session session;
		session = cluster.connect();

		// Eliminar el keyspace para evitar excepciones en ejecución si este
		// existe ya.
		// Otra solución sería rodear las sentencias de create con un try catch
		// y controlar la excepción, si llega a saltar.
		session.execute("drop keyspace utad_cql");
		// try {
		// ejecutamos una query directamente, en este caso, para crear el
		// keyspace
		session.execute("CREATE KEYSPACE utad_cql WITH replication "
				+ "= {'class':'SimpleStrategy', 'replication_factor':1};");

		session.execute("CREATE TABLE utad_cql.users (" + "id int,"
				+ "email text, nombre text, apellido text, "
				+ "PRIMARY KEY (id, email, nombre));");
		// } catch (Exception e) {
		// System.out.println("ya existen keyspace y table");
		// }

		// podemos usar prepared statements
		PreparedStatement ps1 = session
				.prepare("Insert into utad_cql.users(id, email, nombre, apellido) values (?, ?, ?, ?)");

		// y por supuesto, escrituras en batch
		BatchStatement batch = new BatchStatement();

		for (int i = 0; i < 1000; i++) {
			batch.add(ps1.bind(i, "user" + i + "@void.com", "nombre_" + i,
					"apellido_" + i));
		}

		session.execute(batch);

		ResultSet results = session.execute("SELECT * FROM utad_cql.users");

		for (Row row : results) {
			System.out.println("id : " + row.getInt("id"));
			System.out.println("email : " + row.getString("email"));
			System.out.println("nombre : " + row.getString("nombre"));
			System.out.println("apellido : " + row.getString("apellido"));

		}
		System.out.println();

		cluster.close();
	}

}
