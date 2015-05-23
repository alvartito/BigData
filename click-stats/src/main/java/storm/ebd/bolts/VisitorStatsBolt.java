package storm.ebd.bolts;

import java.util.Map;

import storm.ebd.util.Fields;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class VisitorStatsBolt extends BaseRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;
	private int total = 0;
	private int uniqueCount = 0;
	
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple tuple) {
		boolean unique = Boolean.parseBoolean(tuple.getStringByField(storm.ebd.util.Fields.UNIQUE));
		total++;
		if (unique)
			uniqueCount++;
		collector.emit(new Values(total, uniqueCount));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new backtype.storm.tuple.Fields(Fields.TOTAL_COUNT, Fields.TOTAL_UNIQUE));
	}

}
