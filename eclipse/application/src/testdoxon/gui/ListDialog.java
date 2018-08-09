package testdoxon.gui;

import java.io.File;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.internal.ide.registry.SystemEditorOrTextEditorStrategy;

import testdoxon.handler.FileCrawlerHandler;
import testdoxon.model.TestFile;

public class ListDialog extends Dialog implements SelectionListener {

	private int WIDTH = 600;
	private int HEIGHT = 400;
	private Table table;
	private Label label;
	private FileCrawlerHandler fileCrawlerHandler;
	private Thread testThread;
	private Thread prodThread;

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

	@SuppressWarnings("deprecation")
	private void showAllTestClasses() {
		updateDescriptionText("Please wait while we are gathering information. This may take a while.");
		
		if(prodThread != null && prodThread.isAlive()) {
			prodThread.stop();
		}
		
		testThread = new Thread(new Runnable() {
			public void run () {
				TestFile[] testFiles = fileCrawlerHandler.getAllSingleTestClasses();
				updateDescriptionText("(" + testFiles.length + ") Test classes without a corresponding production class");
				clearTable();
				if (testFiles != null && testFiles.length > 0) {
					for (int i = 0; i < testFiles.length; i++) {
						updateListItem(testFiles[i]);
					}
				}
			}
		});
		testThread.start();
	}
	

	private void showAllProdClasses() {
		updateDescriptionText("Please wait while we are gathering information. This may take a while.");
		
		if(testThread != null && testThread.isAlive()) {
			testThread.stop();
		}
		
		prodThread = new Thread(new Runnable() {
			public void run () {
				TestFile[] prodFiles = fileCrawlerHandler.getAllSingleProdClasses();				
				updateDescriptionText("(" + prodFiles.length + ") Production classes without a corresponding test class");
				clearTable();
				if (prodFiles != null && prodFiles.length > 0) {
					for (int i = 0; i < prodFiles.length; i++) {
						updateListItem(prodFiles[i]);
					}
				}
			}
		});
		
		prodThread.start();
	}
	
	synchronized void updateDescriptionText (String msg) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				label.setText(msg);
			}
		});
	}
	
	synchronized void updateListItem (TestFile testFile) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, testFile.getFilename());
				item.setText(1, testFile.getPackage());
				item.setText(2, testFile.getFilepath());
			}
		});
	}
	
	synchronized void clearTable() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				table.removeAll();
			}
		});
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
			
			if (button.getText().equals("Test classes") && button.getSelection()) {
				this.showAllTestClasses();
			} else if (button.getText().equals("Production classes") && button.getSelection()){
				this.showAllProdClasses();
			}

		}

	}
}
