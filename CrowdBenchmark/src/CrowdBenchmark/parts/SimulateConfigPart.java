package CrowdBenchmark.parts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import CrowdBenchmark.events.EventConstants;
import CrowdBenchmark.models.Algorithm;
import CrowdBenchmark.models.AlgorithmModel;
import CrowdBenchmark.models.AlgorithmParameter;
import CrowdBenchmark.models.Answer;
import CrowdBenchmark.models.BenchmarkModel;
import CrowdBenchmark.models.Metric;
import CrowdBenchmark.models.MetricModel;
import CrowdBenchmark.models.SimulationParameter;
import CrowdBenchmark.tools.io.ConfigReader;
import CrowdBenchmark.tools.io.Crowd_TxtWriter;
import CrowdBenchmark.tools.io.TxtWriter;
import CrowdBenchmark.util.Constant;
import CrowdBenchmark.util.ContextUtil;
import CrowdBenchmark.validators.NumericValidator;
import experiments.Feedback_Simulating;
import feedback.FeedBack;
import feedback.FeedBackModel;

public class SimulateConfigPart extends AbstractPart {

	private FormToolkit toolkit;
	private Form form;
	private Text txtUniformSpammer;
	private Text txtRandomSpammer;
	private Text txtExpert;
	private Label lblNormalWorker;
	private Text txtNormalworker;
	private Label lblSloppyWorker;
	private Text txtSloppyWorker;
	private Label lblTotalworker;
	private Text txtNumOfQuestion;
	private Text txtNumOfCategories;
	private Text txtTrapQuestion;
	private Text txtMinCommonQuestion;
	private Text txtAnswerPerQuestion;
	private Text txtAnswerPerWorker;
	private Composite workerSectionBody;
	private AlgorithmParameter algoPara;
	private SimulationParameter simuPara;
	private AlgorithmModel selectedAlgorithm;
	private String lblObserver;

	private Link link;
	private Text txtMinvalue;
	private Text txtMaxvalue;
	private Text txtStep;
	private Label lblMin;
	private Label lblMax;
	private Label lblStep;
	private Combo combo;
	private ComboViewer comboViewer;

	final String[] observerValues = new String[] { "UniformSpammer", "Expert",
			"NormalWorker", "RandomSpammer", "SloppyWorker",
			"AnswerPerQuestion", "AnswerPerWorker" };

	Map<Text, String> txtBindding = new HashMap<Text, String>();
	Map<Button, String> btnBindding = new HashMap<Button, String>();
	Map<String, Text> strBindding = new HashMap<String, Text>();
	Map<TableItem, String> tblBindding = new HashMap<TableItem, String>();
	private NumericValidator validator = null;

	@Inject
	IEventBroker broker;

	@Inject
	IEventBroker algo;

	@Inject
	IEventBroker eval_start;

	@Inject
	IEventBroker worker;

	@Inject
	IEventBroker chart;

	@Inject
	IEclipseContext context;

	private DataBindingContext ctx;
	private Section sctnAlgorithm;
	private Composite algorithmComposite;
	private Section sctnMetric;
	private Composite metricComposite;
	private CheckboxTableViewer algorithmTableViewer;
	private CheckboxTableViewer metricTableViewer;
	private MetricModel selectedMetric;
	protected int index;
	private Composite composite_1;

	/**
	 * * This is a callback that will allow us to create the viewer and
	 * initialize * it.
	 */
	@PostConstruct
	public void createComposite(Composite parent) {
		algoPara = new AlgorithmParameter();
		simuPara = new SimulationParameter();
		selectedAlgorithm = new AlgorithmModel();
		selectedMetric = new MetricModel();
		toolkit = new FormToolkit(parent.getDisplay());

		parent.setLayout(new GridLayout(1, false));
		GridData gd = new GridData();
		gd.horizontalSpan = 2;

		addParameterFormPart(parent);

		// Start binding
		generateSimuParamBinding();
		generateObserverParamBinding();
		validator = new NumericValidator();
		bindSimuParamValues(validator);

		bindAlgoParamValues();
		bindMetricParamValues();

		algorithmTableViewer.setAllChecked(true);
		metricTableViewer.setAllChecked(true);

		// addValidator();
		// updateWorkerChart();

	}

