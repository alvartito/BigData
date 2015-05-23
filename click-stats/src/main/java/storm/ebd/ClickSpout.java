package storm.ebd;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import redis.clients.jedis.Jedis;
import storm.ebd.util.Conf;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class ClickSpout extends BaseRichSpout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SpoutOutputCollector collector;
	private String host;
	private int port;
	private Jedis jedis;
	
	@Override
	public void nextTuple() {
		String content = jedis.rpop("count");
		if (content == null || "nil".equals(content)) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
			}
		} else {
			JSONObject obj = (JSONObject) JSONValue.parse(content);
			String ip = obj.get(storm.ebd.util.Fields.IP).toString();
			String url = obj.get(storm.ebd.util.Fields.URL).toString();
			String clientKey = obj.get(storm.ebd.util.Fields.CLIENT_KEY).toString();
			collector.emit(new Values(ip, url, clientKey));
		}
	}

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		host = conf.get(Conf.REDIS_HOST_KEY).toString();
		port = Integer.valueOf(conf.get(Conf.REDIS_PORT_KEY).toString());
		this.collector = collector;
		connectToRedis();
	}

	private void connectToRedis() {
		jedis = new Jedis(host, port);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(storm.ebd.util.Fields.IP, storm.ebd.util.Fields.URL, storm.ebd.util.Fields.CLIENT_KEY));
	}

}
