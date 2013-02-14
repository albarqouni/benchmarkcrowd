package CrowdBenchmark.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommunityAdvanceConfigModel extends AbstractModel{
	
	private HashMap<String, String> map;
	//private String type;
	//private final String[] distribution = {"Normla Distribution", "Uniform Distribution", "Fix Distribution"};
	private WorkerAdvanceConfigModel expert;
	private WorkerAdvanceConfigModel normal;
	private WorkerAdvanceConfigModel sloppy;
	
	public CommunityAdvanceConfigModel(HashMap<String, String> map){
		this.map = new HashMap<String, String>();
		init(map);
	}
	
	private void init(HashMap<String, String> map){
		String[] listofDistribution = map.get("typeOfDistributor").split(";");
		expert = new WorkerAdvanceConfigModel(map, listofDistribution[0]);
		normal = new WorkerAdvanceConfigModel(map, listofDistribution[1]);
		sloppy = new WorkerAdvanceConfigModel(map, listofDistribution[2]);
	}
	
	public void updatehashMap(){
		String value = expert.getTypeofDistribution() + ";" + normal.getTypeofDistribution() + ";" +sloppy.getTypeofDistribution();
		this.map.put("typeOfDistributor", value);
		this.map.put("typeOfDistributorSensitivity", value);
		this.map.put("typeOfDistributorSpecificity", value);
	}

	public HashMap<String, String> getMap() {
		return map;
	}

	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}

	public WorkerAdvanceConfigModel getExpert() {
		return expert;
	}

	public void setExpert(WorkerAdvanceConfigModel expert) {
		this.expert = expert;
	}

	public WorkerAdvanceConfigModel getNormal() {
		return normal;
	}

	public void setNormal(WorkerAdvanceConfigModel normal) {
		this.normal = normal;
	}

	public WorkerAdvanceConfigModel getSloppy() {
		return sloppy;
	}

	public void setSloppy(WorkerAdvanceConfigModel sloppy) {
		this.sloppy = sloppy;
	}
	
}
