package ejemplos.compositeInputMapJoin;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.KeyValueTextInputFormat;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.mapred.join.CompositeInputFormat;

public class SolCiMapJoinDriver {

	public static void main(String[] args) throws Exception {
		Path employeePath = new Path(args[0]);
		Path dptPath = new Path(args[1]);
		Path outputPath = new Path(args[2]);
		
		JobConf conf = new JobConf("CompositeInputMapJoin");
		conf.setJarByClass(SolCiMapJoinDriver.class);
		conf.setMapperClass(SolCiMapJoinMapper.class);
		
		conf.setInputFormat(CompositeInputFormat.class);
		conf.set("mapred.join.expr", CompositeInputFormat.compose("inner",
				KeyValueTextInputFormat.class, employeePath, dptPath));
		
		TextOutputFormat.setOutputPath(conf, outputPath);
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);
		
		conf.setNumReduceTasks(0);
		
		RunningJob job = JobClient.runJob(conf);
		while (!job.isComplete()) {
			Thread.sleep(1000);
		}
		System.exit(job.isSuccessful() ? 0 : 1);
	}
}