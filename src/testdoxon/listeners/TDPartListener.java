package testdoxon.listeners;

import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import testdoxon.handlers.FileCrawlerHandler;

@SuppressWarnings("restriction")
public class TDPartListener implements IPartListener {
	private CaretListener wordListener;
	private FileCrawlerHandler fileCrawlerHandler;

	private TableViewer viewer;

	public TDPartListener(TableViewer viewer, FileCrawlerHandler fileCrawlerHandler) {
		super();
		this.viewer = viewer;
		this.fileCrawlerHandler = fileCrawlerHandler;
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
					this.wordListener = new UpdateOnCaretMovedListener(this.viewer, text, this.fileCrawlerHandler);
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