	private void addParameterFormPart(final Composite parent) {
		form = toolkit.createForm(parent);
		form.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout layout = new GridLayout();
		form.getBody().setLayout(layout);
		layout.numColumns = 2;
		GridData gd = new GridData();
		gd.horizontalSpan = 2;

		Section workerSection = toolkit.createSection(form.getBody(),
				Section.CLIENT_INDENT | Section.TITLE_BAR);
		// gd_workerSection.widthHint = 335;
		workerSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		workerSection.setText("Crowd");
		customizeSection(workerSection);

		workerSectionBody = new Composite(workerSection, SWT.NONE);
		toolkit.adapt(workerSectionBody);
		toolkit.paintBordersFor(workerSectionBody);
		workerSection.setClient(workerSectionBody);
		createWorkerForm(workerSectionBody);

		Section questionSection = toolkit.createSection(form.getBody(),
				Section.CLIENT_INDENT | Section.TITLE_BAR);
		questionSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		questionSection.setText("Question");
		customizeSection(questionSection);

		Composite questionSectionBody = new Composite(questionSection, SWT.NONE);
		toolkit.adapt(questionSectionBody);
		toolkit.paintBordersFor(questionSectionBody);
		questionSection.setClient(questionSectionBody);
		createQuestionForm(questionSectionBody);

		Section observerSection = toolkit.createSection(form.getBody(),
				Section.TITLE_BAR);
		observerSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 2, 1));
		toolkit.paintBordersFor(observerSection);
		observerSection.setText("Factor");
		customizeSection(observerSection);

		Composite observerSectionBody = new Composite(observerSection, SWT.NONE);
		observerSection.setClient(observerSectionBody);
		toolkit.adapt(observerSectionBody);
		toolkit.paintBordersFor(observerSectionBody);
		observerSectionBody.setLayout(new GridLayout(5, false));

		new Label(observerSectionBody, SWT.NONE);

		lblMin = new Label(observerSectionBody, SWT.NONE);
		toolkit.adapt(lblMin, true, true);
		lblMin.setText("Min");

		lblMax = new Label(observerSectionBody, SWT.NONE);
		toolkit.adapt(lblMax, true, true);
		lblMax.setText("Max");

		lblStep = new Label(observerSectionBody, SWT.NONE);
		toolkit.adapt(lblStep, true, true);
		lblStep.setText("Step");
		new Label(observerSectionBody, SWT.NONE);

		comboViewer = new ComboViewer(observerSectionBody, SWT.READ_ONLY);
		combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		toolkit.paintBordersFor(combo);

		comboViewer.setContentProvider(new ArrayContentProvider()); // org.eclipse.jface.viewers.ArrayContentProvider()
		comboViewer.setLabelProvider(new LabelProvider()); // org.eclipse.jface.viewers.LabelProvider()

		combo.setItems(observerValues);
		// combo.setText(observerValues[0]);
		combo.setVisibleItemCount(observerValues.length);
		combo.addListener(SWT.Modify, new Listener() {

			@Override
			public void handleEvent(Event event) {
				index = combo.getSelectionIndex();
				// System.out.println(filterByText[index]);
				disableObserverText(observerValues[index]);

			}
		});

		txtMinvalue = new Text(observerSectionBody, SWT.BORDER);
		txtMinvalue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		toolkit.adapt(txtMinvalue, true, true);

		txtMaxvalue = new Text(observerSectionBody, SWT.BORDER);
		txtMaxvalue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		toolkit.adapt(txtMaxvalue, true, true);

		txtStep = new Text(observerSectionBody, SWT.BORDER);
		txtStep.setText("1");
		txtStep.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		toolkit.adapt(txtStep, true, true);
		new Label(observerSectionBody, SWT.NONE);

		addAlgorithmSection(parent);

		addMetricSection(parent);

		composite_1 = new Composite(form.getBody(), SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 1));
		toolkit.adapt(composite_1);
		toolkit.paintBordersFor(composite_1);

		final Button saveButton = new Button(composite_1, SWT.PUSH);
		saveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Save current config");
				FileDialog dlg = new FileDialog(saveButton.getShell(), SWT.SAVE);
				String[] filterExt = { "*.txt" };
				dlg.setFilterExtensions(filterExt);
				dlg.setText("Save");
				String path = dlg.open();
				if (path == null)
					return;
				saveConfig(path);
				// System.out.println("End simulating");
			}
		});
		saveButton.setText("Save Config");

		final Button loadConfig = new Button(composite_1, SWT.PUSH);
		loadConfig.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Load config");
				FileDialog dlg = new FileDialog(loadConfig.getShell(), SWT.OPEN);
				String[] filterExt = { "*.txt" };
				dlg.setFilterExtensions(filterExt);
				dlg.setText("Open");
				String path = dlg.open();
				if (path == null)
					return;
				loadConfig(path);
			}
		});
		loadConfig.setText("Load Config");

		link = new Link(form.getBody(), SWT.NONE);
		link.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1,
				1));
		toolkit.adapt(link, true, true);
		link.setText("<a>Advanced</a>");

		link.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				AdvanceConfig dialog = new AdvanceConfig(parent.getShell());
				dialog.create();
				if (dialog.open() == Window.OK) {
					System.out.println("save advance config changes to file");
				}
			}
		});

		// composite = toolkit.createComposite(form.getBody(), SWT.NONE);
		// composite = toolkit.createCompositeSeparator(form.getBody());

		// toolkit.paintBordersFor(composite);
		// new Label(form.getBody(), SWT.NONE);
		// addWorkerChart(parent);

	}

	private void addMetricSection(Composite parent) {
		sctnMetric = toolkit.createSection(form.getBody(), Section.COMPACT
				| Section.TITLE_BAR);
		sctnMetric.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		toolkit.adapt(sctnMetric);
		toolkit.paintBordersFor(sctnMetric);
		sctnMetric.setText("Metric");
		customizeSection(sctnMetric);

		metricComposite = new Composite(sctnMetric, SWT.NONE);
		toolkit.adapt(metricComposite);
		toolkit.paintBordersFor(metricComposite);
		sctnMetric.setClient(metricComposite);

		metricTableViewer = addCheckedTableViewer(toolkit, metricComposite);
		Metric selectAll = new Metric();
		selectAll.setName(Constant.SELECTALL);
		metricTableViewer.add(selectAll);
		for (Metric metric : selectedMetric.getMetrics()) {
			metricTableViewer.add(metric);
		}
		// Table table = metricTableViewer.getTable();
		// addSelectAllListener(table);
		// table.setSize(100, 100);
		// table.pack();
	}

	private CheckboxTableViewer addCheckedTableViewer(FormToolkit toolkit,
			Composite composite) {
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		composite.setLayout(tableColumnLayout);

		final CheckboxTableViewer checkboxTableViewer = CheckboxTableViewer
				.newCheckList(composite, SWT.BORDER | SWT.FULL_SELECTION
						| SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE);
		Table table = checkboxTableViewer.getTable();
		// table.setHeaderVisible(true);
		toolkit.paintBordersFor(table);
		// TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		// tableColumn.setText("Select All");
		// tableColumn.setImage(new Image(Display.getCurrent(),
		// "icons/checkbox.gif"));
		//
		// tableColumnLayout.setColumnData(tableColumn, new ColumnWeightData(20,
		// 150, true));
		// tableColumn.setWidth(150);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(table);
		// addSelectAllListener(table, tableItem);
		checkboxTableViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				boolean checked = event.getChecked();
				Object ele = event.getElement();
				if (Constant.SELECTALL.equalsIgnoreCase(ele.toString())) {
					if (checked) {
						checkboxTableViewer.setAllChecked(true);
					} else
						checkboxTableViewer.setAllChecked(false);
				}
			}
		});
		return checkboxTableViewer;

		// friendsViewer = CheckboxTableViewer.newCheckList(friendsComposite,
		// SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		//
		// Table friendsTable = friendsViewer.getTable();
		// friendsTable.setHeaderVisible(true);
		// friendsTable.setLinesVisible(true);
		// TableColumn friendNameColumn = new TableColumn(friendsTable,
		// SWT.NONE);
		// friendNameColumn.setText("Name");
		// friendsColumnLayout.setColumnData(friendNameColumn,
		// new ColumnWeightData(1));
		//
		// GridDataFactory.fillDefaults().grab(true, true)
		// .applyTo(friendsViewer.getTable());

	}

	private void addAlgorithmSection(Composite parent) {
		sctnAlgorithm = toolkit.createSection(form.getBody(), Section.COMPACT
				| Section.TITLE_BAR);
		sctnAlgorithm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		toolkit.adapt(sctnAlgorithm);
		toolkit.paintBordersFor(sctnAlgorithm);
		sctnAlgorithm.setText("Aggregate Technique");
		customizeSection(sctnAlgorithm);

		algorithmComposite = new Composite(sctnAlgorithm, SWT.NONE);
		toolkit.adapt(algorithmComposite);
		toolkit.paintBordersFor(algorithmComposite);
		sctnAlgorithm.setClient(algorithmComposite);

		algorithmTableViewer = addCheckedTableViewer(toolkit,
				algorithmComposite);

		Algorithm selectAll = new Algorithm();
		selectAll.setName(Constant.SELECTALL);
		algorithmTableViewer.add(selectAll);
		for (Algorithm algo : selectedAlgorithm.getAlgorithms()) {
			algorithmTableViewer.add(algo);
		}
		// Table table = algorithmTableViewer.getTable();
		// addSelectAllListener(table);
		// table.setSize(100, 100);
		// table.pack();
		// table.deselectAll();
	}

	private void addSelectAllListener(final Table table, TableItem tableItem) {
		tableItem.addListener(SWT.CHECK, new Listener() {
			@Override
			public void handleEvent(Event event) {
				System.out.println("abc");
				boolean checkBoxFlag = false;
				for (int i = 0; i < table.getItemCount(); i++) {
					if (table.getItems()[i].getChecked()) {
						checkBoxFlag = true;
					}
				}

				if (checkBoxFlag) {
					for (int m = 0; m < table.getItemCount(); m++) {
						table.getItems()[m].setChecked(false);
						// tableColumn0.setImage(new Image(Display.getCurrent(),
						// "icons/unchecked.gif"));

						table.deselectAll();

					}
				} else {
					for (int m = 0; m < table.getItemCount(); m++) {
						table.getItems()[m].setChecked(true);
						// tableColumn0.setImage(new Image(Display.getCurrent(),
						// "icons/checkbox.gif"));

						table.selectAll();
					}
				}

			}
		});

	}

	private void addSelectAllListener(final Table table) {
		final TableColumn tableColumn0 = table.getColumn(0);
		tableColumn0.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				boolean checkBoxFlag = false;
				for (int i = 0; i < table.getItemCount(); i++) {
					if (table.getItems()[i].getChecked()) {
						checkBoxFlag = true;
					}
				}

				if (checkBoxFlag) {
					for (int m = 0; m < table.getItemCount(); m++) {
						table.getItems()[m].setChecked(false);
						tableColumn0.setImage(new Image(Display.getCurrent(),
								"icons/unchecked.gif"));

						table.deselectAll();

					}
				} else {
					for (int m = 0; m < table.getItemCount(); m++) {
						table.getItems()[m].setChecked(true);
						tableColumn0.setImage(new Image(Display.getCurrent(),
								"icons/checkbox.gif"));

						table.selectAll();
					}
				}

			}
		});

	}

	protected void disableObserverText(String obververValue) {
		for (String key : strBindding.keySet()) {
			Text observerText = strBindding.get(key);
			if (!key.equalsIgnoreCase(obververValue)) {
				observerText.setEnabled(true);
			} else {
				lblObserver = key;
				strBindding.get(key).setEnabled(false);
			}
		}

	}

	private void generateSimuParamBinding() {
		txtBindding.put(txtUniformSpammer, "uniformSpammer");
		txtBindding.put(txtAnswerPerQuestion, "answerPerQuestion");
		txtBindding.put(txtAnswerPerWorker, "answerPerWorker");
		txtBindding.put(txtExpert, "expert");
		txtBindding.put(txtMinCommonQuestion, "minCommonQuestion");
		txtBindding.put(txtNormalworker, "normalWorker");
		txtBindding.put(txtNumOfCategories, "category");
		txtBindding.put(txtNumOfQuestion, "question");
		txtBindding.put(txtRandomSpammer, "randomSpammer");
		txtBindding.put(txtSloppyWorker, "sloppyWorker");
		txtBindding.put(txtTrapQuestion, "trapQuestion");
		txtBindding.put(txtMinvalue, "minObserverValue");
		txtBindding.put(txtMaxvalue, "maxObserverValue");
		txtBindding.put(txtStep, "stepObserverValue");
	}

	private void generateObserverParamBinding() {
		strBindding.put("uniformSpammer", txtUniformSpammer);
		strBindding.put("answerPerQuestion", txtAnswerPerQuestion);
		strBindding.put("answerPerWorker", txtAnswerPerWorker);
		strBindding.put("expert", txtExpert);
		strBindding.put("normalWorker", txtNormalworker);
		strBindding.put("randomSpammer", txtRandomSpammer);
		strBindding.put("sloppyWorker", txtSloppyWorker);
	}

	protected void loadConfig(String filename) {
		ConfigReader reader = new ConfigReader();
		reader.readfile(filename);
		Map<String, String> list = reader.getConfig();
		txtUniformSpammer.setText(list.get("uniformSpammer"));
		txtRandomSpammer.setText(list.get("randomSpammer"));
		txtExpert.setText(list.get("expert"));
		txtNormalworker.setText(list.get("normalWorker"));
		txtSloppyWorker.setText(list.get("sloppyWorker"));
		txtNumOfQuestion.setText(list.get("question"));
		txtNumOfCategories.setText(list.get("category"));
		txtTrapQuestion.setText(list.get("trapQuestion"));
		txtAnswerPerQuestion.setText(list.get("answerPerQuestion"));
		txtAnswerPerWorker.setText(list.get("answerPerWorker"));
		txtMinCommonQuestion.setText(list.get("minCommonQuestion"));

		combo.select(Integer.parseInt(list.get("observer")));
		txtMinvalue.setText(list.get("minObserverValue"));
		txtMaxvalue.setText(list.get("maxObserverValue"));
		txtStep.setText(list.get("step"));

		String[] algos = list.get("algorithms").split(",");
		algorithmTableViewer.setAllChecked(true);
		Object[] algorithms = algorithmTableViewer.getCheckedElements();
		algorithmTableViewer.setAllChecked(false);
		selectedAlgorithm.getAlgorithms().clear();
		for (int i = 0; i < algos.length; i++) {
			String name = algos[i];
			for (Object one_algo : algorithms) {
				if (((Algorithm) one_algo).getName().equals(name)) {
					algorithmTableViewer.setChecked(one_algo, true);
					selectedAlgorithm.getAlgorithms().add((Algorithm) one_algo);
				}
			}
		}

		String[] metricsContent = list.get("metrics").split(",");
		metricTableViewer.setAllChecked(true);
		Object[] metrics = metricTableViewer.getCheckedElements();
		metricTableViewer.setAllChecked(false);
		selectedMetric.getMetrics().clear();
		for (int i = 0; i < metricsContent.length; i++) {
			String name = metricsContent[i];

			for (Object one_metric : metrics) {
				if (((Metric) one_metric).getName().equals(name)) {
					metricTableViewer.setChecked(one_metric, true);
					selectedMetric.getMetrics().add((Metric) one_metric);
				}
			}
		}
	}

	protected String saveConfig(String filename) {
		// File outfile = new File(Constant.SIMULATE_INIT_FILE);
		File outfile = new File(filename);
		try {

			/*
			 * if (!(new File(outfile.getParent()).exists())) { (new
			 * File(outfile.getParent())).mkdirs(); }
			 */
			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
			Integer uniformSpammer = getValue(simuPara.getUniformSpammer());
			Integer randomSpammer = getValue(simuPara.getRandomSpammer());
			Integer expert = getValue(simuPara.getExpert());
			Integer normalWorker = getValue(simuPara.getNormalWorker());
			Integer sloppyWorker = getValue(simuPara.getSloppyWorker());

			bw.write("[Workers] \n" + "uniformSpammer = " + uniformSpammer
					+ "\n" + "randomSpammer = " + randomSpammer + "\n"
					+ "expert = " + expert + "\n" + "normalWorker = "
					+ normalWorker + "\n" + "sloppyWorker = " + sloppyWorker
					+ "\n" + "answerPerQuestion = "
					+ getValue(simuPara.getAnswerPerQuestion()) + "\n");

			bw.write("[Questions] \n" + "question = "
					+ getValue(simuPara.getQuestion()) + "\n" + "category = "
					+ getValue(simuPara.getCategory()) + "\n"
					+ "trapQuestion = " + getValue(simuPara.getTrapQuestion())
					+ "\n" + "minCommonQuestion = "
					+ getValue(simuPara.getMinCommonQuestion()) + "\n"
					+ "answerPerWorker = "
					+ getValue(simuPara.getAnswerPerWorker()) + "\n");
			bw.write("[Observer]\n" + "observer = "
					+ getValue(combo.getSelectionIndex()) + "\n"
					+ "minObserverValue = "
					+ getValue(simuPara.getMinObserverValue()) + "\n"
					+ "maxObserverValue = "
					+ getValue(simuPara.getMaxObserverValue()) + "\n"
					+ "step = " + getValue(simuPara.getStepObserverValue())
					+ "\n");
			String algorithms = "algorithms = ";
			Object[] algos = algorithmTableViewer.getCheckedElements();
			if (algos.length != 0) {
				algorithms += ((Algorithm) algos[0]).getName();
				for (int i = 1; i < algos.length; i++) {
					algorithms += "," + ((Algorithm) algos[i]).getName();
				}
			}
			algorithms += "\n";

			bw.write("[Algorithm]\n" + algorithms);

			String metricsContent = "metrics = ";
			Object[] metrics = metricTableViewer.getCheckedElements();
			if (metrics.length != 0) {
				metricsContent += ((Metric) metrics[0]).getName();
				for (int i = 1; i < metrics.length; i++) {
					metricsContent += "," + ((Metric) metrics[i]).getName();
				}
			}
			metricsContent += "\n";

			bw.write("[Metrics]\n" + metricsContent);

			bw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return outfile.getAbsolutePath();
	}

	protected String startSimulate() {
		File outfile = new File(Constant.CONFIG_FILE);
		try {

			/*
			 * if (!(new File(outfile.getParent()).exists())) { (new
			 * File(outfile.getParent())).mkdirs(); }
			 */
			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
			Integer totalWorker = getValue(simuPara.getUniformSpammer())
					+ getValue(simuPara.getRandomSpammer())
					+ getValue(simuPara.getExpert())
					+ getValue(simuPara.getNormalWorker())
					+ getValue(simuPara.getSloppyWorker());

			Integer workerTypeRatio = getValue(simuPara.getExpert())
					+ getValue(simuPara.getNormalWorker())
					+ getValue(simuPara.getSloppyWorker());
			String spammerRatio = "fix("
					+ getValue(simuPara.getRandomSpammer()) + "); " + "fix("
					+ getValue(simuPara.getUniformSpammer()) + "); " + "fix(0)";
			String workerRatio = "fix(" + getValue(simuPara.getExpert())
					+ "); " + "fix(" + getValue(simuPara.getNormalWorker())
					+ "); " + "fix(" + getValue(simuPara.getSloppyWorker())
					+ ") ";

			bw.write("[Workers] \n" + "total = " + totalWorker + "\n"
					+ "WorkerRatioType = " + workerTypeRatio + "\n"
					+ "spammerRatio = " + spammerRatio + "\n"
					+ "workersRatio = " + workerRatio + "\n"
					+ "feedbacksPerQuestion = "
					+ getValue(simuPara.getAnswerPerQuestion()) + "\n");

			String inputRatioLabels = "";
			if (getValue(simuPara.getCategory()) > 0) {
				Integer tmpRatio = (int) Math.floor(100 / getValue(simuPara
						.getCategory()));
				for (int i = 0; i < getValue(simuPara.getCategory()) - 1; i++) {
					inputRatioLabels += String.valueOf(tmpRatio) + "%; ";
					// inputRatioLabels += "100%;";
				}
			}
			inputRatioLabels += "100%";

			bw.write("[Questions] \n" + "NumOfQuestion = "
					+ getValue(simuPara.getQuestion()) + "\n"
					+ "InputNumberLabels = " + getValue(simuPara.getCategory())
					+ "\n" + "InputRatioLabels = " + inputRatioLabels + "\n"
					+ "HoneyPotRatio = " + getValue(simuPara.getTrapQuestion())
					+ "\n" + "ratioSubset = "
					+ getValue(simuPara.getMinCommonQuestion()) + "\n"
					+ "feedbacksPerWorker = "
					+ getValue(simuPara.getAnswerPerWorker()) + "\n");
			bw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * Feedback_Simulating simulate = new Feedback_Simulating();
		 * simulate.SetFileConfig(Constant.CONFIG_FILE); simulate.run();
		 * FeedBackModel model = simulate.getModel();
		 */
		/*
		 * String answerFile = Crowd_TxtWriter.getInstance().WriteToFile(
		 * formalizeAnswers(model), Constant.ANSWER_FILE);
		 * 
		 * broker.post(EventConstants.DATA_UPDATE_UPDATED, answerFile);
		 * algo.post(EventConstants.DATA_UPDATE_UPDATED, model);
		 */
		return outfile.getAbsolutePath();
	}

	private String formalizeAnswers(FeedBackModel model, int evalID) {
		List<Answer> answers = new ArrayList<Answer>();
		String title = "evalID" + "Worker" + "\t" + "Question" + "\t"
				+ "Answer" + "\n";
		String content = title;
		for (FeedBack onefeedback : model.getListFeedBacks()) {
			content += evalID + "\t" + onefeedback.getWorker().getWID() + "\t"
					+ onefeedback.getQuestion().getCorrespondence().getValue()
					+ "\t" + onefeedback.getAnswer() + "\n";
		}
		return content;
	}

	public static Integer getValue(Integer i) {
		return i != null ? i : 0;
	}

	/** * Passing the focus request to the viewer's control. */

	public void createWorkerForm(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		Label lblUniformSpammer = new Label(parent, SWT.NONE);
		lblUniformSpammer.setText("Uniform Spammer");

		txtUniformSpammer = new Text(parent, SWT.BORDER);
		txtUniformSpammer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		Label lblRandomSpammer = new Label(parent, SWT.NONE);
		lblRandomSpammer.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		lblRandomSpammer.setText("Random Spammer");

		txtRandomSpammer = new Text(parent, SWT.BORDER);
		txtRandomSpammer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label lblExpert = new Label(parent, SWT.NONE);
		lblExpert.setText("Expert");

		txtExpert = new Text(parent, SWT.BORDER);
		txtExpert.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		lblNormalWorker = new Label(parent, SWT.NONE);
		lblNormalWorker.setText("Normal Worker");

		txtNormalworker = new Text(parent, SWT.BORDER);
		txtNormalworker.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		lblSloppyWorker = new Label(parent, SWT.NONE);
		lblSloppyWorker.setText("Sloppy Worker");

		txtSloppyWorker = new Text(parent, SWT.BORDER);
		txtSloppyWorker.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		new Label(workerSectionBody, SWT.NONE);

		lblTotalworker = new Label(parent, SWT.NONE);
		lblTotalworker.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
	}

	public void createQuestionForm(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		Label lblNumOfQuestion = new Label(parent, SWT.NONE);
		lblNumOfQuestion.setText("Question");

		txtNumOfQuestion = new Text(parent, SWT.BORDER);
		txtNumOfQuestion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label lblNumOfCategories = new Label(parent, SWT.NONE);
		lblNumOfCategories.setText("Categories");

		txtNumOfCategories = new Text(parent, SWT.BORDER);
		txtNumOfCategories.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		Label lblTrapQuestion = new Label(parent, SWT.NONE);
		lblTrapQuestion.setText("Trap Question");

		txtTrapQuestion = new Text(parent, SWT.BORDER);
		txtTrapQuestion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label lblMinCommonQuestion = new Label(parent, SWT.NONE);
		lblMinCommonQuestion.setText("Min Common Question");

		txtMinCommonQuestion = new Text(parent, SWT.BORDER);
		txtMinCommonQuestion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		Label lblAnswerPerQuestion = new Label(parent, SWT.NONE);
		lblAnswerPerQuestion.setText("Answer Per Question");

		txtAnswerPerQuestion = new Text(parent, SWT.BORDER);
		txtAnswerPerQuestion.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		Label lblAnswerPerWorker = new Label(parent, SWT.NONE);
		lblAnswerPerWorker.setText("Answer Per Worker");

		txtAnswerPerWorker = new Text(parent, SWT.BORDER);
		txtAnswerPerWorker.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
	}

	@Focus
	public void setFocus() {
		form.setFocus();
	}

	private void bindSimuParamValues(IValidator validator) {
		if (ctx != null) {
			ctx.dispose();
		}
		ctx = new DataBindingContext();

		if (simuPara.getIndex() != null) {
			int index = simuPara.getIndex();
			combo.select(index);
		}

		for (Text key : txtBindding.keySet()) {
			IObservableValue widgetValue = WidgetProperties.text(SWT.Modify)
					.observe(key);
			IObservableValue modelValue = BeanProperties.value(
					SimulationParameter.class, txtBindding.get(key)).observe(
					simuPara);
			if (validator == null) {
				ctx.bindValue(widgetValue, modelValue);
			} else {
				UpdateValueStrategy strategy = new UpdateValueStrategy();
				strategy.setAfterGetValidator(validator);
				// strategy.setBeforeSetValidator(validator);
				// strategy.setAfterConvertValidator(validator);
				// strategy.setConverter(new Converter(String.class,
				// Integer.class) {
				//
				// @Override
				// public Object convert(Object o) {
				// if (o == null) {
				// return 0;
				// }
				// if (o instanceof String) {
				// System.out.println("Convert");
				// String str = (String) o;
				// if (str.trim().isEmpty()) {
				// return 0;
				// }
				// return Integer.valueOf(str);
				// }
				// return o;
				// }
				//
				// });
				Binding bindValue = ctx.bindValue(widgetValue, modelValue,
						strategy, null);
				// Add some decorations
				ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.RIGHT);

			}
		}

	}

	void updateWorkerChart() {
		Map<Text, String> workerBindding = new HashMap<Text, String>();
		workerBindding.put(txtUniformSpammer, "uniformSpammer");
		workerBindding.put(txtRandomSpammer, "randomSpammer");
		workerBindding.put(txtExpert, "expert");
		workerBindding.put(txtNormalworker, "normalWorker");
		workerBindding.put(txtSloppyWorker, "sloppyWorker");
		for (Text key : workerBindding.keySet()) {
			key.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent event) {
					worker.post(EventConstants.DATA_UPDATE_UPDATED, simuPara);
				}
			});
		}
	}

	@Inject
	@Optional
	void updateHandler(
			@UIEventTopic(EventConstants.DATA_SIMULATING_START) String s) {
		// start simulate
		System.out.println("Simulating answers");
		startSimulate();
		broker.send(EventConstants.DATA_UPDATE_CLEAR, 0);

		Integer minvalue = simuPara.getMinObserverValue();
		Integer maxValue = simuPara.getMaxObserverValue();
		Integer step = simuPara.getStepObserverValue();
		if (minvalue == null || maxValue == null) {
			// throw new IllegalArgumentException();
			return;
		}
		int interval = maxValue - minvalue;
		String attribute = lblObserver;

		int start = minvalue;
		int evalID = 0;
		while (start <= maxValue) {

			int value = start;
			// System.out.println("value: " + value + ", key: " + attribute);
			// test(initHashMap(value, attribute));
			evaluate(initHashMap(value, attribute), evalID);
			start += step;
			evalID++;
		}
	}

	@Inject
	@Optional
	void updateMetricHandler(
			@UIEventTopic(EventConstants.METRIC_OBSERVER_UPDATE_UPDATED) String s) {
		// start simulate
		System.out.println("Update metrics to context");
		ContextUtil.updateContext(context, Constant.METRIC, selectedMetric);

		System.out.println("Update observer to context");
		ContextUtil.updateContext(context, Constant.OBSERVER,
				observerValues[index]);

	}

	private void test(HashMap<String, String> listConfig) {
		for (String key : listConfig.keySet()) {
			System.out.println(key + " = " + listConfig.get(key));
		}
	}

	private void evaluate(HashMap<String, String> listConfig, int evalID) {
		/*
		 * for(String key : listConfig.keySet()){ System.out.println(key + " = "
		 * + listConfig.get(key)); }
		 */
		Feedback_Simulating simulate = new Feedback_Simulating(listConfig);
		for (String key : listConfig.keySet()) {
			System.out.println("key: " + key + ", value: " + listConfig.get(key) );
		}
		simulate.SetFileConfig(Constant.CONFIG_FILE);
		simulate.run();
		FeedBackModel model = simulate.getModel();
		String answerFile = Crowd_TxtWriter.getInstance().WriteToFile(
				formalizeAnswers(model, evalID), Constant.ANSWER_FILE);
		broker.send(EventConstants.DATA_UPDATE_UPDATED, answerFile);
		BenchmarkModel benchmarkModel = new BenchmarkModel(evalID, model);
		// chart.send(EventConstants.OBSERVER_UPDATE_UPDATED,
		// observerValues[index]);
		algo.send(EventConstants.DATA_UPDATE_UPDATED, benchmarkModel);
	}

	private HashMap<String, Integer> initParams() {
		HashMap<String, Integer> params = new HashMap<String, Integer>();
		// workers
		params.put("uniformSpammer", getValue(simuPara.getUniformSpammer()));
		params.put("randomSpammer", getValue(simuPara.getRandomSpammer()));
		params.put("expert", getValue(simuPara.getExpert()));
		params.put("normalWorker", getValue(simuPara.getNormalWorker()));
		params.put("sloppyWorker", getValue(simuPara.getSloppyWorker()));
		params.put("answerPerQuestion",
				getValue(simuPara.getAnswerPerQuestion()));

		// questions
		params.put("question", getValue(simuPara.getQuestion()));
		params.put("category", getValue(simuPara.getCategory()));
		params.put("trapQuestion", getValue(simuPara.getTrapQuestion()));
		params.put("minCommonQuestion",
				getValue(simuPara.getMinCommonQuestion()));
		params.put("answerPerWorker", getValue(simuPara.getAnswerPerWorker()));
		return params;
	}

	private HashMap<String, String> initHashMap(int value, String key) {
		HashMap<String, String> listConfig = new HashMap<String, String>();
		HashMap<String, Integer> params = initParams();

		// System.out.println("value: " + value + ", key: " + key);
		// System.out.println("answerPerQuestion: " +
		// params.get("answerPerQuestion"));
		params.put(key, value);

		Integer totalWorker = params.get("uniformSpammer")
				+ params.get("randomSpammer") + params.get("expert")
				+ params.get("normalWorker") + params.get("sloppyWorker");

		Integer workerTypeRatio = params.get("expert")
				+ params.get("normalWorker") + params.get("sloppyWorker");

		String spammerRatio = "fix(" + params.get("randomSpammer") + "); "
				+ "fix(" + params.get("uniformSpammer") + "); " + "fix(0)";

		String workerRatio = "fix(" + params.get("expert") + "); " + "fix("
				+ params.get("normalWorker") + "); " + "fix("
				+ params.get("sloppyWorker") + ") ";

		listConfig.put("observer", key);
		listConfig.put("observer_" + key, value + "");

		listConfig.put("total", totalWorker + "");
		listConfig.put("WorkerRatioType", workerTypeRatio + "");
		listConfig.put("spammerRatio", spammerRatio + "");
		listConfig.put("workerRatio", workerRatio + "");
		listConfig.put("feedbacksPerQuestion", params.get("answerPerQuestion")
				+ "");

		String inputRatioLabels = "";
		if (params.get("category") > 0) {
			Integer tmpRatio = (int) Math.floor(100 / params.get("category"));
			for (int i = 0; i < params.get("category") - 1; i++) {
				inputRatioLabels += String.valueOf(tmpRatio) + "%; ";
			}
		}
		inputRatioLabels += "100%";

		listConfig.put("NumOfQuestion", params.get("question") + "");
		listConfig.put("InputNumberLabels", params.get("category") + "");
		//if multilabel
		listConfig.put("InputDataType", "Binary");
		int numLabels = params.get("category");
		if(numLabels > 2) listConfig.put("InputDataType", "Multiple");

		
		listConfig.put("InputRatioLabels", inputRatioLabels);
		listConfig.put("HoneyPotRatio", params.get("trapQuestion") + "");
		listConfig.put("ratioSubset", params.get("minCommonQuestion") + "");
		listConfig
				.put("feedbacksPerWorker", params.get("answerPerWorker") + "");
		return listConfig;

	}

	private void addValidator() {
		for (Text key : txtBindding.keySet()) {
			key.addListener(SWT.Verify, new Listener() {

				@Override
				public void handleEvent(Event e) {
					String string = e.text;
					char[] chars = new char[string.length()];
					string.getChars(0, chars.length, chars, 0);
					for (int i = 0; i < chars.length; i++) {
						if (!('0' <= chars[i] && chars[i] <= '9')) {
							e.doit = false;
							return;
						}
					}

				}
			});
		}
	}

	@Inject
	@Optional
	void evaluateHandler(
			@UIEventTopic(EventConstants.FUNCTION_SIMULATING_START) String s) {
		System.out.println("Start Evaluating...");
		startEvaluating();
		// System.out.println(selectedAlgorithm.toString());
		System.out.println("End Evaluating");
	}

	protected void startEvaluating() {
		TxtWriter.getInstance().createDirIfNotExist(Constant.CONFIG_FOLDER);
		File outfile = new File(Constant.ALGO_CONFIG_FILE);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));

			String tmp = selectedAlgorithm.toString();
			System.out.println(tmp);
			bw.write("[Algorithm] \n");
			bw.write("algorithms = \"" + tmp + "\" \n");
			bw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		// sent signal to simulate model
		eval_start.send(EventConstants.DATA_SIMULATING_START, "start");

		// run algorithms;
		/*
		 * if (model != null) { Experiment_Evaluate eval = new
		 * Experiment_Evaluate(); eval.SetFileConfig(Constant.ALGO_CONFIG_FILE);
		 * eval.setModel(model); eval.run(); } else {
		 * System.out.println("No input data"); }
		 * importResult(Constant.RESULT_FILE);
		 */

	}

	private void bindAlgoParamValues() {
		DataBindingContext ctx = new DataBindingContext();
		IObservableSet modelSet = BeansObservables.observeSet(
				Realm.getDefault(), selectedAlgorithm, "algorithms");
		// ViewerSupport.bind(checkboxTableViewer, modelSet,
		// BeanProperties.value(Algorithm.class, "name"));

		IObservableSet widgetSet = ViewersObservables.observeCheckedElements(
				algorithmTableViewer, Algorithm.class);
		// modelSet = BeansObservables.obser(Realm.getDefault(),
		// selectedAlgorithm, "algorithms");

		ctx.bindSet(widgetSet, modelSet);

		// .observe(key);
		// ctx.bindSet(widgetSet, modelSet);
		// IObservableValue modelValue = BeanProperties.value(
		// AlgorithmParameter.class, btnBindding.get(key)).observe(
		// algoPara);

	}

	private void bindMetricParamValues() {
		DataBindingContext ctx = new DataBindingContext();
		IObservableSet modelSet = BeansObservables.observeSet(
				Realm.getDefault(), selectedMetric, "metrics");
		IObservableSet widgetSet = ViewersObservables.observeCheckedElements(
				metricTableViewer, Metric.class);

		ctx.bindSet(widgetSet, modelSet);
	}
}
