package CrowdBenchmark.parts;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.ui.RectangleInsets;

import CrowdBenchmark.events.EventConstants;
import CrowdBenchmark.models.Data;
import CrowdBenchmark.models.MetricModel;
import CrowdBenchmark.util.Constant;
import CrowdBenchmark.util.Constant.METRIC;

public class ChartPart {

	private List<Data> data;
	private Map<String, List<Data>> map;
	// CHART_XY chartType = CHART_XY.AnswerPerQuestion_Accurary;
	// Map<CHART_XY, XYSeriesCollection> chartInfo;

	@Inject
	IEclipseContext context;
	private JFreeChart chart;
	private XYSeriesCollection dataset;
	private Composite parent;
	private String xAxisTitle;
	private String yAxisTitle;
	private String chartTitle;
	private String partID;

	private METRIC curMetric;
	private String curFactor;

	@PostConstruct
	public void createComposite(Composite parent) {
		this.parent = parent;
		initChartInfo();
		createTestData();

		chart = createChart(createDataSet(data));

		ChartComposite chartComposite = new ChartComposite(parent, SWT.NONE,
				chart, true);
		chartComposite.setDisplayToolTips(true);
		chartComposite.setHorizontalAxisTrace(false);
		chartComposite.setVerticalAxisTrace(false);

	}

	private void initChartInfo() {
		Object metrics = context.get(Constant.METRIC);
		if (metrics != null && metrics instanceof MetricModel) {
			MetricModel tmp = (MetricModel) metrics;

			assert tmp.getMetrics().size() != 1;
			yAxisTitle = tmp.toString();
			curMetric = METRIC.valueOf(yAxisTitle);

			if (curMetric == METRIC.ComputationTime) {
				yAxisTitle = yAxisTitle + " (ms)";
			}
		}

		Object observers = context.get(Constant.OBSERVER);
		if (observers != null & observers instanceof String) {
			curFactor = (String) observers;
			xAxisTitle = curFactor;
		}

		chartTitle = "The effect of " + curFactor + " on "
				+ String.valueOf(curMetric);
		partID = genPartID(curMetric, curFactor);

		dataset = new XYSeriesCollection();
		// chartInfo = new HashMap<Constant.CHART_XY, XYSeriesCollection>();
		// chartInfo.put(CHART_XY.AnswerPerQuestion_Accurary,
		// new XYSeriesCollection());
		// chartInfo.put(CHART_XY.AnswerPerQuestion_CompletionTime,
		// new XYSeriesCollection());
	}

	private void createTestData() {
		// CSVReader reader = new CSVReader();
		// reader.readfile(Constant.RESULT_TEST_FILE);
		// this.data = reader.getContent();
	}

	// private JFreeChart createChart(List<Data> data, CHART_XY chartType) {
	// // if (data == null) {
	// // return createChart(createDataSet(data, CHART_XY.DEFAULT),
	// // CHART_XY.DEFAULT);
	// // }
	// // return createChart(createDataSet(data, chartType), chartType);
	// }

	private JFreeChart createChart(XYDataset dataset) {

		// switch (chartType) {
		// case AnswerPerQuestion_Accurary:
		// xAxisTitle = Constant.ANSWER_PER_QUESTION;
		// yAxisTitle = Constant.ACCURACY;
		// chartTitle = Constant.ACCURACY_VS_ANSWER_PER_QUESTION;
		// break;
		//
		// case AnswerPerQuestion_CompletionTime:
		// xAxisTitle = Constant.ANSWER_PER_QUESTION;
		// yAxisTitle = Constant.COMPLETETION_TIME + " (ms)";
		// chartTitle = Constant.COMPLETETION_VS_ANSWER_PER_QUESTION;
		// break;
		// default:
		// break;
		// }
		JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,
				xAxisTitle, // x axis label
				yAxisTitle, // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
				);
		chart.setBackgroundPaint(Color.white);

		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		// plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);

