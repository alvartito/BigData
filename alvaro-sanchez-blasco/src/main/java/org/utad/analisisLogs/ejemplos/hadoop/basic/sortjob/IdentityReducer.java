package org.utad.analisisLogs.ejemplos.hadoop.basic.sortjob;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
   * Just for showing pourposes. Use the default {@link Reducer} because it
   * is an identity reducer.
   */
  public class IdentityReducer extends Reducer<Text, NullWritable, Text, NullWritable> {

  	protected void reduce(Text line, Iterable<NullWritable> nulls, Context context) 
  			throws IOException ,InterruptedException {
  		for(NullWritable nul: nulls) {
  			context.write(line, nul);	
  		}  		
  	};
  			
	}