package storm.ebd.bolts;

import java.util.Map;

import org.json.simple.JSONObject;

import storm.ebd.util.IPResolver;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class GeographyBolt extends BaseRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private IPResolver resolver;
	private OutputCollector collector;

	public GeographyBolt(IPResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple tuple) {
		String ip = tuple.getStringByField(storm.ebd.util.Fields.IP);
		JSONObject json = resolver.resolveIP(ip);
		String city = (String) json.get(storm.ebd.util.Fields.CITY);
		String country = (String) json.get(storm.ebd.util.Fields.COUNTRY_NAME);
		collector.emit(new Values(country, city));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(storm.ebd.util.Fields.COUNTRY, storm.ebd.util.Fields.CITY));
	}

}
