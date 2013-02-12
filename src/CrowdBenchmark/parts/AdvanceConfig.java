package CrowdBenchmark.parts;

import java.util.HashMap;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import CrowdBenchmark.models.AdvanceConfigModel;
import CrowdBenchmark.models.Algorithm;
import CrowdBenchmark.models.CommunityAdvanceConfigModel;
import CrowdBenchmark.models.WorkerAdvanceConfigModel;
import CrowdBenchmark.util.Constant;
import org.eclipse.swt.graphics.Point;

public class AdvanceConfig extends TitleAreaDialog {
	private AdvanceConfigModel advanceConfig;

	private Combo expert_combo;
	private Combo normalWorker_combo;
	private Combo sloppyWorker_combo;

	private Text txtEM;
	private Text txtIter;
	private Text txtSLME;

	private Text expert_fisrtParam;
	private Text expert_secondParam;
	private Text normalWorker_fisrtParam;
	private Text normalWorker_secondParam;
	private Text sloppyWorker_fisrtParam;
	private Text sloppyWorker_secondParam;

	private FormToolkit toolkit;
	private Form form;
	
	private Composite expert_section;
	private Composite expert_composite;
	private Composite normalWorker_section;
	private Composite normalWorker_composite;
	private Composite sloppyWorker_section;
	private Composite sloppyWorker_composite;

	final String[] feedbacksDistributor = new String[] { "FeedBacks Per Question Distributor",
			"FeedBacks Per Worker Distributor", "FeedBacks Constraint Distributor" };
	final String[] observerValues = new String[] { "Normal Distribution",
			"Fix Distribution", "Uniform Distribution" };

	protected int expert_index;
	protected int normalWorker_index;
	protected int sloppyWorker_index;

	private Section sctnAlgorithm;
	private Composite algorithmComposite;
	
	private Section sctnFeedbacks;
	private Composite feedbacksComposite;
	private Combo feedbacks_combo;
	private Text txtFeedbacksRatio;
	private Text txtFeedbacksRatioQuestion;

	private int feedbacks_index;

	public AdvanceConfig(Shell parentShell) {
		super(parentShell);
		setBlockOnOpen(false);
	}

	@Override
	public void create() {
		super.create();
		// Set the title
		setTitle("Advance Configuration\r\n");
		// Set the message
		//setMessage("This is a TitleAreaDialog", IMessageProvider.INFORMATION);

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		// layout.horizontalAlignment = GridData.FILL;
		parent.setLayout(layout);

		// The text fields will grow with the size of the dialog
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		initValue();
		addParameterFormPart(parent);

		return parent;
	}
	
	private void initValue(){
		advanceConfig = new AdvanceConfigModel();
		advanceConfig.readAdvanceConfigFile(Constant.ADVANCE_CONFIG_FILE);
	}

	private void addParameterFormPart(final Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		form.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		GridLayout layout = new GridLayout();
		layout.marginWidth = 2;
		layout.horizontalSpacing = 0;
		form.getBody().setLayout(layout);
		layout.numColumns = 2;
		GridData gd = new GridData();
		gd.horizontalSpan = 2;

		addWorkerSection(parent);
		addAlgorithmSection(parent);
		addFeedbacksSection(parent);
	}

