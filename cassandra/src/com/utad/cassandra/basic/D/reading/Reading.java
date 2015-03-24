package com.utad.cassandra.basic.D.reading;

import java.util.ArrayList;
import java.util.List;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.query.RowQuery;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.util.RangeBuilder;
import com.utad.cassandra.util.Utils;

public class Reading {

	private static final String keySpaceName = "utad";

	private static final String rowKey = "usersById";

	private static final String columnFamilyName = "users2";

	private static Keyspace ksUsers = null;

	private static ColumnFamily<String, String> cfUsers = null;

	public static void main(String args[]) throws ConnectionException {

		ksUsers = Utils.getKeyspace(keySpaceName);
		cfUsers = new ColumnFamily<String, String>(columnFamilyName,
				StringSerializer.get(), StringSerializer.get());

		caso1();
		caso2();
		caso3();
		caso4();
		caso5();
		caso6();
		caso7();

	}

	/**
	 * Leer los primeros 20 usuarios.
	 * 
	 * @throws ConnectionException
	 * */
	private static void caso1() throws ConnectionException {
		RowQuery<String, String> query = ksUsers.prepareQuery(cfUsers)
				.getKey(rowKey)
				.withColumnRange(new RangeBuilder().setLimit(20).build());
		System.out.println("\nLeer los primeros 20 usuarios.");
		for (Column<String> c : query.execute().getResult()) {
			System.out.println("Email for user " + c.getName() + " is "
					+ c.getStringValue());
		}
	}

	/**
	 * Leer todos los usuarios a partir del usuario con id 50.
	 * 
	 * @throws ConnectionException
	 */
	private static void caso2() throws ConnectionException {
		RowQuery<String, String> query = ksUsers.prepareQuery(cfUsers)
				.getKey(rowKey)
				.withColumnRange(new RangeBuilder().setStart("50").build());
		System.out
				.println("\nLeer todos los usuarios a partir del usuario con id 50.");
		for (Column<String> c : query.execute().getResult()) {
			System.out.println("Email for user " + c.getName() + " is "
					+ c.getStringValue());
		}
	}

	/**
	 * Leer todos los usuarios desde el principio hasta el 50
	 * 
	 * @throws ConnectionException
	 * */
	private static void caso3() throws ConnectionException {
		RowQuery<String, String> query = ksUsers.prepareQuery(cfUsers)
				.getKey(rowKey)
				.withColumnRange(new RangeBuilder().setEnd("50").build());
		System.out
				.println("\nLeer todos los usuarios desde el principio hasta el 50.");
		for (Column<String> c : query.execute().getResult()) {
			System.out.println("Email for user " + c.getName() + " is "
					+ c.getStringValue());
		}
	}

	/**
	 * Leer los usuarios entre el 50 y el 60.
	 * 
	 * @throws ConnectionException
	 * */
	private static void caso4() throws ConnectionException {
		RowQuery<String, String> query = ksUsers
				.prepareQuery(cfUsers)
				.getKey(rowKey)
				.withColumnRange(
						new RangeBuilder().setStart("50").setEnd("60").build());
		System.out.println("\nLeer todos los usuarios entre el 50 y el 60.");
		for (Column<String> c : query.execute().getResult()) {
			System.out.println("Email for user " + c.getName() + " is "
					+ c.getStringValue());
		}
	}

	/**
	 * Leer los usuarios entre el 50 y el 60, pero no más de 8
	 * 
	 * @throws ConnectionException
	 * */
	private static void caso5() throws ConnectionException {
		RowQuery<String, String> query = ksUsers
				.prepareQuery(cfUsers)
				.getKey(rowKey)
				.withColumnRange(
						new RangeBuilder().setStart("50").setEnd("60")
								.setLimit(8).build());
		System.out
				.println("\nLeer todos los usuarios entre el 50 y el 60, pero no más de 8.");
		for (Column<String> c : query.execute().getResult()) {
			System.out.println("Email for user " + c.getName() + " is "
					+ c.getStringValue());
		}
	}

	/**
	 * Lista de usuarios que queremos filtrar.
	 * */
	private static List<String> getListaUsuariosFiltro() {
		// Lista de usuarios que queremos filtrar
		List<String> ids = new ArrayList<String>();
		ids.add("1");
		ids.add("3");
		ids.add("5");
		ids.add("7");
		ids.add("11");
		return ids;
	}

	/**
	 * Leer los usuarios 1, 3, 5, 7 y 11.
	 * <p>
	 * Uso de slides en el filtro.
	 * 
	 * @throws ConnectionException
	 * */
	private static void caso6() throws ConnectionException {

		RowQuery<String, String> query = ksUsers.prepareQuery(cfUsers)
				.getKey(rowKey).withColumnSlice(getListaUsuariosFiltro());
		System.out.println("\nLeer los usuarios 1, 3, 5, 7 y 11.");
		for (Column<String> c : query.execute().getResult()) {
			System.out.println("Email for user " + c.getName() + " is "
					+ c.getStringValue());
		}
	}

	/**
	 * Leer los usuarios 1, 3, 5, 7 y 11, pero sólo a partir del usuario 5.
	 * 
	 * @throws ConnectionException
	 * */
	private static void caso7() throws ConnectionException {

		List<String> ids = getListaUsuariosFiltro();

		// Eliminar de la lista los valores que no nos interesan
		List<String> aux = new ArrayList<String>();
		for (String s : ids) {
			if (Integer.valueOf(s) >= 5)
				aux.add(s);
		}

		RowQuery<String, String> query2 = ksUsers.prepareQuery(cfUsers)
				.getKey(rowKey).withColumnSlice(aux);
		System.out
				.println("\nLeer los usuarios 1, 3, 5, 7 y 11, pero sólo a partir del usuario 5.");
		for (Column<String> c : query2.execute().getResult()) {
			System.out.println("Email for user " + c.getName() + " is "
					+ c.getStringValue());
		}
	}

}
