package storm.ebd;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

/**
 * Esta clase consume las tuplas producidas e implementa la lógica necesaria. La
 * creamos dentro del mismo paquete storm.ebd y hacemos que herede de la clase
 * BaseRichBolt.
 * 
 * */
public class HolaMundoBolt extends BaseRichBolt {

	private int myCount = 0;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void execute(Tuple input) {
		String test = input.getStringByField("sentence");
		if("Hola Mundo".equals(test)){
		myCount++;
		System.out.println("Encontrado Hola Mundo!. Contador:" +
		Integer.toString(myCount));
		}
	}

	@Override
	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// TODO Auto-generated method stub

	}

}
