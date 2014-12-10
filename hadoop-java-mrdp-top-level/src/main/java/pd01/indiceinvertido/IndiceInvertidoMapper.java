package pd01.indiceinvertido;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class IndiceInvertidoMapper extends Mapper<Text, Text, Text, Text> {

	// TODO write initialization method

	private String fileName = ""; 
	
	public void setup(Context context) {
		FileSplit fileSplit = (FileSplit) context.getInputSplit(); 
		Path path = fileSplit.getPath(); 
		fileName = path.getName();
	}

	
	
	@Override
	public void map(Text key, Text value, Context context) throws IOException,
			InterruptedException {
//		El pseudo código sería:
//			map(offset, line)
//			foreach word in line:
//			emit( word, nombreFichero)
		String row[] = value.toString().split(" ");
		for (String string : row) {
			context.write(new Text(string), new Text(fileName+"@"+key.toString()));
		}
	}
}
