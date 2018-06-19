package testdoxon.listeners;

import java.io.File;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;

import testdoxon.handlers.FileCrawlerHandler;
import testdoxon.models.TDFile;
import testdoxon.utils.DoxonUtils;
import testdoxon.views.View;

public class UpdateOnCaretMovedListener implements CaretListener {
	private FileCrawlerHandler fileCrawlerHandler;

	private StyledText text;
	private TableViewer viewer;

	public UpdateOnCaretMovedListener(TableViewer viewer, StyledText text, FileCrawlerHandler fileCrawlerHandler) {
		super();
		this.viewer = viewer;
		this.text = text;
		this.fileCrawlerHandler = fileCrawlerHandler;
	}

	@Override
	public void caretMoved(CaretEvent arg0) {
		// TODO Auto-generated method stub
		String word = DoxonUtils.getWordUnderCaret(arg0.caretOffset, text);

		if (word.length() > 0 && Character.isUpperCase(word.charAt(0))) {
			String fileToLookFor = "Test" + word + ".java";
			if (View.currentOpenFile != null) {
				String newTestFilepath = fileCrawlerHandler.getTestFilepathFromFilename(fileToLookFor,
						View.currentOpenFile.getAbsolutePath(), View.currentOpenFile.getName());

				if (newTestFilepath != null && !newTestFilepath.equals(View.currentTestFile.getAbsolutePath())) {
					View.currentTestFile = new TDFile(new File(newTestFilepath));
					View.currentTestFile.setHeaderFilepath(View.currentTestFile.getAbsolutePath());

					if (View.currentTestFile != null) {
						Display.getDefault().syncExec(new Runnable() {
							@Override
							public void run() {
								try {
									viewer.setInput(View.currentTestFile);
								} catch (AssertionFailedException e) {
									// Do nothing
								}
							}
						});
					}
				}
			}
		} else {
			// Show current class test class.
			this.findFileToOpen();
		}
	}

	private void findFileToOpen() {
		if (View.currentOpenFile != null) {
			// If a test class already is open
			if (View.currentOpenFile.getName().matches("^Test.*")) {
				View.currentTestFile = View.currentOpenFile;
				// If a regular class is open
			} else {
				// DoxonUtils doxonUtils = new DoxonUtils();
				String newTestFilepath = DoxonUtils.createTestPath(View.currentOpenFile) + "Test"
						+ View.currentOpenFile.getName();
				View.currentTestFile = new TDFile(new File(newTestFilepath));
			}
			View.currentTestFile.setHeaderFilepath(View.currentOpenFile.getAbsolutePath());

			if (View.currentTestFile != null) {
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						try {
							viewer.setInput(View.currentTestFile);
						} catch (AssertionFailedException e) {
							// Do nothing
						}

					}
				});
			}
		}
	}

}
