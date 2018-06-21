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

import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import testdoxon.handler.FileCrawlerHandler;

@SuppressWarnings("restriction")
public class TDPartListener implements IPartListener {
	private CaretListener wordListener;
	private FileCrawlerHandler fileCrawlerHandler;

	private TableViewer viewer;
	private ComboViewer methodNamesComboBox;

	public TDPartListener(TableViewer viewer, FileCrawlerHandler fileCrawlerHandler, ComboViewer methodNamesComboBox) {
		super();
		this.viewer = viewer;
		this.fileCrawlerHandler = fileCrawlerHandler;
		this.methodNamesComboBox = methodNamesComboBox;
	}

	@Override
	public void partActivated(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub
		IEditorPart iep = arg0.getSite().getPage().getActiveEditor();
		if (iep != null) {
			if (iep instanceof CompilationUnitEditor) {
				AbstractTextEditor e = (AbstractTextEditor) iep;
				Control adapter = e.getAdapter(Control.class);

				if (adapter instanceof StyledText) {
					StyledText text = (StyledText) adapter;
					this.wordListener = new UpdateOnCaretMovedListener(this.viewer, text, this.fileCrawlerHandler, this.methodNamesComboBox);
					text.addCaretListener(this.wordListener);
				}
			}
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partClosed(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partDeactivated(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub
		if (this.wordListener != null) {
			IEditorPart iep = arg0.getSite().getPage().getActiveEditor();
			if (iep != null) {
				if (iep instanceof CompilationUnitEditor) {
					AbstractTextEditor e = (AbstractTextEditor) iep;
					Control adapter = e.getAdapter(Control.class);

					if (adapter instanceof StyledText) {
						StyledText text = (StyledText) adapter;
						text.removeCaretListener(this.wordListener);
					}
				}
			}
		}
	}

	@Override
	public void partOpened(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub

	}

}
