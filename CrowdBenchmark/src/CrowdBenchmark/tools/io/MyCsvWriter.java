package CrowdBenchmark.tools.io;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import CrowdBenchmark.models.Answer;
import CrowdBenchmark.models.Data;

import com.csvreader.CsvWriter;

public class MyCsvWriter extends Writer {
	private static MyCsvWriter singleton;
	private String defaultPath = "";;

	private MyCsvWriter() {
		//defaultPath = Mainconfig.getInstance().getListConfig().get("CSVoutput");
	}

	private MyCsvWriter(String path) {
		defaultPath = path;
	}

	public static MyCsvWriter getInstance() {
		if (singleton == null) {
			singleton = new MyCsvWriter();
		}
		return singleton;
	}

	@Override
	public String WriteToFile(String input, String FileName) {
		return WriteToFile(input, FileName, true, false);
	}
	
	public void WriteToFile(List<Data> input, String path){
		try {
			CsvWriter csvOutput = new CsvWriter(new FileWriter("test", false), ',');
			int size = input.size();
			int start = 0;
			for (; start < size; start++) {
				Data onerow = input.get(start);
				String[] content = onerow.toString().split("\t");
				for (String piece : content) {
					csvOutput.write(piece);
				}
				csvOutput.endRecord();
			}
			csvOutput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String WriteToFile(String input, String filename, boolean withTitle,
			boolean rawdata) {
		String FileName = filename;
		String[] lines = input.split("\n");
		if (rawdata) {
			saveRawFile(FileName, lines, withTitle);
		} else {
			saveGeneral(FileName, withTitle, lines);
		}
		return FileName;
	}

	private void saveRawFile(String outputFile, String[] lines,
			boolean withTitle) {
		boolean alreadyExists = new File(outputFile).exists();
		try {
			CsvWriter csvOutput = new CsvWriter(
					new FileWriter(outputFile, true), ',');
			int size = lines.length;
			int start = 0;
			if (!withTitle)
				start = 1;
			if (alreadyExists) {
				start = 1;
			}
			for (; start < size; start++) {
				String line = lines[start];
				csvOutput.write(line);
				csvOutput.endRecord();
			}
			csvOutput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveGeneral(String outputFile, boolean withTitle,
			String[] lines) {
		boolean alreadyExists = new File(outputFile).exists();
		try {
			CsvWriter csvOutput = new CsvWriter(
					new FileWriter(outputFile, true), ',');
			int size = lines.length;
			int start = 0;
			if (!withTitle)
				start = 1;
			if (alreadyExists) {
				start = 1;
			}
			for (; start < size; start++) {
				String line = lines[start];
				String[] content = line.split("\t");
				for (String piece : content) {
					csvOutput.write(piece);
				}
				csvOutput.endRecord();
			}
			csvOutput.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
