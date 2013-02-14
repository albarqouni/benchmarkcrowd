package CrowdBenchmark.tools.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import CrowdBenchmark.models.Answer;

public class TxtWriter extends Writer {

	private static TxtWriter singleton;
	private static String outputPath;

	private TxtWriter() {
		// outputPath = Mainconfig.getInstance().getListConfig().get("Output");
		outputPath = "./";
	}

	public static TxtWriter getInstance() {
		if (singleton == null) {
			singleton = new TxtWriter();
		}
		return singleton;
	}

	private String Answers2String(List<Answer> input) {
		String content = "Worker" + "\t" + "Question" + "\t" + "Answer" + "\n";
		for (Answer answer : input) {
			content += answer.getWorker() + "\t" + answer.getQuestion() + "\t"
					+ answer.getAnswer() + "\n";
		}
		return content;
	}

	public String WriteToFile(List<Answer> input, String filename) {
		File outfile = new File(filename);
		try {

			/*
			 * if (!(new File(outfile.getParent()).exists())) { (new
			 * File(outfile.getParent())).mkdirs(); }
			 */
			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
			bw.write(Answers2String(input));
			bw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return outfile.getAbsolutePath();
	}

	@Override
	public String WriteToFile(String input, String filename) {
		File outfile = new File(outputPath + filename + ".txt");
		try {

			/*
			 * if (!(new File(outfile.getParent()).exists())) { (new
			 * File(outfile.getParent())).mkdirs(); }
			 */
			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
			bw.write(input);
			bw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return outfile.getAbsolutePath();
	}

	public void createDirIfNotExist(String folder) {
		File dir = new File(folder);

		if (!dir.exists()) {
			boolean success = dir.mkdirs();
			if (!success) {
				System.err.println("Unable to create " + folder + " folder");
				assert false : "Unable to create " + folder + " folder";
			}
		}
	}

}
