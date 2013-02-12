package CrowdBenchmark.parts;

import java.awt.Font;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.experimental.chart.swt.ChartComposite;

import CrowdBenchmark.events.EventConstants;
import CrowdBenchmark.models.SimulationParameter;

public class WorkerChartPart {

	private SimulationParameter simuPara;
	private DefaultPieDataset dataset;

	@Inject
	@Optional
	void updateHandler(
			@UIEventTopic(EventConstants.DATA_UPDATE_UPDATED) SimulationParameter simuPara) {
		this.simuPara = simuPara;
		createDataSet();
	}

	@PostConstruct
	public void createComposite(Composite parent) {
		JFreeChart chart = createChart(createDataSet());
		ChartComposite chartComposite = new ChartComposite(parent, SWT.NONE,
				chart, true);
		chartComposite.setDisplayToolTips(true);
		chartComposite.setHorizontalAxisTrace(false);
		chartComposite.setVerticalAxisTrace(false);
	}

	private JFreeChart createChart(PieDataset dataset) {
		JFreeChart chart = ChartFactory.createPieChart("Worker Population", // chart
																			// title
				dataset, // data
				true, // include legend
				true, false);

		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSectionOutlinesVisible(false);
		plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
		plot.setNoDataMessage("No data available");
		plot.setCircular(false);
		plot.setLabelGap(0.02);
		return chart;
	}

	private PieDataset createDataSet() {
		if (dataset == null) {
			dataset = new DefaultPieDataset();
		} else {
			dataset.clear();
			Integer uniformSpammer = SimulateConfigPart.getValue(simuPara
					.getUniformSpammer());
			Integer randomSpammer = SimulateConfigPart.getValue(simuPara
					.getRandomSpammer());
			Integer expert = SimulateConfigPart.getValue(simuPara.getExpert());
			Integer normalWorker = SimulateConfigPart.getValue(simuPara
					.getNormalWorker());
			Integer sloppyWorker = SimulateConfigPart.getValue(simuPara
					.getSloppyWorker());
			dataset.setValue("Uniform Spammer", uniformSpammer);
			dataset.setValue("Random Spammer", randomSpammer);
			dataset.setValue("Expert", expert);
			dataset.setValue("Normal Worker", normalWorker);
			dataset.setValue("Sloppy Worker", sloppyWorker);
		}
		return dataset;
	}

}
