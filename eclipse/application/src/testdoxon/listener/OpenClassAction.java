package testdoxon.listener;

import java.io.File;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;

import testdoxon.handler.FileCrawlerHandler;
import testdoxon.handler.FileHandler;
import testdoxon.log.TDLog;
import testdoxon.model.TDFile;
import testdoxon.model.TestFile;
import testdoxon.util.TDGlobals;

public class OpenClassAction extends Action {

	private TableViewer viewer;
	private ComboViewer testClassPaths;
	private FileCrawlerHandler fileCrawlerHandler;

	public OpenClassAction(ComboViewer testClassPaths, TableViewer viewer, FileCrawlerHandler fileCrawlerHandler) {
		this.viewer = viewer;
		this.testClassPaths = testClassPaths;
		this.fileCrawlerHandler = fileCrawlerHandler;
	}

	public void run() {
		ISelection selection = testClassPaths.getSelection();

		Object obj = ((IStructuredSelection) selection).getFirstElement();

		if (obj != null && obj instanceof TestFile) {
			TestFile _tmp = (TestFile) obj;
			
			File file = new File(_tmp.getFilepath());
			if(!file.exists()) {
				TDLog.info(_tmp.getFilename() + " does not exists. Removing file from database.", TDLog.ERROR);
				fileCrawlerHandler.removeEntry(_tmp);
				this.testClassPaths.remove(_tmp);
			} else {
				if (TDGlobals.currentTestFile == null || !_tmp.getFilepath().equals(TDGlobals.currentTestFile.getAbsolutePath())) {
					FileHandler filehandler = new FileHandler();
					if (filehandler.fileExists(_tmp.getFilepath())) {
						TDGlobals.currentTestFile = new TDFile(new File(_tmp.getFilepath()));
						TDGlobals.currentTestFile.setHeaderFilepath(TDGlobals.currentTestFile.getAbsolutePath());
					}
	
					if (TDGlobals.currentTestFile != null) {
						Display.getDefault().syncExec(new Runnable() {
							@Override
							public void run() {
								try {
									viewer.setInput(TDGlobals.currentTestFile);
								} catch (AssertionFailedException e) {
									TDLog.info(e.getMessage(), TDLog.WARNING);
								}
							}
						});
					}
				}
			}
		}
	}
}
