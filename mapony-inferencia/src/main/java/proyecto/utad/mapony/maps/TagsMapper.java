package proyecto.utad.mapony.maps;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import writables.CustomWritable;

public class TagsMapper extends Mapper<Text, Text, Text, CustomWritable> {
		
	
		public void map(Text k, Text v, Context con)
				throws IOException, InterruptedException {
			String[] line = k.toString().split("\t");
			if (line.length == 3){
				CustomWritable value = new CustomWritable("tags");
				value.setTexto(line[2].toLowerCase() + " " +  v.toString().toLowerCase());
				con.write(new Text(line[0]), value); 
			}
		}
}
