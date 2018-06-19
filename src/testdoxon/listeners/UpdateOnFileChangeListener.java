package testdoxon.listeners;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;

public class UpdateOnFileChangeListener implements IPartListener {

	@Override
	public void partActivated(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub
		
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
//		// TODO Auto-generated method stub
//		if (wordListener != null) {
//			IEditorPart iep = arg0.getSite().getPage().getActiveEditor();
//			if (iep != null) {
//				AbstractTextEditor e = (AbstractTextEditor) iep;
//				Control adapter = e.getAdapter(Control.class);
//
//				if (adapter instanceof StyledText) {
//					StyledText text = (StyledText) adapter;
//					text.removeCaretListener(wordListener);
//				}
//			}
//		}
	}

	@Override
	public void partOpened(IWorkbenchPart arg0) {
		// TODO Auto-generated method stub
		
	}

}