	private void addWorkerSection(Composite parent) {
		Section observerSection = toolkit.createSection(form.getBody(),
				Section.TITLE_BAR);
		GridData gd_observerSection = new GridData(SWT.FILL, SWT.TOP, true,
				true, 1, 2);
		gd_observerSection.widthHint = 360;
		observerSection.setLayoutData(gd_observerSection);
		toolkit.paintBordersFor(observerSection);
		observerSection.setText("Worker");
		
		Composite workerHolder = new Composite(observerSection,SWT.NONE);
		GridLayout gl_workerHolder = new GridLayout(1, true);
		gl_workerHolder.verticalSpacing = 0;
		gl_workerHolder.marginBottom = 5;
		workerHolder.setLayout(gl_workerHolder);
		observerSection.setClient(workerHolder);
		toolkit.adapt(workerHolder);
		toolkit.paintBordersFor(workerHolder);

		expert_section = new Composite(workerHolder,
				SWT.NONE);
		expert_section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_expert_section = new GridLayout(2, false);
		gl_expert_section.verticalSpacing = 0;
		gl_expert_section.marginHeight = 0;
		expert_section.setLayout(gl_expert_section);

		addExpertDistribution();
		
		normalWorker_section = new Composite(workerHolder,
				SWT.NONE);
		normalWorker_section.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		GridLayout gl_normalWorker_section = new GridLayout(2, false);
		gl_normalWorker_section.verticalSpacing = 0;
		gl_normalWorker_section.marginHeight = 0;
		normalWorker_section.setLayout(gl_normalWorker_section);

		// new Label(observerSectionBody, SWT.NONE);
		addNormalWorkerDistribution();
		
		sloppyWorker_section = new Composite(workerHolder,
				SWT.NONE);
		sloppyWorker_section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		GridLayout gl_sloppyWorker_section = new GridLayout(2, false);
		gl_sloppyWorker_section.verticalSpacing = 0;
		gl_sloppyWorker_section.marginHeight = 0;
		sloppyWorker_section.setLayout(gl_sloppyWorker_section);

		// new Label(observerSectionBody, SWT.NONE);
		addSloppyWorkerDistribution();

	}
	
	private int getIndex(Constant.DISTRIBUTION distribution){
		switch (distribution) {
		case NormalDistribution:
			return 0;
		case UniformDistribution:
			return 2;
		case FixedDistribution:
			return 1;	
		default:
			return 3;
		}
	}
	
	private int getIndexofFeedbacksDistributor(Constant.FEEDBACKSDISTRIBUTION distribution){
		switch (distribution) {
		case FeedBacksPerQuestionDistributor:
			return 0;
		case FeedBacksPerWorkerDistributor:
			return 1;
		default:
			return 2;
		}
	}

