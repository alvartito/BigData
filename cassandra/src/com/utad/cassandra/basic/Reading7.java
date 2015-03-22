package com.utad.cassandra.basic;

import java.util.ArrayList;
import java.util.List;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.query.RowQuery;
import com.netflix.astyanax.serializers.StringSerializer;
import com.utad.cassandra.util.Utils;

public class Reading7 {

public static void main(String args[]) throws ConnectionException {
		
		List<String> ids = new ArrayList<String>();
		ids.add("1");
		ids.add("3");
		ids.add("5");
		ids.add("7");
		ids.add("11");
		String keyspaceName = "utad";
		String columnFamilyName = "users";
		String rowKeyUsersById = "usersById";
		
		Keyspace ksUsers = Utils.getKeyspace(keyspaceName);

		ColumnFamily<String, String> cfUsers = new ColumnFamily<String, String>(columnFamilyName, StringSerializer.get(), StringSerializer.get());

		// Si necesitamos borrar el column family
		// ksUsers.dropColumnFamily(columnFamilyName);

		RowQuery<String,String> query = ksUsers.prepareQuery(cfUsers)
				.getKey(rowKeyUsersById).withColumnSlice(ids);
		
		for(Column<String> c: query.execute().getResult()){
			System.out.println("Email for user " + c.getName() + " is " + c.getStringValue());
		}
		
		List<String> ids2 = new ArrayList<String>();
		ids2.add("1");
		ids2.add("3");
		ids2.add("5");
		ids2.add("7");
		ids2.add("11");
		
		// Otra opción: 		
		List<String> aux =  new ArrayList<String>();
		for (String s : ids2){
			if (Integer.valueOf(s) >= 5) aux.add(s);  
		}
		
		RowQuery<String,String> query2 = ksUsers.prepareQuery(cfUsers)
				.getKey(rowKeyUsersById).withColumnSlice(aux);
		
		for(Column<String> c: query2.execute().getResult()){
			System.out.println("Email for user " + c.getName() + " is " + c.getStringValue());
		}
	}
}
