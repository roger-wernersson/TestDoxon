package testdoxon.gui;


import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


public class StatisticDialog extends Dialog {

	public StatisticDialog(Shell parent) {
		super(parent);
		
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
		label.setText("Hej");
		label.setLayoutData(gdLeft);
		
		Label label2 = new Label(container, SWT.PUSH);
		label2.setText("Hej2");
		label2.setLayoutData(gdRight);
		
		Label label3 = new Label(container, SWT.PUSH);
		label3.setBackground(new Color(null, 0, 105, 153));
		label3.setText("Hej3");
		label3.setLayoutData(gdLeft);
		
		Label label4 = new Label(container, SWT.PUSH);
		label4.setText("Hej4");
		label4.setLayoutData(gdRight);
		
		Label label5 = new Label(container, SWT.PUSH);
		label5.setBackground(new Color(null, 0, 105, 153));
		label5.setText("Hej5");
		label5.setLayoutData(gdLeft);
		
		Label label6 = new Label(container, SWT.PUSH);
		label6.setText("Hej6");
		label6.setLayoutData(gdRight);

		
		
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
