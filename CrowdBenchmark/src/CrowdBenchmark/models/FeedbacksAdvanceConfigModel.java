package CrowdBenchmark.models;

import java.util.HashMap;

import CrowdBenchmark.util.Constant;

public class FeedbacksAdvanceConfigModel extends AbstractModel{
	private HashMap<String, String> map;
	private String feedbacksDistributor;
	private String feedbackRatio;
	private String feedbackRatioQuestion;
	
	public FeedbacksAdvanceConfigModel(HashMap<String, String> map){
		this.map =  new HashMap<String, String>();
		feedbacksDistributor = map.get("feedbacksDistributor");
		feedbackRatio = map.get("feedbackRatio");
		feedbackRatioQuestion = map.get("feedbackRatioQuestion");
	}
	
	public void updateHashMap(){
		map.put("feedbacksDistributor", feedbacksDistributor);
		map.put("feedbackRatio", feedbackRatio);
		map.put("feedbackRatioQuestion", feedbackRatioQuestion);
	}

	public HashMap<String, String> getMap() {
		return map;
	}

	public String getFeedbacksDistributor() {
		return feedbacksDistributor;
	}

	public void setFeedbacksDistributor(String feedbacksDistributor) {
		this.feedbacksDistributor = feedbacksDistributor;
	}

	public String getFeedbackRatio() {
		return feedbackRatio;
	}

	public void setFeedbackRatio(String feedbackRatio) {
		this.feedbackRatio = feedbackRatio;
	}

	public String getFeedbackRatioQuestion() {
		return feedbackRatioQuestion;
	}

	public void setFeedbackRatioQuestion(String feedbackRatioQuestion) {
		this.feedbackRatioQuestion = feedbackRatioQuestion;
	}
}
