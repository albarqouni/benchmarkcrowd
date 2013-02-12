package CrowdBenchmark.models;

import java.util.HashMap;

public class AlgorithmAdvanceConfigModel extends AbstractModel{
	
	private HashMap<String, String> map;
	private String EM_iter;
	private String SLME_iter;
	private String ITER_iter;
	
	public AlgorithmAdvanceConfigModel(HashMap<String, String> map){
		this.map =  new HashMap<String, String>();
		EM_iter = map.get("Em_iter");
		SLME_iter = map.get("SLME_iter");
		ITER_iter = map.get("IterativeLearning_iter");
	}
	
	public void updateHashMap(){
		map.put("Em_iter", EM_iter);
		map.put("SLME_iter", SLME_iter);
		map.put("IterativeLearning_iter", ITER_iter);
	}

	public HashMap<String, String> getMap() {
		return map;
	}

	public String getEM_iter() {
		return EM_iter;
	}

	public void setEM_iter(String eM_iter) {
		EM_iter = eM_iter;
	}

	public String getSLME_iter() {
		return SLME_iter;
	}

	public void setSLME_iter(String sLME_iter) {
		SLME_iter = sLME_iter;
	}

	public String getITER_iter() {
		return ITER_iter;
	}

	public void setITER_iter(String iTER_iter) {
		ITER_iter = iTER_iter;
	}
}
