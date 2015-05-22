package storm.ebd;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class HolaMundoTopology {
	public static void main(String[] args) {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("randomHolaMundo", new HolaMundoSpout(), 10);
		builder.setBolt("HolaMundoBolt", new HolaMundoBolt(), 2).shuffleGrouping("randomHolaMundo");

		Config conf = new Config();
		conf.setDebug(true);

		if (args != null && args.length > 0) {
			// Si vienen datos, intenta hacer submit de la topology y si no hay servidor (nimbus y
			// supervisor arrancados), lanza excepción.
			// El servidor nimbus se define en el archivo storm.yaml que se encuentra en la carpeta
			// conf del directorio de storm que estamos utilizando
			conf.setNumWorkers(3);
			try {
				StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
			} catch (AlreadyAliveException e) {
				e.printStackTrace();
			} catch (InvalidTopologyException e) {
				e.printStackTrace();
			}
		} else {
			// Trabajamos en un cluster en modo local. Lo simula.
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("test", conf, builder.createTopology());
			Utils.sleep(10000);
			cluster.killTopology("test");
			cluster.shutdown();
		}
	}
}
