/**
Copyright 2018 Delicate Sound Of Software AB, All Rights Reserved

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package testdoxon.listener;

import java.io.File;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

import testdoxon.handler.FileCrawlerHandler;
import testdoxon.model.TDFile;
import testdoxon.util.DoxonUtils;
import testdoxon.views.View;

public class UpdateOnFileChangedListener implements ISelectionListener {
	private FileCrawlerHandler fileCrawlerHandler;
	private TableViewer viewer;
	private ComboViewer testClassPathsComboBox;

	public UpdateOnFileChangedListener(FileCrawlerHandler fileCrawlerHandler, TableViewer viewer, ComboViewer testClassPathsComboBox) {
		super();
		this.fileCrawlerHandler = fileCrawlerHandler;
		this.viewer = viewer;
		this.testClassPathsComboBox = testClassPathsComboBox;
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
				
				// Update combo viewer to show all test classes
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						try {
							testClassPathsComboBox.setInput(fileCrawlerHandler.getAllTestClassesAsTestFileArray());
						} catch (AssertionFailedException e) {
							// Do nothing
						}
					}
				});

				DoxonUtils.findFileToOpen(this.viewer);
			}

		}
	}

}