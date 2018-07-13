package testdoxon.listener;

import java.io.File;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;

import testdoxon.handler.FileHandler;
import testdoxon.log.TDLog;
import testdoxon.model.TDFile;
import testdoxon.model.TestFile;
import testdoxon.util.TDGlobals;

public class OpenClassAction extends Action {

	private TableViewer viewer;
	private ComboViewer testClassPaths;

	public OpenClassAction(ComboViewer testClassPaths, TableViewer viewer) {
		this.viewer = viewer;
		this.testClassPaths = testClassPaths;
	}

	public void run() {
		ISelection selection = testClassPaths.getSelection();

		Object obj = ((IStructuredSelection) selection).getFirstElement();

		if (obj != null && obj instanceof TestFile) {
			TestFile _tmp = (TestFile) obj;
			
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
