package com.utad.cassandra.util;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

public class Utils {

	public static String clusterName = "utad";

	public static Keyspace getKeyspace(String name) {

		// Crear la conexión con la BD
		AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder().forCluster(clusterName).forKeyspace(name)
				.withAstyanaxConfiguration(new AstyanaxConfigurationImpl().setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE))
				.withConnectionPoolConfiguration(new ConnectionPoolConfigurationImpl("MyConnectionPool").setPort(9160).setMaxConnsPerHost(1).setSeeds("127.0.0.1:9160"))
				.withConnectionPoolMonitor(new CountingConnectionPoolMonitor()).buildKeyspace(ThriftFamilyFactory.getInstance());

		context.start();
		// Crear la conexión con la BD

		// Devolvemos el Keyspace que vamos a utilizar. 
		// Tenemos que haberlo creado previamente.
		return context.getClient();
	}
}
