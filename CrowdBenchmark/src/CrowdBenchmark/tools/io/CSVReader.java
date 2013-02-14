package CrowdBenchmark.tools.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CrowdBenchmark.models.Data;

public class CSVReader extends Reader {

	private String currentpath;
	private String content;
	private Map<String, Integer> mapping;
	private List<Data> data;

	public CSVReader() {
		currentpath = "results\\";
	}

	public CSVReader(String path) {
		currentpath = path;
	}

	@Override
	public void readfile(String filename) {
		data = new ArrayList<Data>();
		String path = filename;
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader dataInput = new BufferedReader(new FileReader(
					new File(path)));
			String titles = dataInput.readLine();
			Initmapping(titles);
			String line;

			while ((line = dataInput.readLine()) != null) {
				// buffer.append(cleanLine(line.toLowerCase()));
				String[] onerow = line.split(",");
				data.add(new Data(formatContent(onerow, "EvalID"),
						formatContent(onerow, "Answer Per Question"),
						formatContent(onerow, "Algorithm"), formatContent(
								onerow, "Worker accuray"), formatContent(
								onerow, "Correspondences Accuracy"),
						formatContent(onerow, "Completion time"),
						formatContent(onerow, "Observer")));
			}
			dataInput.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private String formatContent(String[] row, String title) {
		if (mapping.get(title) == null)
			return "Not exist";
		else {
			String detail = row[mapping.get(title)];
			if (title.equals("Algorithm")) {
				String[] algos = detail.split("\\.");
				return algos[1];
			} else
				return detail;
		}
	}

	private void Initmapping(String line) {
		mapping = new HashMap<String, Integer>();
		String[] titles = line.split(",");
		for (int i = 0; i < titles.length; i++) {
			mapping.put(titles[i], i);
		}
	}

	public void setCurrentPath(String path) {
		currentpath = path;
	}

	public List<Data> getContent() {
		return data;
	}

}
