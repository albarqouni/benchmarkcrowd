package CrowdBenchmark.models;

import java.util.ArrayList;
import java.util.List;

public enum ResultProvider {
	INSTANCE;

	private List<Data> data;

	private ResultProvider() {
		data = new ArrayList<Data>();
		data.add(new Data("1", "4", "Alg1", "0.1", "0.5", "10.0", "1"));
		data.add(new Data("1", "4", "Alg2", "0.1","0.5", "12.4", "2"));
		data.add(new Data("1", "4", "Alg3", "0.1","0.5", "2.4", "3"));
		data.add(new Data("1", "4", "Alg4", "0.1","0.5", "4.5", "4"));
		data.add(new Data("1", "4", "Alg5", "0.1","0.5", "5.6", "5"));
	}

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}
}
