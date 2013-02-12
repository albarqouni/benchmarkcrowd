package CrowdBenchmark.models;

public class Answer extends AbstractModel {
	private String worker;
	private String question;
	private String answer;
	private int evalID;

	public Answer(int evalID, String worker, String question, String answer) {
		this.evalID = evalID;
		this.worker = worker;
		this.question = question;
		this.answer = answer;
	}

	public int getEvalID() {
		return evalID;
	}

	public void setEvalID(int evalID) {
		this.evalID = evalID;
	}

	public String getWorker() {
		return worker;
	}

	public void setWorker(String worker) {
		propertyChangeSupport.firePropertyChange("worker", this.worker,
				this.worker = worker);
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		propertyChangeSupport.firePropertyChange("question", this.question,
				this.question = question);
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		propertyChangeSupport.firePropertyChange("answer", this.answer,
				this.answer = answer);
	}

	@Override
	public String toString() {
		return evalID + "\t" + worker + "\t" + question + "\t" + answer;
	}

}
