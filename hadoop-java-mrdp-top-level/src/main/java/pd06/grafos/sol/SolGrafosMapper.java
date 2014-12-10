package pd06.grafos.sol;

import java.io.IOException;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SolGrafosMapper extends
		Mapper<Text, Text, IntWritable, SolNodeState> {

	IntWritable id = new IntWritable();
	IntWritable distance = new IntWritable();
	Text listNeighboors = new Text();
	BooleanWritable hasList = new BooleanWritable( true);
	BooleanWritable dontHaveList = new BooleanWritable( false);
	Text voidText = new Text( "");
	
	@Override
	public void map(Text key, Text value, Context context)
			throws IOException, InterruptedException {
		// 1	0	2,3,
		// 2	-1	1,4,5,
		id.set( Integer.parseInt( key.toString()));
		
		String[] tokens = value.toString().split( "\t");
		int distInt = Integer.parseInt( tokens[0]);
		distance.set( distInt);
		String listNeighboorsString = tokens[1];
		listNeighboors.set( listNeighboorsString);
		
		// First we have to emit the node we receive
		context.write( id, new SolNodeState( hasList, distance, listNeighboors));
		// And we update the node number counter
		context.getCounter( "nodes", "totalNumber").increment( 1);
		
		// do nothing if distance is infinite
		if( distInt == -1){
			return;
		}
		
		// else, we emit a proposed distance for every neighboor
		for (String idNeighboorString: listNeighboorsString.split( ",")) {
			if (idNeighboorString.length() > 0) {
				id.set( Integer.parseInt( idNeighboorString));
				distance.set( 1 + distInt);
				context.write( id, new SolNodeState( dontHaveList, distance, voidText));
			}
		}
	}
}
