package pd06.grafos.sol;

import java.io.IOException;

import org.apache.hadoop.io.BooleanWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class SolGrafosReducer extends
		Reducer<IntWritable, SolNodeState, IntWritable, SolNodeState> {

	Text listNeighboors = new Text();
	IntWritable minDistanceWritable = new IntWritable();
	BooleanWritable myTrue = new BooleanWritable(true);

	@Override
	public void reduce(IntWritable id, Iterable<SolNodeState> values,
			Context context) throws IOException, InterruptedException {

		int minDistance = Integer.MAX_VALUE;
		boolean hasList = false;

		for (SolNodeState state : values) {
			if (state.getHasListNeighboors().get()) {
				hasList = true;
				listNeighboors.set(state.getListNeighboors());
			}
			int thisDist = state.getDistance().get();
			if (thisDist != -1) { // exclude infinite distance
				if (thisDist < minDistance) {
					minDistance = thisDist;
				}
			}
		}

		if (hasList) {
			if (minDistance == Integer.MAX_VALUE) {
				minDistance = -1; // convert infinite to format used to store
									// data
			} else {
				// And we update the nodes with distance counter
				context.getCounter( "nodes", "reachedNumber").increment( 1);
			}
			minDistanceWritable.set(minDistance);
			context.write(id, new SolNodeState(myTrue, minDistanceWritable,
					listNeighboors));
		} else {
			// this is an error: we lost the information about the neighboors
			System.err
					.println("Error in reducer: we found no neighboors list for node: "
							+ id.get());
		}

	}
}
