package CrowdBenchmark.tools.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class TxtReader extends Reader {

	private String currentpath;
	private String content;

	public TxtReader() {
		currentpath = "results\\";
	}

	public TxtReader(String path) {
		currentpath = path;
	}

	@Override
	public void readfile(String filename) {
		String path = filename;
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader dataInput = new BufferedReader(new FileReader(
					new File(path)));
			String line;

			while ((line = dataInput.readLine()) != null) {
				// buffer.append(cleanLine(line.toLowerCase()));
				buffer.append(line);
				buffer.append('\n');
			}
			dataInput.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		content = buffer.toString();
	}

	public void setCurrentPath(String path) {
		currentpath = path;
	}

	public String getContent() {
		return content;
	}
}
