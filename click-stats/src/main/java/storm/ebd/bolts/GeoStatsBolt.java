package storm.ebd.bolts;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import storm.ebd.util.Fields;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class GeoStatsBolt extends BaseRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;
	private Map<String, CountryStats> stats = new HashMap<String, CountryStats>();

	private class CountryStats {
		private int countryTotal = 0;
		private static final int COUNT_INDEX = 0;
		private static final int PERCENTAGE_INDEX = 1;
		private String countryName;

		public CountryStats(String countryName) {
			this.countryName = countryName;
		}

		private Map<String, List<Integer>> cityStats = new HashMap<String, List<Integer>>();

		public void cityFound(String cityName) {
			countryTotal++;
			if (cityStats.containsKey(cityName)) {
				cityStats.get(cityName).set(COUNT_INDEX, cityStats.get(cityName).get(COUNT_INDEX).intValue() + 1);
			} else {
				List<Integer> list = new LinkedList<Integer>();
				list.add(1);
				list.add(0);
				cityStats.put(cityName, list);
			}
			double percent = (double) cityStats.get(cityName).get(COUNT_INDEX) / (double) countryTotal;
			cityStats.get(cityName).set(PERCENTAGE_INDEX, (int) percent);
		}

		public int getCountryTotal() {
			return countryTotal;
		}

		public int getCityTotal(String cityName) {
			return cityStats.get(cityName).get(COUNT_INDEX).intValue();
		}

		public String toString() {
			return "Total Count for " + countryName + " is " + Integer.toString(countryTotal) + "\n" + "Cities: "
					+ cityStats.toString();
		}
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple tuple) {
		String country = tuple.getStringByField(Fields.COUNTRY);
		String city = tuple.getStringByField(Fields.CITY);
		if (!stats.containsKey(country)) {
			stats.put(country, new CountryStats(country));
		}
		stats.get(country).cityFound(city);
		collector.emit(new Values(country, stats.get(country).getCountryTotal(), city, stats.get(country).getCityTotal(city)));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new backtype.storm.tuple.Fields(Fields.COUNTRY, Fields.COUNTRY_TOTAL,
				Fields.CITY, Fields.CITY_TOTAL));
	}

}