package testdoxon.gui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import testdoxon.handler.FileCrawlerHandler;
import testdoxon.model.TestFile;

public class ListDialog extends Dialog implements SelectionListener {

	private int WIDTH = 600;
	private int HEIGHT = 400;
	private Table table;
	private Label label;
	private FileCrawlerHandler fileCrawlerHandler;

	public ListDialog(Shell parent, FileCrawlerHandler fileCrawlerHandler) {
		super(parent);
		this.fileCrawlerHandler = fileCrawlerHandler;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		GridLayout layout = (GridLayout) parent.getLayout();
		layout.marginHeight = 0;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		container.setLayout(new GridLayout(1, true));
 
		FillLayout fl = new FillLayout(SWT.FILL);
		fl.type = SWT.VERTICAL;
		Composite labelComp = new Composite(container, SWT.NONE);
		labelComp.setLayout(fl);
		labelComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Composite buttonComp = new Composite(labelComp, SWT.CENTER);
		RowLayout fl2 = new RowLayout();
		fl2.type = SWT.HORIZONTAL;
		fl2.marginTop = 10;
		fl2.marginLeft = ((this.WIDTH / 2) - 100);
		fl2.spacing = 0;
		buttonComp.setLayout(fl2);

		Button testButton = new Button(buttonComp, SWT.RADIO);
		testButton.setText("Test classes");
		testButton.addSelectionListener(this);
		testButton.setSelection(true);

		Button prodButton = new Button(buttonComp, SWT.RADIO);
		prodButton.setText("Production classes");
		prodButton.addSelectionListener(this);

		this.label = new Label(labelComp, SWT.NONE);
		this.label.setText("Description");
		this.label.setAlignment(SWT.CENTER);

		this.table = new Table(container, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		this.table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		this.table.setHeaderVisible(true);
		this.table.setLinesVisible(true);
		String[] titles = { "Filename", "Package", "Filepath" };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(this.table, SWT.FILL);
			column.setText(titles[i]);
			column.setWidth((int)((this.WIDTH - 30) / 3));
		}

		this.showAllTestClasses();
		
		return container;
	}

	private void showAllTestClasses() {
		this.table.removeAll();

		TestFile[] testFiles = this.fileCrawlerHandler.getAllSingleTestClasses();
		this.label.setText("(" + testFiles.length + ") Test classes without a corresponding production class");

		if (testFiles != null && testFiles.length > 0) {
			for (int i = 0; i < testFiles.length; i++) {
				TableItem item = new TableItem(this.table, SWT.NONE);
				item.setText(0, testFiles[i].getFilename());
				item.setText(1, testFiles[i].getPackage());
				item.setText(2, testFiles[i].getFilepath());
			}
		}
	}

	private void showAllProdClasses() {
		this.table.removeAll();
		
		TestFile[] prodFiles = this.fileCrawlerHandler.getAllSingleProdClasses();
		this.label.setText("(" + prodFiles.length + ") Production classes without a corresponding test class");

		if (prodFiles != null && prodFiles.length > 0) {
			for (int i = 0; i < prodFiles.length; i++) {
				TableItem item = new TableItem(this.table, SWT.NONE);
				item.setText(0, prodFiles[i].getFilename());
				item.setText(1, prodFiles[i].getPackage());
				item.setText(2, prodFiles[i].getFilepath());
			}
		}
	}

	@Override
	protected Point getInitialSize() {
		return new Point(this.WIDTH, this.HEIGHT);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("List single classes");
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		if (arg0.getSource() instanceof Button) {
			Button button = (Button) arg0.getSource();
			if (button.getText().equals("Test classes")) {
				this.showAllTestClasses();
			} else {
				this.showAllProdClasses();
			}

		}

	}
}
