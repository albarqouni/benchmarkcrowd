package CrowdBenchmark.models;

public class Metric extends AbstractModel {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		propertyChangeSupport.firePropertyChange("name", this.name,
				this.name = name);
	}

	@Override
	public String toString() {
		return name;
	}

}
