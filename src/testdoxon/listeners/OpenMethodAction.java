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

package testdoxon.listeners;

import java.io.File;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.ITextEditor;

import testdoxon.exceptionHandlers.TDException;
import testdoxon.handlers.FileHandler;
import testdoxon.views.View;

public class OpenMethodAction extends Action {

	private TableViewer viewer;
	private ViewPart view;
	private FileHandler fileHandler;
	
	public OpenMethodAction(TableViewer viewer, ViewPart view, FileHandler fileHandler) {
		this.viewer = viewer;
		this.view = view;
		this.fileHandler = fileHandler;
	}
	
	public void run() {
		ISelection selection = viewer.getSelection();
		@SuppressWarnings("unused")
		Object obj = ((IStructuredSelection) selection).getFirstElement();
		File file = (File) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor().getEditorInput().getAdapter(File.class);

		if (file.getAbsolutePath().equals(View.currentTestFile.getAbsolutePath().toString())) {
			// Opened class is the correct Test class, so just move to the correct line in
			// that class.
			ITextEditor editor = (ITextEditor) view.getSite().getWorkbenchWindow().getActivePage().getActiveEditor();
			IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());

			if (document != null) {
				IRegion lineInfo = null;

				try {
					int lineNumber = 1;
					try {
						lineNumber = fileHandler.getLineNumberOfSpecificMethod(View.currentTestFile.getAbsolutePath(), obj.toString());
						if (lineNumber == -1) {
							lineNumber = 1;
						}
					} catch (TDException e1) {
						e1.printStackTrace();
					}
					lineInfo = document.getLineInformation(lineNumber - 1);
				} catch (BadLocationException e) {

				}

				if (lineInfo != null) {
					editor.selectAndReveal(lineInfo.getOffset(), lineInfo.getLength());
				}
			}

		} else {
			// Open Test class and jump to correct line
			IPath location = Path.fromOSString(View.currentTestFile.getAbsolutePath());
			IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(location);

			if (iFile != null) {
				IWorkbenchPage iWorkbenchPage = view.getSite().getPage();

				@SuppressWarnings("rawtypes")
				HashMap<String, Comparable> map = new HashMap<String, Comparable>();
				int lineNumber = 0;
				try {
					lineNumber = fileHandler.getLineNumberOfSpecificMethod(iFile.getRawLocation().toOSString(),
							obj.toString());
					if (lineNumber == 0) {
						map.put(IMarker.LINE_NUMBER, 1);
					} else {
						map.put(IMarker.LINE_NUMBER, lineNumber);
					}

				} catch (TDException e) {
					e.printStackTrace();
				}

				try {
					IMarker marker = iFile.createMarker(IMarker.TEXT);
					marker.setAttributes(map);
					marker.setAttribute(IDE.EDITOR_ID_ATTR, "org.eclipse.ui.MarkdownTextEditor");
					IDE.openEditor(iWorkbenchPage, marker, true);
					marker.delete();
				} catch (CoreException e2) {
					e2.printStackTrace();
				}
			}
		}
	}
}
