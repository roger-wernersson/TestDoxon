package testdoxon.gui;


import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
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
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);
		container.setBackground(new Color(null, 0,0,0));
		
		GridData gdLeft = new GridData();
		gdLeft.widthHint = 200;
		gdLeft.heightHint = 30;
		
		GridData gdRight = new GridData();
		gdRight.widthHint = 50;
		gdRight.heightHint = 30;
		
		Label label = new Label(container, SWT.PUSH);
		label.setBackground(new Color(null, 0, 105, 153));
		label.setText("Test classes in memory");
		label.setLayoutData(gdLeft);
		
		Label labelTestClasses = new Label(container, SWT.PUSH);
		labelTestClasses.setText(Integer.toString(fileCrawlerHandler.getNrOfTestClasses()));
		labelTestClasses.setLayoutData(gdRight);
		
		Label label2 = new Label(container, SWT.PUSH);
		label2.setBackground(new Color(null, 0, 105, 153));
		label2.setText("Prod. classes in memory");
		label2.setLayoutData(gdLeft);
		
		Label labelProduction = new Label(container, SWT.PUSH);
		labelProduction.setText(Integer.toString(fileCrawlerHandler.getNrOfProdClasses()));
		labelProduction.setLayoutData(gdRight);
		
		Label label3 = new Label(container, SWT.PUSH);
		label3.setBackground(new Color(null, 0, 105, 153));
		label3.setText("Last lookup time (ms)");
		label3.setLayoutData(gdLeft);
		
		Label labelFindTime = new Label(container, SWT.PUSH);
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
		newShell.setText("Configure...");
	}

}
