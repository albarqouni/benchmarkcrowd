package CrowdBenchmark.models;

import feedback.FeedBackModel;


public class BenchmarkModel extends AbstractModel {
	private FeedBackModel model;
	private int evalID;

	public BenchmarkModel(int evalID, FeedBackModel model) {
		this.evalID = evalID;
		this.model = model;
	}

	public int getEvalID() {
		return evalID;
	}

	public void setEvalID(int evalID) {
		this.evalID = evalID;
	}

	public FeedBackModel getModel() {
		return model;
	}

	public void setModel(FeedBackModel model) {
		this.model = model;
	}
}
