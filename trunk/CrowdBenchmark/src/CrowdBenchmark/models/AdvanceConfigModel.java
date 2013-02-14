package CrowdBenchmark.models;

import java.util.HashMap;

import CrowdBenchmark.tools.io.ConfigReader;
import CrowdBenchmark.tools.io.ConfigWriter;


public class AdvanceConfigModel extends AbstractModel {
	private AlgorithmAdvanceConfigModel algos;
	private CommunityAdvanceConfigModel workers;
	private FeedbacksAdvanceConfigModel feedbacks;
	private HashMap<String, String> map;
	
	public AdvanceConfigModel(){
	}
	
	public void readAdvanceConfigFile(String filename){
		ConfigReader reader = new ConfigReader();
		reader.readfile(filename);
		map = (HashMap<String, String>) reader.getConfig();
		algos = new AlgorithmAdvanceConfigModel(map);
		workers = new CommunityAdvanceConfigModel(map);
		feedbacks = new FeedbacksAdvanceConfigModel(map);
	}
	
	private void updateConfig(){
		HashMap<String, String> algos_map = algos.getMap();
		for(String key : algos_map.keySet()){
			//System.out.println(key + " " + algos_map.get(key));
			map.put(key, algos_map.get(key));
		}
		
		HashMap<String, String> workers_map = workers.getMap();
		for(String key : workers_map.keySet()){
			//System.out.println(key + " " + workers_map.get(key));
			map.put(key, workers_map.get(key));
		}
		
		HashMap<String, String> feedbacks_map = feedbacks.getMap();
		for(String key : feedbacks_map.keySet()){
			//System.out.println(key + " " + feedbacks.get(key));
			map.put(key, feedbacks_map.get(key));
		}
	}
	
	public void writeAdvanceConfigFile(String filename){
		updateConfig();
		ConfigWriter.getInstance().WriteToFile(map, filename);
	}

	public AlgorithmAdvanceConfigModel getAlgos() {
		return algos;
	}

	public void setAlgos(AlgorithmAdvanceConfigModel algos) {
		this.algos = algos;
	}

	public CommunityAdvanceConfigModel getWorkers() {
		return workers;
	}

	public void setWorkers(CommunityAdvanceConfigModel workers) {
		this.workers = workers;
	}

	public FeedbacksAdvanceConfigModel getFeedbacks() {
		return feedbacks;
	}

	public void setFeedbacks(FeedbacksAdvanceConfigModel feedbacks) {
		this.feedbacks = feedbacks;
	}

	public HashMap<String, String> getMap() {
		return map;
	}

	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}	
}
