package pd01.indiceinvertido;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class IndiceInvertidoReducer extends Reducer<Text, Text, Text, Text> {

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

// TODO completar
		
//		reduce(word, Iterator<string> files)
//		string fileList = “”
//		foreach f in files:
//		fileList += f
//		emit(word, fileList)

		StringBuilder fileList = new StringBuilder();
		for (Text f : values) {
			fileList.append(f+"#");
		}

		context.write(key, new Text(fileList.toString()));
		
	}
}
