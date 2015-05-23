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

	private TopologyBuilder builder;
	private final String DEFAULT_JEDIS_PORT = "6379";
	private Config conf;
	private LocalCluster cluster;

	public ClickTopology() {
		getBuilder().setSpout("clickSpout", new ClickSpout(), 10);
		// Primera capa de bolts
		getBuilder().setBolt("repeatsBolt", new RepeatVisitBolt(), 10).shuffleGrouping("clickSpout");
		//La topología divide los streams en el bolt según el siguiente criterio de fieldsGrouping:
		getBuilder().setBolt("geographyBolt", new GeographyBolt(new HttpIPResolver()), 10).shuffleGrouping("clickSpout");
		// Segunda capa de bolts: conmutativa
		getBuilder().setBolt("totalStats", new VisitorStatsBolt(), 1).globalGrouping("repeatsBolt");
		getBuilder().setBolt("geoStats", new GeoStatsBolt(), 10).fieldsGrouping("geographyBolt",
				new Fields(storm.ebd.util.Fields.COUNTRY));
		getConf().put(Conf.REDIS_PORT_KEY, DEFAULT_JEDIS_PORT);
	}

	public TopologyBuilder getBuilder() {
		if(null == builder){
			builder = new TopologyBuilder();
		}
		return builder;
	}

	public LocalCluster getLocalCluster() {
		if(null == cluster){
			cluster = new LocalCluster();
		}
		return cluster;
	}

	public Config getConf() {
		if(null == conf) {
			conf = new Config();
		}
		return conf;
	}

	public void runLocal(int runTime) {
		getConf().setDebug(true);
		getConf().put(Conf.REDIS_HOST_KEY, "localhost");
		getLocalCluster().submitTopology("test", conf, getBuilder().createTopology());
		if (runTime > 0) {
			Utils.sleep(runTime);
			shutDownLocal();
		}
	}

	public void shutDownLocal() {
		if (getLocalCluster() != null) {
			getLocalCluster().killTopology("test");
			getLocalCluster().shutdown();
		}
	}

	public void runCluster(String name, String redisHost) throws AlreadyAliveException, InvalidTopologyException {
		getConf().setNumWorkers(20);
		getConf().put(Conf.REDIS_HOST_KEY, redisHost);
		StormSubmitter.submitTopology(name, getConf(), getBuilder().createTopology());
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
