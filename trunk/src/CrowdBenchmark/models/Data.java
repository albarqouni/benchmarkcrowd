package CrowdBenchmark.models;


public class Data extends AbstractModel {
	private String evalID;
	private String answerPerQuestion;
	private String algorithm;
	private String workerEstimation;
	private String accuracy;
	private String duration;
	private String observer;

	private void Data() {
		// TODO Auto-generated method stub

	}

	public Data(String evalID, String answerPerQuestion, String algorithm, String workerEstimation,
			String accuracy, String duration, String observer) {
		this.evalID = evalID;
		this.answerPerQuestion = answerPerQuestion;
		this.algorithm = algorithm;
		this.workerEstimation = workerEstimation;
		this.accuracy = accuracy;
		this.duration = duration;
		this.observer = observer;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		propertyChangeSupport.firePropertyChange("algorithm", this.algorithm,
				this.algorithm = algorithm);
	}

	public String getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(String accuracy) {
		propertyChangeSupport.firePropertyChange("accuracy", this.accuracy,
				this.accuracy = accuracy);
	}

	public String getWorkerEstimation() {
		return workerEstimation;
	}

	public void setWorkerEstimation(String workerEstimation) {
		this.workerEstimation = workerEstimation;
	}

	public String getCompletionTime() {
		return duration;
	}

	public void setCompletionTime(String duration) {
		propertyChangeSupport.firePropertyChange("answer", this.duration,
				this.duration = duration);
	}

	public String getEvalID() {
		return evalID;
	}

	public void setEvalID(String evalID) {
		propertyChangeSupport.firePropertyChange("evalID", this.evalID,
				this.evalID = evalID);
	}

	public String getAnswerPerQuestion() {
		return answerPerQuestion;
	}

	public void setAnswerPerQuestion(String answerPerQuestion) {
		propertyChangeSupport.firePropertyChange("answerPerQuestion",
				this.answerPerQuestion,
				this.answerPerQuestion = answerPerQuestion);
	}

	@Override
	public String toString() {
		return evalID + "\t" + answerPerQuestion + "\t" + algorithm + "\t" + workerEstimation + "\t" 
				+ accuracy + "\t" + duration + "\t" + observer;
	}

	public String getObserver() {
		return observer;
	}

	public void setObserver(String observer) {
		this.observer = observer;
	}
}
