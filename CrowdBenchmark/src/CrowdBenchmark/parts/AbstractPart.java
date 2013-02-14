package CrowdBenchmark.parts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.Section;

public abstract class AbstractPart {
	protected void customizeSection(Section section) {
		Display display = Display.getCurrent();
		section.setTitleBarForeground(display
				.getSystemColor(SWT.COLOR_TITLE_FOREGROUND));
		section.setTitleBarBackground(display
				.getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		section.setTitleBarGradientBackground(display
				.getSystemColor(SWT.COLOR_TITLE_BACKGROUND));
	}

}
