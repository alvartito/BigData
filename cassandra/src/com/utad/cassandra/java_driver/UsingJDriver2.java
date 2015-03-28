package com.utad.cassandra.java_driver;

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.utad.cassandra.basic.User;

public class UsingJDriver2 {

	public static void main(String args[]) {
		String[] calles_28001 = { "Alcala", "Preciados", "Gran Via", "Princesa" };
		String[] calles_28002 = { "Castellana", "Goya", "Serrano", "Velazquez" };

		String addres = "Alcala";
		String cpCode = "28001";

		int index_28001 = 0;
		int index_28002 = 0;

		List<User> users = new ArrayList<User>();

		for (int i = 0; i < 2000; i++) {

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
		// datos son los correctos. Limitamos la muestra resultado a 20 registros.
		StringBuilder sbQuery = new StringBuilder();
		sbQuery.append("select id_usuario, nombre, email ");
		//sbQuery.append(",cp, calle  ");
		sbQuery.append("from utad_cql.usersByCPAddress where calle='").append(addres);
		sbQuery.append("' and cp=").append(cpCode);
		sbQuery.append(" limit 20");
		
		ResultSet results = session.execute(sbQuery.toString());

		// Leemos los datos recuperados.
		for (Row row : results) {
			System.out.println("\nid : " + row.getInt("id_usuario"));
			System.out.println("nombre : " + row.getString("nombre"));
			System.out.println("email : " + row.getString("email"));
//			System.out.println("cp : " + row.getInt("cp"));
//			System.out.println("calle : " + row.getString("calle"));
		}

		cluster.close();
		System.out.println("\nFin de la ejecución");
	}

}
