package CrowdBenchmark.models;

import java.util.HashSet;
import java.util.Set;

import CrowdBenchmark.util.Constant.METRIC;

public class MetricModel extends AbstractModel {
	private Set<Metric> metrics = new HashSet<Metric>();

	public MetricModel() {
		METRIC[] metricName = METRIC.values();
		for (int i = 0; i < metricName.length; i++) {
			Metric metric = new Metric();
			metric.setName(String.valueOf(metricName[i]));
			metrics.add(metric);
		}
	}

	public Set<Metric> getMetrics() {
		return metrics;
	}

	public void setMetrics(Set<Metric> metrics) {
		propertyChangeSupport.firePropertyChange("metrics", this.metrics,
				this.metrics = new HashSet(metrics));
	}

	@Override
	public String toString() {
		String tmp = "";
		for (Metric o : metrics) {
			tmp += ", " + o.getName();
		}
		if (!tmp.isEmpty()) {
			tmp = tmp.substring(1).trim();

		}
		return tmp;
	}

}