		XYItemRenderer r = plot.getRenderer();
		if (r instanceof XYLineAndShapeRenderer) {
			XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			renderer.setBaseShapesVisible(true);
			renderer.setBaseShapesFilled(true);
		}

		// change the auto tick unit selection to integer units only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		return chart;
	}

	private XYDataset createDataSet(List<Data> data) {
		if (data == null) {
			return dataset;
		} else {
			dataset.removeAllSeries();
		}
		map = new HashMap<String, List<Data>>();
		for (Data d : data) {
			if (!map.containsKey(d.getAlgorithm())) {
				List<Data> tmp = new ArrayList<Data>();
				tmp.add(d);
				map.put(d.getAlgorithm(), tmp);
			} else {
				map.get(d.getAlgorithm()).add(d);
			}
		}

		// switch (chartType) {
		// case AnswerPerQuestion_CompletionTime:
		// for (String algo : map.keySet()) {
		// List<Data> value = map.get(algo);
		// XYSeries series = new XYSeries(algo);
		// for (Data d : value) {
		// series.add(Double.valueOf(d.getObserver()),
		// Double.valueOf(d.getCompletionTime()));
		// }
		// dataset.addSeries(series);
		// }
		// break;
		//
		// case AnswerPerQuestion_Accurary:
		// for (String algo : map.keySet()) {
		// List<Data> value = map.get(algo);
		// XYSeries series = new XYSeries(algo);
		// for (Data d : value) {
		// series.add(Double.valueOf(d.getObserver()),
		// Double.valueOf(d.getAccuracy()));
		// }
		// dataset.addSeries(series);
		// }
		// break;
		// }
		switch (curMetric) {
		case WorkerEstimationError:
			for (String algo : map.keySet()) {
				List<Data> value = map.get(algo);
				XYSeries series = new XYSeries(algo);
				for (Data d : value) {
					series.add(Double.valueOf(d.getObserver()),
							Double.valueOf(d.getWorkerEstimation()));
				}
				dataset.addSeries(series);
			}
			break;
		case Accuracy:
			for (String algo : map.keySet()) {
				List<Data> value = map.get(algo);
				XYSeries series = new XYSeries(algo);
				for (Data d : value) {
					series.add(Double.valueOf(d.getObserver()),
							Double.valueOf(d.getAccuracy()));
				}
				dataset.addSeries(series);
			}
			break;
		case ComputationTime:
			for (String algo : map.keySet()) {
				List<Data> value = map.get(algo);
				XYSeries series = new XYSeries(algo);
				for (Data d : value) {
					series.add(Double.valueOf(d.getObserver()),
							Double.valueOf(d.getCompletionTime()));
				}
				dataset.addSeries(series);
			}
			break;
		default:
			break;
		}

		return dataset;
	}

	@Focus
	public void setFocus() {
	}

	@SuppressWarnings("restriction")
	@Inject
	@Optional
	void updateHandler(
			@UIEventTopic(EventConstants.RESULT_UPDATE_UPDATED) List<Data> data) {
		Object tmp = context.get(Constant.NEWPART);
		assert tmp != null;
		if (tmp instanceof Set<?>) {
			Set<String> newparts = (Set<String>) tmp;
			if (newparts.contains(partID)) {
				this.data = data;
				createDataSet(data);
			}
		}
	}

	//
	// @Inject
	// @Optional
	// void updateHandler(
	// @UIEventTopic(EventConstants.OBSERVER_UPDATE_UPDATED) String observer) {
	// Object metrics = context.get(Constant.METRIC);
	// if (metrics != null && metrics instanceof MetricModel) {
	//
	// }
	// }

	@PreDestroy
	private void dispose() {

	}

	public static String genPartID(METRIC metric, String observer) {
		switch (metric) {
		case Accuracy:
			return "accuracy" + "_" + observer.toLowerCase();
		case WorkerEstimationError:
			return "worker estimation error" + "_" + observer.toLowerCase();
		case ComputationTime:
			return "time" + "_" + observer.toLowerCase();
		default:
			throw new IllegalArgumentException(String.valueOf(metric));
		}
	}

}
