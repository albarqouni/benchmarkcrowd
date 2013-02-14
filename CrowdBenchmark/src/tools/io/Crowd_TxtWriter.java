package tools.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import CrowdBenchmark.models.Answer;

public class Crowd_TxtWriter extends Writer {

	private static Crowd_TxtWriter singleton;
	private static String outputPath;
	
	private Crowd_TxtWriter() {
		//outputPath = Mainconfig.getInstance().getListConfig().get("Output");
		outputPath = "./";
	}

	public static Crowd_TxtWriter getInstance() {
		if (singleton == null) {
			singleton = new Crowd_TxtWriter();
		}
		return singleton;
	}
	
	private String Answers2String(List<Answer> input){
		String content = "Worker" + "\t" + "Question" + "\t" + "Answer" + "\n";
		for(Answer answer : input){
			content+= answer.getWorker() + "\t" + answer.getQuestion() + "\t" + answer.getAnswer() + "\n";
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
		if (outfile.exists())
			try {
				outfile.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
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

}
