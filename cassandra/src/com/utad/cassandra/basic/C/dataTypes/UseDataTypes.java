package com.utad.cassandra.basic.C.dataTypes;

import com.google.common.collect.ImmutableMap;
import com.netflix.astyanax.ColumnListMutation;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.serializers.IntegerSerializer;
import com.netflix.astyanax.serializers.StringSerializer;
import com.utad.cassandra.util.Utils;

public class UseDataTypes {

	private static final String keySpaceName = "utad";
	private static final String columnFamilyName2 = "users2";
	private static final String columnFamilyName3 = "users3";
	private static final String rowKey = "usersById";
	private static Keyspace ksUsers = null;
	private static ColumnFamily<String, String> cfUsers = null;

	public static void main(String args[]) throws Exception {

		ksUsers = Utils.getKeyspace(keySpaceName);

		caso1();
		caso2();
		caso3();

	}

	/**
	 * Caso 1
	 * <p>
	 * crear el column family usando tipo Bytes en la ordenaci�n de las column
	 * keys
	 * 
	 * @throws ConnectionException
	 * */
	private static void caso1() throws ConnectionException {
		// tipos para el
		// 1. row key
		// 2. column key
		cfUsers = new ColumnFamily<String, String>(columnFamilyName2,
				StringSerializer.get(), StringSerializer.get());

		ksUsers.dropColumnFamily(columnFamilyName2);
		
		try {
			ksUsers.createColumnFamily(cfUsers, ImmutableMap
					.<String, Object> builder()
					// cómo almacenará cassandra internamente las column keys
					.put("key_validation_class", "BytesType")
					// cómo se ordenarán las column keys
					.put("comparator_type", "BytesType").build());

			MutationBatch m = ksUsers.prepareMutationBatch();

			ColumnListMutation<String> clm = m.withRow(cfUsers, rowKey);

			for (int i = 0; i < 20; i++) {
				clm.putColumn(i + "", "user" + i + "@void.com");
			}

			m.execute();

		} catch (Exception e) {
			System.out.println("ya existe el column family "
					+ columnFamilyName2);
		}
		ColumnList<String> result = ksUsers.prepareQuery(cfUsers)
				.getKey(rowKey).execute().getResult();
		if (!result.isEmpty()) {
			for (int i = 0; i < result.size(); i++) {
				String value = result.getColumnByIndex(i).getStringValue();
				System.out.println("email for user "
						+ result.getColumnByIndex(i).getName() + " is: "
						+ value);
			}
		}
	}

	/**
	 * Caso 2
	 * <p>
	 * Adaptar el ejemplo anterior para crear el column family users3 usando:
	 * <p>
	 * String como row key
	 * <p>
	 * Integer como column key
	 * <p>
	 * IntegerType como key_validation_class
	 * <p>
	 * IntegerType como comparator_type
	 * <p>
	 * Usar ids de usuario del 80 al 99
	 * 
	 * */
	private static void caso2() throws ConnectionException {
		ColumnFamily<String, Integer> cfUsers = new ColumnFamily<String, Integer>(
				columnFamilyName3, StringSerializer.get(),
				IntegerSerializer.get());

		try {
			ksUsers.createColumnFamily(cfUsers, ImmutableMap
					.<String, Object> builder()
					// cómo almacenará cassandra internamente las column keys
					.put("key_validation_class", "IntegerType")
					// cómo se ordenarán las column keys
					.put("comparator_type", "IntegerType").build());

			MutationBatch m = ksUsers.prepareMutationBatch();

			ColumnListMutation<Integer> clm = m.withRow(cfUsers, rowKey);

			for (int i = 80; i < 100; i++) {
				clm.putColumn(i, "user" + i + "@void.com");
			}

			m.execute();

		} catch (Exception e) {
			System.out.println("ya existe el column family "
					+ columnFamilyName3);
		}
		ColumnList<Integer> result = ksUsers.prepareQuery(cfUsers)
				.getKey(rowKey).execute().getResult();
		if (!result.isEmpty()) {
			for (int i = 0; i < result.size(); i++) {
				String value = result.getColumnByIndex(i).getStringValue();
				System.out.println("email for user "
						+ result.getColumnByIndex(i).getName() + " is: "
						+ value);
			}
		}
	}

	/**
	 * Leer del column family users3 (caso 2) usando:
	 * <p>
	 * String como row key
	 * <p>
	 * String como column key
	 * 
	 * @throws ConnectionException
	 * 
	 * */
	private static void caso3() throws ConnectionException {
		ColumnFamily<String, String> cfUsers = new ColumnFamily<String, String>(
				columnFamilyName3, StringSerializer.get(),
				StringSerializer.get());
		ColumnList<String> result = ksUsers.prepareQuery(cfUsers)
				.getKey(rowKey).execute().getResult();
		if (!result.isEmpty()) {
			System.out.println(result.size());
			for (int i = 0; i < result.size(); i++) {
				String value = result.getColumnByIndex(i).getStringValue();
				System.out.println("email for user "
						+ result.getColumnByIndex(i).getName() + " is: "
						+ value);
			}
		}
	}
}
