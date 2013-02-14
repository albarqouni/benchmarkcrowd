package CrowdBenchmark.models;

import java.beans.PropertyChangeSupport;
import java.util.Map;

import tools.io.ConfigReader;
import CrowdBenchmark.util.Constant;

public class SimulationParameter extends AbstractModel {

	private Integer uniformSpammer;
	private Integer randomSpammer;
	private Integer expert;
	private Integer normalWorker;
	private Integer sloppyWorker;
	private Integer question;
	private Integer category;
	private Integer trapQuestion;
	private Integer answerPerQuestion;
	private Integer answerPerWorker;
	private Integer minCommonQuestion;
	private Integer minObserverValue;
	private Integer maxObserverValue;
	private Integer stepObserverValue;
	private Map<String, String> list;

	public SimulationParameter() {
		// init();
		//initMap();
	}

	private void init() {
		this.uniformSpammer = new Integer(5);
		this.randomSpammer = new Integer(5);
		this.expert = new Integer(5);
		this.normalWorker = new Integer(5);
		this.sloppyWorker = new Integer(5);
		this.question = new Integer(40);
		this.category = new Integer(2);
		this.trapQuestion = new Integer(10);
		this.answerPerQuestion = new Integer(5);
		this.answerPerWorker = new Integer(8);
		this.minCommonQuestion = new Integer(8);

		this.stepObserverValue = new Integer(1);
	}

	private void initMap() {
		ConfigReader reader = new ConfigReader();
		reader.readfile(Constant.SIMULATE_INIT_FILE);
		list = reader.getConfig();
		this.uniformSpammer = Integer.parseInt(list.get("uniformSpammer"));
		this.randomSpammer = Integer.parseInt(list.get("randomSpammer"));
		this.expert = Integer.parseInt(list.get("expert"));
		this.normalWorker = Integer.parseInt(list.get("normalWorker"));
		this.sloppyWorker = Integer.parseInt(list.get("sloppyWorker"));
		this.question = Integer.parseInt(list.get("question"));
		this.category = Integer.parseInt(list.get("category"));
		this.trapQuestion = Integer.parseInt(list.get("trapQuestion"));
		this.answerPerQuestion = Integer
				.parseInt(list.get("answerPerQuestion"));
		this.answerPerWorker = Integer.parseInt(list.get("answerPerWorker"));
		this.minCommonQuestion = Integer
				.parseInt(list.get("minCommonQuestion"));
		this.stepObserverValue = new Integer(1);

	}

	public Integer getUniformSpammer() {
		return uniformSpammer;
	}

	public void setUniformSpammer(Integer uniformSpammer) {
		propertyChangeSupport.firePropertyChange("uniformSpammer",
				this.uniformSpammer, this.uniformSpammer = uniformSpammer);
	}

	public Integer getRandomSpammer() {
		return randomSpammer;
	}

	public void setRandomSpammer(Integer randomSpammer) {
		propertyChangeSupport.firePropertyChange("randomSpammer",
				this.randomSpammer, this.randomSpammer = randomSpammer);
	}

	public Integer getExpert() {
		return expert;
	}

	public void setExpert(Integer expert) {
		propertyChangeSupport.firePropertyChange("expert", this.expert,
				this.expert = expert);
	}

	public Integer getNormalWorker() {
		return normalWorker;
	}

	public void setNormalWorker(Integer normalWorker) {
		propertyChangeSupport.firePropertyChange("normalWorker",
				this.normalWorker, this.normalWorker = normalWorker);
		// updateTotal();
	}

	public Integer getSloppyWorker() {
		return sloppyWorker;
	}

	public void setSloppyWorker(Integer sloppyWorker) {
		propertyChangeSupport.firePropertyChange("sloppyWorker",
				this.sloppyWorker, this.sloppyWorker = sloppyWorker);
		// updateTotal();
	}

	public Integer getQuestion() {
		return question;
	}

	public void setQuestion(Integer question) {
		propertyChangeSupport.firePropertyChange("question", this.question,
				this.question = question);
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		propertyChangeSupport.firePropertyChange("category", this.category,
				this.category = category);
	}

	public Integer getTrapQuestion() {
		return trapQuestion;
	}

	public void setTrapQuestion(Integer trapQuestion) {
		propertyChangeSupport.firePropertyChange("trapQuestion",
				this.trapQuestion, this.trapQuestion = trapQuestion);
	}

	public Integer getAnswerPerQuestion() {
		return answerPerQuestion;
	}

	public void setAnswerPerQuestion(Integer answerPerQuestion) {
		propertyChangeSupport.firePropertyChange("answerPerQuestion",
				this.answerPerQuestion,
				this.answerPerQuestion = answerPerQuestion);
	}

	public Integer getAnswerPerWorker() {
		return answerPerWorker;
	}

	public void setAnswerPerWorker(Integer answerPerWorker) {
		propertyChangeSupport.firePropertyChange("answerPerWorker",
				this.answerPerWorker, this.answerPerWorker = answerPerWorker);
	}

	public PropertyChangeSupport getPropertyChangeSupport() {
		return propertyChangeSupport;
	}

	public Integer getMinCommonQuestion() {
		return minCommonQuestion;
	}

	public void setMinCommonQuestion(Integer minCommonQuestion) {
		propertyChangeSupport.firePropertyChange("minCommonQuestion",
				this.minCommonQuestion,
				this.minCommonQuestion = minCommonQuestion);
	}

	public Integer getMinObserverValue() {
		return minObserverValue;
	}

	public void setMinObserverValue(Integer minObserverValue) {
		propertyChangeSupport
				.firePropertyChange("minObserverValue", this.minObserverValue,
						this.minObserverValue = minObserverValue);
	}

	public Integer getMaxObserverValue() {
		return maxObserverValue;
	}

	public void setMaxObserverValue(Integer maxObserverValue) {
		propertyChangeSupport
				.firePropertyChange("maxObserverValue", this.minObserverValue,
						this.maxObserverValue = maxObserverValue);
	}

	public Integer getStepObserverValue() {
		return stepObserverValue;
	}

	public void setStepObserverValue(Integer stepObserverValue) {
		propertyChangeSupport.firePropertyChange("stepObserverValue",
				this.stepObserverValue,
				this.stepObserverValue = stepObserverValue);
	}

}
