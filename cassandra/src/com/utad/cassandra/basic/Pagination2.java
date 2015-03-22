package com.utad.cassandra.basic;

import java.util.Date;

import com.google.common.collect.ImmutableMap;
import com.netflix.astyanax.ColumnListMutation;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.query.RowQuery;
import com.netflix.astyanax.serializers.IntegerSerializer;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.util.RangeBuilder;
import com.utad.cassandra.util.Utils;

public class Pagination2 {
	public static void main(String args[]) throws ConnectionException {

		int pageSize = 10000;
		
		Keyspace ksUsers = Utils.getKeyspace("utad");
		
		ksUsers.dropColumnFamily("users");
		
		ColumnFamily<String, Integer> cfUsers = new ColumnFamily<String, Integer>(
				"users", StringSerializer.get(), IntegerSerializer.get());
		
		ksUsers.createColumnFamily(
				cfUsers,
				ImmutableMap.<String, Object> builder()
						.put("key_validation_class", "IntegerType")
						.put("comparator_type", "IntegerType").build());
		
		MutationBatch m = ksUsers.prepareMutationBatch();
		String rowKey = "usersById";
		ColumnListMutation<Integer> clm = m.withRow(cfUsers, rowKey);
		// crear mutation
		
		for (int i = 1; i <= 5000000; i++) {
			
			// escribir en el mutation			
			clm.putColumn(i, "user" + i + "@void.com");
			
			if (i % pageSize == 0) {
				m.execute();
				clm = m.withRow(cfUsers, rowKey);
				// si hemos completado una página, ejecutar el mutation
				
				// y volver a crear un mutation de nuevo! si no, la siguiente vez
				// volverá a escribir los datos anteriores
			}
		}
		
		m.execute();
		
		System.out.println("finished writing paginated values" + new Date());
		
		// leer los datos
		
		RowQuery<String,Integer> query = ksUsers.prepareQuery(cfUsers)
				.getKey(rowKey).autoPaginate(true).withColumnRange(new RangeBuilder().setLimit(pageSize).build());
		
		ColumnList<Integer> columns = query.execute().getResult();
		
		while (!columns.isEmpty()){
			for(Column<Integer> c: columns){
				String value = c.getName() + " " + c.getStringValue();
			}			
			columns = query.execute().getResult();
		}
		
		System.out.println("finished reading paginated values" + new Date());
	}
}
