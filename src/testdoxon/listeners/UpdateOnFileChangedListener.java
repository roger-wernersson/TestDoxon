package testdoxon.listeners;

import java.io.File;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

import testdoxon.handlers.FileCrawlerHandler;
import testdoxon.models.TDFile;
import testdoxon.utils.DoxonUtils;
import testdoxon.views.View;

public class UpdateOnFileChangedListener implements ISelectionListener {
	private FileCrawlerHandler fileCrawlerHandler;
	private TableViewer viewer;

	public UpdateOnFileChangedListener(FileCrawlerHandler fileCrawlerHandler, TableViewer viewer) {
		super();
		this.fileCrawlerHandler = fileCrawlerHandler;
		this.viewer = viewer;
	}

	@Override
	public void selectionChanged(IWorkbenchPart arg0, ISelection arg1) {
		// TODO Auto-generated method stub
		if (arg0.getTitle().matches(".*\\.java")) {
			File file = (File) arg0.getSite().getPage().getActiveEditor().getEditorInput().getAdapter(File.class);

			if (View.currentOpenFile == null || !file.getName().equals(View.currentOpenFile.getName())) {
				View.currentOpenFile = new TDFile(file);

				// Get all test classes
				String testFolder = DoxonUtils.findTestFolder(View.currentOpenFile.getAbsolutePath());
				if (testFolder != null) {
					this.fileCrawlerHandler.getAllTestClasses(testFolder);
				}

				DoxonUtils.findFileToOpen(this.viewer);
			}

		}
	}

}
