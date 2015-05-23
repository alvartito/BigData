package storm.ebd.bolts;

import java.util.Map;

import redis.clients.jedis.Jedis;
import storm.ebd.util.Conf;
import storm.ebd.util.Fields;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class RepeatVisitBolt extends BaseRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;
	private String host;
	private int port;
	private Jedis jedis;

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		host = stormConf.get(Conf.REDIS_HOST_KEY).toString();
		port = Integer.valueOf(stormConf.get(Conf.REDIS_PORT_KEY).toString());
		connectToRedis();
	}

	private void connectToRedis() {
		jedis = new Jedis(host, port);
		jedis.connect();
	}

	// ClickSpout. Es necesario mirar si el los flags del cliente
	// en Redis y actuar en consecuencia emitiendo la tupla modificada
	@Override
	public void execute(Tuple tuple) {
		String clientKey = tuple.getStringByField(storm.ebd.util.Fields.CLIENT_KEY);
		String url = tuple.getStringByField(storm.ebd.util.Fields.CLIENT_KEY);
		String key = url + ":" + clientKey;
		String value = jedis.get(key);
		if (value == null) { // es un cliente nuevo
			jedis.set(key, "visited");
			collector.emit(new Values(clientKey, url, Boolean.TRUE.toString()));
		} else {
			collector.emit(new Values(clientKey, url, Boolean.FALSE.toString()));
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// TODO Auto-generated method stub

	}

}
