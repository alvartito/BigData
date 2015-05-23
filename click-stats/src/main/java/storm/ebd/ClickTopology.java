package storm.ebd;

import storm.ebd.bolts.GeoStatsBolt;
import storm.ebd.bolts.GeographyBolt;
import storm.ebd.bolts.RepeatVisitBolt;
import storm.ebd.bolts.VisitorStatsBolt;
import storm.ebd.util.Conf;
import storm.ebd.util.HttpIPResolver;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

public class ClickTopology {

	private TopologyBuilder builder = new TopologyBuilder();
	private final String DEFAULT_JEDIS_PORT = "6379";
	private Config conf = new Config();
	private LocalCluster cluster = new LocalCluster();

	public ClickTopology() {
		builder.setSpout("clickSpout", new ClickSpout(), 10);
		// Primera capa de bolts
		builder.setBolt("repeatsBolt", new RepeatVisitBolt(), 10).shuffleGrouping("clickSpout");
		//La topología divide los streams en el bolt según el siguiente criterio de fieldsGrouping:
		builder.setBolt("geographyBolt", new GeographyBolt(new HttpIPResolver()), 10).shuffleGrouping("clickSpout");
		// Segunda capa de bolts: conmutativa
		builder.setBolt("totalStats", new VisitorStatsBolt(), 1).globalGrouping("repeatsBolt");
		builder.setBolt("geoStats", new GeoStatsBolt(), 10).fieldsGrouping("geographyBolt",
				new Fields(storm.ebd.util.Fields.COUNTRY));
		conf.put(Conf.REDIS_PORT_KEY, DEFAULT_JEDIS_PORT);
	}

	public void runLocal(int runTime) {
		conf.setDebug(true);
		conf.put(Conf.REDIS_HOST_KEY, "localhost");
		cluster.submitTopology("test", conf, builder.createTopology());
		if (runTime > 0) {
			Utils.sleep(runTime);
			shutDownLocal();
		}
	}

	public void shutDownLocal() {
		if (cluster != null) {
			cluster.killTopology("test");
			cluster.shutdown();
		}
	}

	public void runCluster(String name, String redisHost) throws AlreadyAliveException, InvalidTopologyException {
		conf.setNumWorkers(20);
		conf.put(Conf.REDIS_HOST_KEY, redisHost);
		StormSubmitter.submitTopology(name, conf, builder.createTopology());
	}

	public static void main(String[] args) throws Exception {
		// Nota. Insertamos los datos en Redis antes de ejecutarse.
		ClickTopology topology = new ClickTopology();
		if (args != null && args.length > 1) {
			topology.runCluster(args[0], args[1]);
		} else {
			if (args != null && args.length == 1)
				System.out.println("Ejecutandose en modo local.");
			topology.runLocal(10000);
		}

	}
}
