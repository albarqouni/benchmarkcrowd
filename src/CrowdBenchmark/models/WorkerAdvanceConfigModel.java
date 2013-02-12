package CrowdBenchmark.models;

import java.util.HashMap;

import CrowdBenchmark.util.Constant;

public class WorkerAdvanceConfigModel {
	private HashMap<String, String> map;
	
	private String selectedDistribution;
	private String mean;
	private String sd;
	private String lowBound;
	private String upBound;
	private String fixed;
	
	
	
	public WorkerAdvanceConfigModel(HashMap<String, String> map, String typeOfDistribution){
		this.map = map;
		init(typeOfDistribution);
	}
	
	public String getTypeofDistribution(){
		switch (Constant.DISTRIBUTION.valueOf(selectedDistribution)) {
		case NormalDistribution:
			return "NormalDistribution(" + mean + "," + sd + ")";
		case UniformDistribution:
			return "UniformDistribution(" + lowBound + "," + upBound + ")";						
		default:
			return "FixedDistribution(" + fixed + ")";
		}
	}
	
	private void init(String typeOfDistribution){
		mean = map.get("mean");
		sd = map.get("sd");
		lowBound = map.get("lowBound");
		upBound = map.get("upBound");
		fixed = map.get("fixed");
		String[] match = typeOfDistribution.split("[(,)]");
		switch (Constant.DISTRIBUTION.valueOf(match[0])) {
		case NormalDistribution:
			selectedDistribution = "NormalDistribution";
			mean = match[1];
			sd = match[2];
			break;
		case UniformDistribution:
			selectedDistribution = "UniformDistribution";
			lowBound = match[1];
			upBound = match[2];
			break;
		default:
			selectedDistribution = "FixedDistribution";
			int index = match.length;
			fixed = match[index-1];
			break;
		}
	}

	public String getSelectedDistribution() {
		return selectedDistribution;
	}

	public void setSelectedDistribution(String selectedDistribution) {
		this.selectedDistribution = selectedDistribution;
	}

	public String getMean() {
		return mean;
	}

	public void setMean(String mean) {
		this.mean = mean;
	}

	public String getSd() {
		return sd;
	}

	public void setSd(String sd) {
		this.sd = sd;
	}

	public String getLowBound() {
		return lowBound;
	}

	public void setLowBound(String lowBound) {
		this.lowBound = lowBound;
	}

	public String getUpBound() {
		return upBound;
	}

	public void setUpBound(String upBound) {
		this.upBound = upBound;
	}

	public String getFixed() {
		return fixed;
	}

	public void setFixed(String fixed) {
		this.fixed = fixed;
	}
}
