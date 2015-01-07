package pd05.nline.sol;

import java.io.IOException;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SolNlineMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException,
			InterruptedException {
		
		String comment = "**** Results of the computation for the parameters [" + value.toString() + "]:";
		context.write( NullWritable.get(), new Text( comment));
		
		// 1.0,2.0,3.0,4.0
		String words[] = value.toString().split( ",");
		double m11 = Double.parseDouble( words[0]);
		double m12 = Double.parseDouble( words[1]);
		double m21 = Double.parseDouble( words[2]);
		double m22 = Double.parseDouble( words[3]);
		
		double[][] array2d = { {m11,m12} , {m21,m22} };
		RealMatrix rm = MatrixUtils.createRealMatrix( array2d);
		///double[][] ai = rm.inverse().getData();
		double[][] ai = rm.getData();
		
		StringBuilder sb = new StringBuilder();
		sb.append( "[[").append( ai[0][0]).append( " ").append( ai[0][1]).append( "][")
				.append( ai[1][0]).append( " ").append( ai[1][1]).append( "]]");
		
		context.write( NullWritable.get(), new Text(sb.toString()));
	}
}
