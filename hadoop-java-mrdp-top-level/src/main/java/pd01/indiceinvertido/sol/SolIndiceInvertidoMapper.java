package pd01.indiceinvertido.sol;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class SolIndiceInvertidoMapper extends Mapper<Text, Text, Text, Text> {

	private String fileName;
	private Text wordText = new Text();
	private Text reference = new Text();

	@Override
	public void setup(Context context) {
		FileSplit fileSplit = (FileSplit) context.getInputSplit();
		Path path = fileSplit.getPath();
		fileName = path.getName() + "#";
	}

	@Override
	public void map(Text key, Text value, Context context) throws IOException,
			InterruptedException {
		String line = value.toString();
		for (String word : line.split("\\W+")) {
			if (word.length() > 0) {
				wordText.set(word);
				reference.set(fileName + key.toString());

				context.write(wordText, reference);
			}
		}
	}
}
