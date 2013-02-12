package CrowdBenchmark.parts;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import tools.io.AnswersReader;
import tools.io.Crowd_TxtWriter;
import CrowdBenchmark.events.EventConstants;
import CrowdBenchmark.models.Answer;

public class AnswerPart extends AbstractPart {
	private TableViewer tableViewer;
	private Button btnImport;
	private Button btnExport;

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		// Label searchLabel = new Label(parent, SWT.NONE);
		// searchLabel.setText("Search: ");
		// final Text searchText = new Text(parent, SWT.BORDER | SWT.SEARCH);
		// searchText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
		// | GridData.HORIZONTAL_ALIGN_FILL));

		createViewer(parent);
		createButton(parent);
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
		 * @Override public void widgetSelected(SelectionEvent e) { FileDialog
		 * dlg = new FileDialog(btnImport.getShell(), SWT.OPEN); String[]
		 * filterExt = { "*.txt" }; dlg.setFilterExtensions(filterExt);
		 * dlg.setText("Open"); String path = dlg.open(); importData(path); }
		 * }); }
		 */
		{
			btnExport = new Button(composite, SWT.NONE);
			btnExport.setText("Export");
			btnExport.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				@Override
				public void widgetSelected(SelectionEvent e) {
					// System.out.println(tableViewer.getInput());
					FileDialog dlg = new FileDialog(btnExport.getShell(),
							SWT.SAVE);
					String[] filterExt = { "*.txt" };
					dlg.setFilterExtensions(filterExt);
					dlg.setText("Save");
					String path = dlg.open();
					if (path == null)
						return;
					List<Answer> answers = (List<Answer>) tableViewer
							.getInput();
					Crowd_TxtWriter.getInstance().WriteToFile(answers, path);
				}
			});
		}

	}

	protected void importData(String path) {
		if (path == null)
			return;
		AnswersReader reader = new AnswersReader();
		reader.readfile(path);
		List<Answer> data = appendData(reader.getAnswers());
		// System.out.println(reader.getAnswers());
		tableViewer.setInput(data);
		tableViewer.refresh();

	}

	protected List<Answer> appendData(List<Answer> data) {
		List<Answer> currentData = (List<Answer>) tableViewer.getInput();
		System.out.println(data.size());
		for (Answer answer : data) {
			currentData.add(answer);
		}
		return currentData;
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
		// tableViewer.setInput(ModelProvider.INSTANCE.getAnswers());
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

	private void createColumns(Composite parent, TableViewer tableViewer) {
		String[] titles = { "EvalID", "Worker", "Question", "Answer" };
		int[] bounds = { 100, 100, 100, 100 };

		// column is for the evalID
		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Answer p = (Answer) element;
				return p.getEvalID() + "";
			}
		});

		// First column is for the worker
		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Answer p = (Answer) element;
				return p.getWorker();
			}
		});

		// Second column is for the question
		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Answer p = (Answer) element;
				return p.getQuestion();
			}
		});

		// Third column is for the answer
		col = createTableViewerColumn(titles[3], bounds[3], 3);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Answer p = (Answer) element;
				return p.getAnswer();
			}
		});

	}

	@Inject
	@Optional
	void updateHandler(@UIEventTopic(EventConstants.DATA_UPDATE_CLEAR) int num) {
		// importData(s + "");
		tableViewer.setInput(new ArrayList<Answer>());
		tableViewer.refresh();
	}

	@Inject
	@Optional
	void updateHandler(
			@UIEventTopic(EventConstants.DATA_UPDATE_UPDATED) String path) {
		importData(path);
		// System.out.println(path);
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
