package testdoxon.gui;



import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import testdoxon.handler.FileCrawlerHandler;
import testdoxon.util.TDGlobals;


public class StatisticDialog extends Dialog {

	private FileCrawlerHandler fileCrawlerHandler;
	public StatisticDialog(Shell parent, FileCrawlerHandler fileCrawlerHandler) {
		super(parent);
		this.fileCrawlerHandler = fileCrawlerHandler;
		
	}
	
	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		GridLayout layout = (GridLayout)parent.getLayout();
		layout.marginHeight = 0;
	}



	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		Display display = Display.getCurrent();
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);
		container.setBackground(new Color(display, 0,0,0));
		
		GridData gdLeft = new GridData();
		gdLeft.widthHint = 200;
		gdLeft.heightHint = 30;
		
		GridData gdRight = new GridData();
		gdRight.widthHint = 50;
		gdRight.heightHint = 30;
		
		CLabel label = new CLabel(container, SWT.CENTER);
		label.setBackground(new Color(display, 0, 105, 153));
		label.setText("Test classes in memory");
		label.setLayoutData(gdLeft);
		label.setAlignment(SWT.CENTER);
		
		
		FontData fontData = label.getFont().getFontData()[0];
		
		Font font = new Font(display, new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
		
		label.setFont(font);
		label.setForeground(new Color(display, 255,255,255));
		
		CLabel labelTestClasses = new CLabel(container, SWT.CENTER);
		labelTestClasses.setText(Integer.toString(fileCrawlerHandler.getNrOfTestClasses()));
		labelTestClasses.setLayoutData(gdRight);
		
		CLabel label2 = new CLabel(container, SWT.CENTER);
		label2.setBackground(new Color(display, 0, 105, 153));
		label2.setText("Prod. classes in memory");
		label2.setLayoutData(gdLeft);
		label2.setFont(font);
		label2.setForeground(new Color(display, 255,255,255));
		
		CLabel labelProduction = new CLabel(container, SWT.CENTER);
		labelProduction.setText(Integer.toString(fileCrawlerHandler.getNrOfProdClasses()));
		labelProduction.setLayoutData(gdRight);
		
		CLabel label3 = new CLabel(container, SWT.CENTER);
		label3.setBackground(new Color(display, 0, 105, 153));
		label3.setText("Last lookup time (ms)");
		label3.setLayoutData(gdLeft);
		label3.setFont(font);
		label3.setForeground(new Color(display, 255,255,255));
		
		CLabel labelFindTime = new CLabel(container, SWT.CENTER);
		labelFindTime.setText(Long.toString(TDGlobals.ms_recursiveRead));
		labelFindTime.setLayoutData(gdRight);

		
		
		return container;	
			
	}

	@Override
	protected Point getInitialSize() {
		return new Point(280,  150);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Statistics");
	}

}
