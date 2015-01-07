package org.utad.analisisLogs.ejemplos.hadoop.writables;

import java.io.IOException;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Simple example job that computes the average temperature
 * of the hottest day for each location per each year. Input file to use:
 * avg_temp_per_day_2011.txt
 */
public class HottestDayPerYear extends Configured implements Tool {
	
  public static class LineAsKeyMap extends Mapper<LongWritable, Text, Text, FloatWritable> {

  	Text outKey = new Text();
  	FloatWritable outValue = new FloatWritable();
		@Override
		protected void map(LongWritable offset, Text line, Context context) 
				throws IOException ,InterruptedException {
			String row[] = line.toString().split(" ");
			String yearStr = row[0];
//			String monthStr = row[1];
//			String dayStr = row[2];
			String locationStr = row[3];
			float temperature = Float.valueOf(row[4]);
			
			outKey.set(locationStr + "|" + yearStr);
			outValue.set(temperature);
			
			context.write(outKey, outValue);
		};		
	}
 
  public static class MaxReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {
  	
  	FloatWritable maxTempOut = new FloatWritable();
  	
  	protected void reduce(Text locationYear, Iterable<FloatWritable> temperatures, Context context) 
  			throws IOException ,InterruptedException {
  		
  		float maxTemp = 0;
  		for(FloatWritable temperature: temperatures) {
  			// We don't keep the temperature instance, as instances are reused. Be careful!
  			maxTemp = Math.max(maxTemp, temperature.get());	
  		}  		
  		maxTempOut.set(maxTemp);
  		context.write(locationYear, maxTempOut);
  	};
  			
	}
		
	@Override
  public int run(String[] args) throws Exception {
		if(args.length != 2) {
			System.out.println("Invalid number of arguments\n\n" +
					"Usage: hottest-day-per-year <input_path> <output_path>\n\n");
			return -1;
		}
		String input = args[0];
		String output = args[1];
		
		Path oPath = new Path(output);
		FileSystem.get(oPath.toUri(), getConf()).delete(oPath, true);
		
		Job job = new Job(getConf(), "Hottest day per year");
		job.setJarByClass(HottestDayPerYear.class);
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(FloatWritable.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FloatWritable.class);
		
		job.setMapperClass(LineAsKeyMap.class);
		job.setCombinerClass(MaxReducer.class);
		job.setReducerClass(MaxReducer.class); 		
		
		FileInputFormat.addInputPath(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));
		
		job.waitForCompletion(true);
		
		return 0;
  }

	public static void main(String args[]) throws Exception {
		ToolRunner.run(new HottestDayPerYear(), args);
	}
}