	private void addExpertDistribution() {
		Composite distribution = new Composite(expert_section, SWT.NONE);
		distribution.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		toolkit.adapt(distribution);
		toolkit.paintBordersFor(distribution);
		distribution.setLayout(new GridLayout(1, false));

		Label expert = new Label(distribution, SWT.NONE);
		expert.setLayoutData(new GridData(SWT.LEFT, SWT.DOWN, true, false, 1,
				1));
		expert.setText("Expert");

		ComboViewer comboViewer = new ComboViewer(distribution, SWT.READ_ONLY);
		expert_combo = comboViewer.getCombo();
		expert_combo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1,
				1));
		toolkit.paintBordersFor(expert_combo);

		comboViewer.setContentProvider(new ArrayContentProvider()); // org.eclipse.jface.viewers.ArrayContentProvider()
		comboViewer.setLabelProvider(new LabelProvider()); // org.eclipse.jface.viewers.LabelProvider()

		expert_combo.setItems(observerValues);
		// combo.setText(observerValues[0]);
		expert_combo.setVisibleItemCount(observerValues.length);
		expert_index = getIndex(Constant.DISTRIBUTION.valueOf(advanceConfig.getWorkers().getExpert().getSelectedDistribution()));
		expert_combo.select(expert_index);
		addExpertParameters();
		expert_combo.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event event) {
				expert_index = expert_combo.getSelectionIndex();
				System.out.println(expert_combo.getItem(expert_index));
				if(expert_composite != null) expert_composite.dispose();
				addExpertParameters();
			}
		});
	}

	private void addExpertParameters() {
		expert_composite = new Composite(expert_section, SWT.NONE);
		expert_composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		toolkit.adapt(expert_composite);
		toolkit.paintBordersFor(expert_composite);
		expert_composite.setLayout(new GridLayout(2, true));
		
		Label firstParams = new Label(expert_composite, SWT.NONE);
		firstParams.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1));
		toolkit.adapt(firstParams, true, true);
		//firstParams.setText("mean");

		Label secondParam = new Label(expert_composite, SWT.NONE);
		secondParam.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1));
		toolkit.adapt(secondParam, true, true);
		//secondParam.setText("sd");

		expert_fisrtParam = new Text(expert_composite, SWT.BORDER);
		expert_fisrtParam.setSize(new Point(3000, 0));
		GridData gd_expert_fisrtParam = new GridData(SWT.LEFT, SWT.CENTER, false, true,
				1, 1);
		gd_expert_fisrtParam.widthHint = 60;
		expert_fisrtParam.setLayoutData(gd_expert_fisrtParam);

		expert_secondParam = new Text(expert_composite, SWT.BORDER);
		expert_secondParam.setSize(new Point(500, 0));
		GridData gd_expert_secondParam = new GridData(SWT.LEFT, SWT.CENTER, false, true, 1,
				1);
		gd_expert_secondParam.widthHint = 60;
		expert_secondParam.setLayoutData(gd_expert_secondParam);
		expert_secondParam.setVisible(true);
		
		int index = expert_combo.getSelectionIndex();
		switch (index) {
		case 0:
			firstParams.setText("Mean");
			secondParam.setText("Sd");
			expert_fisrtParam.setText(advanceConfig.getWorkers().getExpert().getMean());
			expert_secondParam.setText(advanceConfig.getWorkers().getExpert().getSd());
			break;
		case 1:
			firstParams.setText("Reliability");
			secondParam.setText("");
			expert_fisrtParam.setText(advanceConfig.getWorkers().getExpert().getFixed());
			expert_secondParam.setVisible(false);
			break;
		case 2:
			firstParams.setText("Lower Bound");
			secondParam.setText("Upper Bound");
			expert_fisrtParam.setText(advanceConfig.getWorkers().getExpert().getLowBound());
			expert_secondParam.setText(advanceConfig.getWorkers().getExpert().getUpBound());
			break;

		default:
			break;
		}
		expert_section.layout();
	}
	private void addNormalWorkerDistribution() {
		Composite distribution = new Composite(normalWorker_section, SWT.NONE);
		toolkit.adapt(distribution);
		toolkit.paintBordersFor(distribution);
		distribution.setLayout(new GridLayout(1, false));

		Label normalWorker = new Label(distribution, SWT.NONE);
		normalWorker.setLayoutData(new GridData(SWT.LEFT, SWT.DOWN, false, false, 1,
				1));
		normalWorker.setText("Normal Worker");

		ComboViewer comboViewer = new ComboViewer(distribution, SWT.READ_ONLY);
		normalWorker_combo = comboViewer.getCombo();
		normalWorker_combo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1,
				1));
		toolkit.paintBordersFor(normalWorker_combo);

		comboViewer.setContentProvider(new ArrayContentProvider()); // org.eclipse.jface.viewers.ArrayContentProvider()
		comboViewer.setLabelProvider(new LabelProvider()); // org.eclipse.jface.viewers.LabelProvider()

		normalWorker_combo.setItems(observerValues);
		// combo.setText(observerValues[0]);
		normalWorker_combo.setVisibleItemCount(observerValues.length);
		normalWorker_index = getIndex(Constant.DISTRIBUTION.valueOf(advanceConfig.getWorkers().getNormal().getSelectedDistribution()));
		normalWorker_combo.select(normalWorker_index);
		addNormalWorkerParameters();
		normalWorker_combo.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event event) {
				normalWorker_index = normalWorker_combo.getSelectionIndex();
				System.out.println(normalWorker_combo.getItem(normalWorker_index));
				if(normalWorker_composite != null) normalWorker_composite.dispose();
				addNormalWorkerParameters();
			}
		});
	}

	private void addNormalWorkerParameters() {
		normalWorker_composite = new Composite(normalWorker_section, SWT.NONE);
		normalWorker_composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolkit.adapt(normalWorker_composite);
		toolkit.paintBordersFor(normalWorker_composite);
		normalWorker_composite.setLayout(new GridLayout(2, true));
		
		Label firstParams = new Label(normalWorker_composite, SWT.NONE);
		firstParams.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		toolkit.adapt(firstParams, true, true);
		//firstParams.setText("mean");

		Label secondParam = new Label(normalWorker_composite, SWT.NONE);
		secondParam.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		toolkit.adapt(secondParam, true, true);
		//secondParam.setText("sd");

		normalWorker_fisrtParam = new Text(normalWorker_composite, SWT.BORDER);
		GridData gd_normalWorker_fisrtParam = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_normalWorker_fisrtParam.widthHint = 60;
		normalWorker_fisrtParam.setLayoutData(gd_normalWorker_fisrtParam);

		normalWorker_secondParam = new Text(normalWorker_composite, SWT.BORDER);
		GridData gd_normalWorker_secondParam = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		gd_normalWorker_secondParam.widthHint = 60;
		normalWorker_secondParam.setLayoutData(gd_normalWorker_secondParam);
		normalWorker_secondParam.setVisible(true);
		
		int index = normalWorker_combo.getSelectionIndex();
		switch (index) {
		case 0:
			firstParams.setText("Mean");
			secondParam.setText("Sd");
			normalWorker_fisrtParam.setText(advanceConfig.getWorkers().getNormal().getMean());
			normalWorker_secondParam.setText(advanceConfig.getWorkers().getNormal().getSd());
			break;
		case 1:
			firstParams.setText("Reliability");
			secondParam.setText("");
			normalWorker_fisrtParam.setText(advanceConfig.getWorkers().getNormal().getFixed());
			normalWorker_secondParam.setVisible(false);
			break;
		case 2:
			firstParams.setText("Lower Bound");
			secondParam.setText("Upper Bound");
			normalWorker_fisrtParam.setText(advanceConfig.getWorkers().getNormal().getLowBound());
			normalWorker_secondParam.setText(advanceConfig.getWorkers().getNormal().getUpBound());
			break;

		default:
			break;
		}
		normalWorker_section.layout();
	}
	private void addSloppyWorkerDistribution() {
		Composite distribution = new Composite(sloppyWorker_section, SWT.NONE);
		toolkit.adapt(distribution);
		toolkit.paintBordersFor(distribution);
		distribution.setLayout(new GridLayout(1, false));

		Label sloppyWorker = new Label(distribution, SWT.NONE);
		sloppyWorker.setLayoutData(new GridData(SWT.LEFT, SWT.DOWN, false, false, 1,
				1));
		sloppyWorker.setText("Sloppy Worker");

		ComboViewer comboViewer = new ComboViewer(distribution, SWT.READ_ONLY);
		sloppyWorker_combo = comboViewer.getCombo();
		sloppyWorker_combo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1,
				1));
		toolkit.paintBordersFor(sloppyWorker_combo);

		comboViewer.setContentProvider(new ArrayContentProvider()); // org.eclipse.jface.viewers.ArrayContentProvider()
		comboViewer.setLabelProvider(new LabelProvider()); // org.eclipse.jface.viewers.LabelProvider()

		sloppyWorker_combo.setItems(observerValues);
		// combo.setText(observerValues[0]);
		sloppyWorker_combo.setVisibleItemCount(observerValues.length);
		sloppyWorker_index = getIndex(Constant.DISTRIBUTION.valueOf(advanceConfig.getWorkers().getSloppy().getSelectedDistribution()));
		sloppyWorker_combo.select(sloppyWorker_index);
		addSloppyWorkerParameters();
		sloppyWorker_combo.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event event) {
				sloppyWorker_index = sloppyWorker_combo.getSelectionIndex();
				System.out.println(sloppyWorker_combo.getItem(sloppyWorker_index));
				if(sloppyWorker_composite != null) sloppyWorker_composite.dispose();
				addSloppyWorkerParameters();
			}
		});
	}

	private void addSloppyWorkerParameters() {
		sloppyWorker_composite = new Composite(sloppyWorker_section, SWT.NONE);
		sloppyWorker_composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolkit.adapt(sloppyWorker_composite);
		toolkit.paintBordersFor(sloppyWorker_composite);
		sloppyWorker_composite.setLayout(new GridLayout(2, true));
		
		Label firstParams = new Label(sloppyWorker_composite, SWT.NONE);
		firstParams.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		toolkit.adapt(firstParams, true, true);
		//firstParams.setText("mean");

		Label secondParam = new Label(sloppyWorker_composite, SWT.NONE);
		secondParam.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		toolkit.adapt(secondParam, true, true);
		//secondParam.setText("sd");

		sloppyWorker_fisrtParam = new Text(sloppyWorker_composite, SWT.BORDER);
		GridData gd_sloppyWorker_fisrtParam = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_sloppyWorker_fisrtParam.widthHint = 60;
		sloppyWorker_fisrtParam.setLayoutData(gd_sloppyWorker_fisrtParam);

		sloppyWorker_secondParam = new Text(sloppyWorker_composite, SWT.BORDER);
		GridData gd_sloppyWorker_secondParam = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		gd_sloppyWorker_secondParam.widthHint = 60;
		sloppyWorker_secondParam.setLayoutData(gd_sloppyWorker_secondParam);
		sloppyWorker_secondParam.setVisible(true);
		
		int index = sloppyWorker_combo.getSelectionIndex();
		switch (index) {
		case 0:
			firstParams.setText("Mean");
			secondParam.setText("Sd");
			sloppyWorker_fisrtParam.setText(advanceConfig.getWorkers().getSloppy().getMean());
			sloppyWorker_secondParam.setText(advanceConfig.getWorkers().getSloppy().getSd());
			break;
		case 1:
			firstParams.setText("Reliability");
			secondParam.setText("");
			sloppyWorker_fisrtParam.setText(advanceConfig.getWorkers().getSloppy().getFixed());
			sloppyWorker_secondParam.setVisible(false);
			break;
		case 2:
			firstParams.setText("Lower Bound");
			secondParam.setText("Upper Bound");
			sloppyWorker_fisrtParam.setText(advanceConfig.getWorkers().getSloppy().getLowBound());
			sloppyWorker_secondParam.setText(advanceConfig.getWorkers().getSloppy().getUpBound());
			break;

		default:
			break;
		}
		sloppyWorker_section.layout();
	}
	private void addAlgorithmSection(Composite parent) {
		sctnAlgorithm = toolkit.createSection(form.getBody(),
				Section.CLIENT_INDENT | Section.TITLE_BAR);
		// gd_workerSection.widthHint = 335;
		sctnAlgorithm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 2));

		sctnAlgorithm.setText("Algorithm");

		algorithmComposite = new Composite(sctnAlgorithm, SWT.NONE);
		toolkit.adapt(algorithmComposite);
		toolkit.paintBordersFor(algorithmComposite);
		sctnAlgorithm.setClient(algorithmComposite);
		createAlgorithmForm(algorithmComposite);

	}

	public void createAlgorithmForm(Composite parent) {
		GridLayout gl_algorithmComposite = new GridLayout(2, false);
		gl_algorithmComposite.verticalSpacing = 10;
		gl_algorithmComposite.marginHeight = 6;
		gl_algorithmComposite.marginTop = 3;
		parent.setLayout(gl_algorithmComposite);

		Label EM = new Label(parent, SWT.NONE);
		GridData gd_EM = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_EM.verticalIndent = -2;
		EM.setLayoutData(gd_EM);
		EM.setText("EM Iteration");

		txtEM = new Text(parent, SWT.BORDER);
		GridData gd_txtEM = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1,
				1);
		gd_txtEM.verticalIndent = 2;
		gd_txtEM.widthHint = 60;
		txtEM.setLayoutData(gd_txtEM);
		txtEM.setText(advanceConfig.getAlgos().getEM_iter());

		Label Iter = new Label(parent, SWT.NONE);
		Iter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
				1));
		Iter.setText("ITER Iteration");

		txtIter = new Text(parent, SWT.BORDER);
		GridData gd_txtIter = new GridData(SWT.LEFT, SWT.CENTER, true, false,
				1, 1);
		gd_txtIter.widthHint = 60;
		txtIter.setLayoutData(gd_txtIter);
		txtIter.setText(advanceConfig.getAlgos().getITER_iter());

		Label SLME = new Label(parent, SWT.NONE);
		GridData gd_SLME = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_SLME.verticalIndent = 1;
		SLME.setLayoutData(gd_SLME);
		SLME.setText("SLME Iteration");

		txtSLME = new Text(parent, SWT.BORDER);
		GridData gd_txtSLME = new GridData(SWT.LEFT, SWT.CENTER, true, false,
				1, 1);
		gd_txtSLME.widthHint = 60;
		txtSLME.setLayoutData(gd_txtSLME);
		txtSLME.setText(advanceConfig.getAlgos().getSLME_iter());
	}
	
	private void addFeedbacksSection(Composite parent) {
		sctnFeedbacks = toolkit.createSection(form.getBody(),
				Section.CLIENT_INDENT | Section.TITLE_BAR);
		// gd_workerSection.widthHint = 335;
		sctnFeedbacks.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 2, 1));

		sctnFeedbacks.setText("Feedbacks");

		feedbacksComposite = new Composite(sctnFeedbacks, SWT.NONE);
		toolkit.adapt(feedbacksComposite);
		toolkit.paintBordersFor(feedbacksComposite);
		sctnFeedbacks.setClient(feedbacksComposite);
		createFeedbacksForm();

	}
	public void createFeedbacksForm() {
		GridLayout gl_feedbacksComposite = new GridLayout(2, false);
		gl_feedbacksComposite.marginLeft = 5;
		gl_feedbacksComposite.horizontalSpacing = 0;
		feedbacksComposite.setLayout(gl_feedbacksComposite);

		Label distributor = new Label(feedbacksComposite, SWT.NONE);
		GridData gd_distributor = new GridData(SWT.LEFT, SWT.DOWN, true, false, 1,
				1);
		gd_distributor.horizontalIndent = 5;
		distributor.setLayoutData(gd_distributor);
		distributor.setText("Distributor");

		ComboViewer comboViewer = new ComboViewer(feedbacksComposite, SWT.READ_ONLY);
		feedbacks_combo = comboViewer.getCombo();
		GridData gd_feedbacks_combo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1,
				1);
		gd_feedbacks_combo.widthHint = 286;
		gd_feedbacks_combo.heightHint = 88;
		feedbacks_combo.setLayoutData(gd_feedbacks_combo);
		toolkit.paintBordersFor(feedbacks_combo);

		comboViewer.setContentProvider(new ArrayContentProvider()); // org.eclipse.jface.viewers.ArrayContentProvider()
		comboViewer.setLabelProvider(new LabelProvider()); // org.eclipse.jface.viewers.LabelProvider()

		feedbacks_combo.setItems(feedbacksDistributor);
		// combo.setText(observerValues[0]);
		feedbacks_combo.setVisibleItemCount(feedbacksDistributor.length);
		feedbacks_index = getIndexofFeedbacksDistributor(Constant.FEEDBACKSDISTRIBUTION.valueOf(advanceConfig.getFeedbacks().getFeedbacksDistributor()));
		feedbacks_combo.select(feedbacks_index);
		
		Label feedbackRatio = new Label(feedbacksComposite, SWT.NONE);
		GridData gd_feedbackRatio = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_feedbackRatio.horizontalIndent = 5;
		feedbackRatio.setLayoutData(gd_feedbackRatio);
		feedbackRatio.setText("Feedbacks Ratio");

		txtFeedbacksRatio = new Text(feedbacksComposite, SWT.BORDER);
		GridData gd_txtFeedbacksRatio = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtFeedbacksRatio.widthHint = 60;
		txtFeedbacksRatio.setLayoutData(gd_txtFeedbacksRatio);
		txtFeedbacksRatio.setText(advanceConfig.getFeedbacks().getFeedbackRatio());
		
		Label feedbackRatioQuestion = new Label(feedbacksComposite, SWT.NONE);
		GridData gd_feedbackRatioQuestion = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_feedbackRatioQuestion.horizontalIndent = 5;
		feedbackRatioQuestion.setLayoutData(gd_feedbackRatioQuestion);
		feedbackRatioQuestion.setText("Feedbacks Ratio Question");

		txtFeedbacksRatioQuestion = new Text(feedbacksComposite, SWT.BORDER);
		GridData gd_txtFeedbacksRatioQuestion = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtFeedbacksRatioQuestion.widthHint = 60;
		txtFeedbacksRatioQuestion.setLayoutData(gd_txtFeedbacksRatioQuestion);
		txtFeedbacksRatioQuestion.setText(advanceConfig.getFeedbacks().getFeedbackRatioQuestion());
	}
	

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.CENTER;

		parent.setLayoutData(gridData);
		// Create Add button
		// Own method as we need to overview the SelectionAdapter
		createOkButton(parent, OK, "Apply", true);
		// Add a SelectionListener

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, "Cancel", false);
		// Add a SelectionListener
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	protected Button createOkButton(Composite parent, int id, String label,
			boolean defaultButton) {
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(new Integer(id));
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (isValidInput()) {
					okPressed();
				}
			}
		});
		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
		}
		setButtonLayoutData(button);
		return button;
	}

	private boolean isValidInput() {
		boolean valid = true;
		
		return valid;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	// Coyy textFields because the UI gets disposed
	// and the Text Fields are not accessible any more.
	private void saveInput() {
		saveWorker();
		saveAlgos();
		saveFeedbacks();
		advanceConfig.writeAdvanceConfigFile(Constant.ADVANCE_CONFIG_FILE);
	}
	
	private void saveFeedbacks(){
		int feedback_index = feedbacks_combo.getSelectionIndex();
		switch (feedback_index) {
		case 0:
			advanceConfig.getFeedbacks().setFeedbacksDistributor("FeedBacksPerQuestionDistributor");
			break;
		case 1:
			advanceConfig.getFeedbacks().setFeedbacksDistributor("FeedBacksPerWorkerDistributor");
			break;	
		default:
			advanceConfig.getFeedbacks().setFeedbacksDistributor("FeedBacksConstraintDistributor");
			break;
		}
		advanceConfig.getFeedbacks().setFeedbackRatio(txtFeedbacksRatio.getText());
		advanceConfig.getFeedbacks().setFeedbackRatioQuestion(txtFeedbacksRatioQuestion.getText());
		advanceConfig.getFeedbacks().updateHashMap();
	}
	
	private void saveWorker(){
		int expert_index = expert_combo.getSelectionIndex();
		switch (expert_index) {
		case 0:
			advanceConfig.getWorkers().getExpert().setSelectedDistribution("NormalDistribution");
			advanceConfig.getWorkers().getExpert().setMean(expert_fisrtParam.getText());
			advanceConfig.getWorkers().getExpert().setSd(expert_secondParam.getText());
			break;
		case 1:
			advanceConfig.getWorkers().getExpert().setSelectedDistribution("FixedDistribution");
			advanceConfig.getWorkers().getExpert().setFixed(expert_fisrtParam.getText());
			break;
		case 2:
			advanceConfig.getWorkers().getExpert().setSelectedDistribution("UniformDistribution");
			advanceConfig.getWorkers().getExpert().setLowBound(expert_fisrtParam.getText());
			advanceConfig.getWorkers().getExpert().setUpBound(expert_secondParam.getText());
			break;
		default:
			break;
		}
		int normalWorker_index = normalWorker_combo.getSelectionIndex();
		switch (normalWorker_index) {
		case 0:
			advanceConfig.getWorkers().getNormal().setSelectedDistribution("NormalDistribution");
			advanceConfig.getWorkers().getNormal().setMean(normalWorker_fisrtParam.getText());
			advanceConfig.getWorkers().getNormal().setSd(normalWorker_secondParam.getText());
			break;
		case 1:
			advanceConfig.getWorkers().getNormal().setSelectedDistribution("FixedDistribution");
			advanceConfig.getWorkers().getNormal().setFixed(normalWorker_fisrtParam.getText());
			break;
		case 2:
			advanceConfig.getWorkers().getNormal().setSelectedDistribution("UniformDistribution");
			advanceConfig.getWorkers().getNormal().setLowBound(normalWorker_fisrtParam.getText());
			advanceConfig.getWorkers().getNormal().setUpBound(normalWorker_secondParam.getText());
			break;
		default:
			break;
		}
		int sloppyWorker_index = sloppyWorker_combo.getSelectionIndex();
		switch (sloppyWorker_index) {
		case 0:
			advanceConfig.getWorkers().getSloppy().setSelectedDistribution("NormalDistribution");
			advanceConfig.getWorkers().getSloppy().setMean(sloppyWorker_fisrtParam.getText());
			advanceConfig.getWorkers().getSloppy().setSd(sloppyWorker_secondParam.getText());
			break;
		case 1:
			advanceConfig.getWorkers().getSloppy().setSelectedDistribution("FixedDistribution");
			advanceConfig.getWorkers().getSloppy().setFixed(sloppyWorker_fisrtParam.getText());
			break;
		case 2:
			advanceConfig.getWorkers().getSloppy().setSelectedDistribution("UniformDistribution");
			advanceConfig.getWorkers().getSloppy().setLowBound(sloppyWorker_fisrtParam.getText());
			advanceConfig.getWorkers().getSloppy().setUpBound(sloppyWorker_secondParam.getText());
			break;
		default:
			break;
		}
		advanceConfig.getWorkers().updatehashMap();
	}
	
	private void saveAlgos(){
		advanceConfig.getAlgos().setEM_iter(txtEM.getText());
		advanceConfig.getAlgos().setITER_iter(txtIter.getText());
		advanceConfig.getAlgos().setSLME_iter(txtSLME.getText());
		advanceConfig.getAlgos().updateHashMap();
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}
}