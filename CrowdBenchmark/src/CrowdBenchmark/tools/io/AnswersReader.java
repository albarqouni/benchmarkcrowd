package CrowdBenchmark.tools.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import CrowdBenchmark.models.Answer;
public class AnswersReader extends TxtReader {

	private List<Answer> answers;

	public AnswersReader() {
		super();
	}

	@Override
	public void readfile(String filename) {
		String path = filename;
		answers = new ArrayList<Answer>();
		try {
			BufferedReader dataInput = new BufferedReader(new FileReader(
					new File(path)));
			String line;
			// title
			dataInput.readLine();

			while ((line = dataInput.readLine()) != null) {
				// buffer.append(cleanLine(line.toLowerCase()));
				// System.out.println(line);
				String[] content = line.split("\t");
				answers.add(new Answer(Integer.parseInt(content[0]),
						content[1], content[2], content[3]));
			}
			dataInput.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public List<Answer> getAnswers() {
		return answers;
	}
}
