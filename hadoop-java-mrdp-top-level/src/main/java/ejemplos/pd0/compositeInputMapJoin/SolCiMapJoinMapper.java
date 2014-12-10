package ejemplos.pd0.compositeInputMapJoin;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.join.TupleWritable;

public class SolCiMapJoinMapper extends MapReduceBase implements
		Mapper<Text, TupleWritable, Text, Text> {
	Text leftResult = new Text();
	
	public void map(Text key, TupleWritable value,
			OutputCollector<Text, Text> output, Reporter reporter)
			throws IOException {
		String strKey = key.toString();
		String strEmployee = ((Text) value.get(0)).toString();
		leftResult.set( strKey + "	" + strEmployee);
		output.collect( leftResult, (Text) value.get(1));
	}
}