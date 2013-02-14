package CrowdBenchmark.parts;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import CrowdBenchmark.events.EventConstants;
import CrowdBenchmark.models.BenchmarkModel;
import CrowdBenchmark.models.Data;
import CrowdBenchmark.models.MetricModel;
import CrowdBenchmark.tools.io.CSVReader;
import CrowdBenchmark.tools.io.My_CsvWriter;
import CrowdBenchmark.util.Constant;
import experiments.Experiment_Evaluate;
import feedback.FeedBackModel;

public class ResultPart extends AbstractPart {
	private TableViewer tableViewer;
	private Button btnImport;
	private Button btnExport;
	private FormToolkit toolkit;
	private Form form;
	private FeedBackModel model;
	private MetricModel metrics;

	@Inject
	IEventBroker chartBroker;

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		GridData gd = new GridData();
		gd.horizontalSpan = 2;

		// Label searchLabel = new Label(parent, SWT.NONE);
		// searchLabel.setText("Search: ");
		// final Text searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
		// searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
		// | GridData.HORIZONTAL_ALIGN_FILL));
		new Label(parent, SWT.NONE);
		createViewer(parent);
		createButton(parent);
		new Label(parent, SWT.NONE);
		// importResult(Constant.RESULT_TEST_FILE);
	}

	private void createButton(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_composite.widthHint = 116;
		composite.setLayoutData(gd_composite);
		composite.setLayout(new GridLayout(2, false));
		/*
		 * { btnImport = new Button(composite, SWT.NONE);
		 * btnImport.setText("Import"); btnImport.addSelectionListener(new
		 * SelectionListener() {
		 * 
		 * @Override public void widgetDefaultSelected(SelectionEvent e) { }
		 * 
		 * @Override public void widgetSelected(SelectionEvent e) {
		 * importResult(); } }); }
		 */
		{
			btnExport = new Button(composite, SWT.NONE);
			btnExport.setText("Export");
			btnExport.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					exportResult();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub

				}
			});
		}

	}

	protected void importResult(String filename) {
		CSVReader reader = new CSVReader();
		reader.readfile(filename);
		updateTable(reader.getContent());
	}

	protected void updateResult(String filename, int evalID) {
		CSVReader reader = new CSVReader();
		reader.readfile(filename);
		appendTable(reader.getContent(), evalID);
	}

	protected void appendTable(List<Data> data, int evalID) {
		List<Data> currentData = (List<Data>) tableViewer.getInput();
		if (currentData == null)
			currentData = new ArrayList<Data>();
		for (Data datum : data) {
			datum.setEvalID(evalID + "");
			currentData.add(datum);
		}

		refreshTable(currentData);

	}

	private void refreshTable(List<Data> currentData) {
		tableViewer.setInput(currentData);
		tableViewer.refresh();
		chartBroker.post(EventConstants.RESULT_UPDATE_UPDATED, currentData);
	}

	protected void importResult() {
		FileDialog dlg = new FileDialog(btnImport.getShell(), SWT.OPEN);
		String[] filterExt = { "*.csv" };
		dlg.setFilterExtensions(filterExt);
		dlg.setText("Open");
		String path = dlg.open();
		if (path == null)
			return;
		// CSVReader reader = new CSVReader();
		// reader.readfile(path);
		// // System.out.println(reader.getContent());
		// updateTable(reader.getContent());
		importResult(path);
	}

	protected void exportResult() {
		FileDialog dlg = new FileDialog(btnExport.getShell(), SWT.SAVE);
		String[] filterExt = { "*.csv" };
		dlg.setFilterExtensions(filterExt);
		dlg.setText("Save");
		String path = dlg.open();
		if (path == null)
			return;
		List<Data> data = (List<Data>) tableViewer.getInput();
		TableColumn[] columns = tableViewer.getTable().getColumns();
		String title = columns[0].getText();
		for (int i = 1; i < columns.length; i++) {
			title += "\t" + columns[i].getText();
		}
		title += "\n";
		// System.out.println(title);
		// MyCsvWriter.getInstance().WriteToFile(data, path);
		My_CsvWriter.getInstance().WriteToFile(data, path, title);

	}

	private void createViewer(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(parent, tableViewer);
		final Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		tableViewer.setContentProvider(new ArrayContentProvider());
		// Get the content for the viewer, setInput will call getElements in the
		// contentProvider
		// updateTable(ResultProvider.INSTANCE.getData());

		// Make the selection available to other views
		// getSite().setSelectionProvider(tableViewer);
		// Set the sorter for the table

		// Layout the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		tableViewer.getControl().setLayoutData(gridData);

	}

	private void updateTable(List<Data> data) {
		List<Data> currentData = (List<Data>) tableViewer.getInput();
		if (currentData == null)
			currentData = new ArrayList<Data>();
		for (Data datum : data) {
			currentData.add(datum);
		}
		refreshTable(currentData);
	}

	private void createColumns(Composite parent, TableViewer tableViewer) {
		String[] titles = { "EvalID", "Answer Per Question", "Algorithm",
				"Worker Estimation Error", "Accuracy", "Completion time",
				"Factor value" };
		int[] bounds = { 100, 150, 100, 100, 100, 100, 100 };

		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Data p = (Data) element;
				return p.getEvalID();
			}
		});

		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Data p = (Data) element;
				return p.getAnswerPerQuestion();
			}
		});

		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Data p = (Data) element;
				return p.getAlgorithm();
			}
		});

		col = createTableViewerColumn(titles[3], bounds[3], 3);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Data p = (Data) element;
				return p.getWorkerEstimation();
			}
		});

		col = createTableViewerColumn(titles[4], bounds[4], 4);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Data p = (Data) element;
				return p.getAccuracy();
			}
		});

		col = createTableViewerColumn(titles[5], bounds[5], 5);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Data p = (Data) element;
				return p.getCompletionTime();
			}
		});

		col = createTableViewerColumn(titles[6], bounds[6], 6);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Data p = (Data) element;
				return p.getObserver();
			}
		});

	}

	private TableViewerColumn createTableViewerColumn(String title, int bound,
			int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(
				tableViewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	// @Inject
	// @Optional
	// void updateMetricHanlder(
	// @UIEventTopic(EventConstants.DATA_UPDATE_UPDATED) MetricModel metrics) {
	// this.metrics = metrics;
	// }

	@Inject
	@Optional
	void updateHandler(
			@UIEventTopic(EventConstants.DATA_UPDATE_UPDATED) BenchmarkModel model) {
		this.model = model.getModel();
		// run algorithms;
		if (model != null) {
			Experiment_Evaluate eval = new Experiment_Evaluate();
			eval.SetFileConfig(Constant.ALGO_CONFIG_FILE);
			eval.setModel(this.model);
			eval.run();
		} else {
			System.out.println("No input data");
		}
		updateResult(Constant.RESULT_FILE, model.getEvalID());
	}

	@Inject
	@Optional
	void updateHandler(@UIEventTopic(EventConstants.DATA_UPDATE_CLEAR) int num) {
		// importData(s + "");
		tableViewer.setInput(new ArrayList<Data>());
		tableViewer.refresh();
	}

	@Focus
	public void setFocus() {
		tableViewer.getControl().setFocus();
	}

	public TableViewer getTableViewer() {
		return tableViewer;
	}

	public void setTableViewer(TableViewer tableViewer) {
		this.tableViewer = tableViewer;
	}
}
