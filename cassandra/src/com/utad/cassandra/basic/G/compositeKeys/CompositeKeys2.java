package com.utad.cassandra.basic.G.compositeKeys;

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

public class CompositeKeys2 {
	public static void main(String args[]) throws ConnectionException {
		// productos visitados por el usuario 1
				String[] products1 = { "1", "2", "3", "1", "2" };
				// productos visitados por el usuario 2
				String[] products2 = { "1", "1", "3", "1", "6" };
				// productos visitados por el usuario 3
				String[] products3 = { "1", "2", "5", "7", "8" };
				// productos visitados por el usuario 4
				String[] products4 = { "1", "2", "2", "8", "9" };
				// productos visitados por el usuario 5
				String[] products5 = { "4", "5", "6", "7", "7" };

				String[][] userVisitsProduct = { products1, products2, products3,
						products4, products5 };

//				family_user_visists_product2

				Keyspace ksUsers = Utils.getKeyspace("utad");
				
				//ksUsers.dropColumnFamily("UserVisitsProduct2");
				
				ColumnFamily<String, String> cfUsers = new ColumnFamily<String, String>(
						"UserVisitsProduct2", StringSerializer.get(), StringSerializer.get());
				try{
					ksUsers.createColumnFamily(
							cfUsers,
							ImmutableMap.<String, Object> builder()
									.put("default_validation_class", "CounterColumnType")
									.put("replicate_on_write",true).build());
				}
				catch(Exception e){
					System.out.println("Ya existe el column family");
				}

				MutationBatch m = ksUsers.prepareMutationBatch();
				String rowKey = "usersById";
				
				ColumnListMutation<String> clm = m.withRow(cfUsers, rowKey);
				
				for(int i= 0; i < userVisitsProduct.length; i++){
					String user = (i + 1) + "";
					for(String p : userVisitsProduct[i]){
						String key = user + ":" + p;
						clm.incrementCounterColumn(key, 1);
					}
				}
				m.execute();

				
				ColumnList<String> columns;

				RowQuery<String,String> query = ksUsers.prepareQuery(cfUsers)
						//.getKey("usersById").withColumnRange(new RangeBuilder().setStart("3:").setEnd("4").build()).autoPaginate(true);
						.getKey("usersById").withColumnRange(new RangeBuilder().build()).autoPaginate(true);
				
				columns = query.execute().getResult();
				
				for(Column<String> c : columns){
					String key = c.getName();
					Long value = c.getLongValue();
					
					String user = key.split(":")[0];
					String prod = key.split(":")[1];
										
					System.out.println("User " + user + " has visited product " + prod + ": " + value + " times");
				}
	}
}
