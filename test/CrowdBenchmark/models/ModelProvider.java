package CrowdBenchmark.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Just for testing
 * 
 * @author sonthanh
 * 
 */
public enum ModelProvider {
	INSTANCE;

	private List<Answer> answers;

	private ModelProvider() {
		answers = new ArrayList<Answer>();
		answers.add(new Answer(1, "W1", "Q1", "Y"));
		answers.add(new Answer(1, "W2", "Q2", "N"));
		answers.add(new Answer(1, "W1", "Q3", "Y"));
		answers.add(new Answer(1, "W3", "Q1", "Y"));
		answers.add(new Answer(1, "W1", "Q2", "N"));
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

}
