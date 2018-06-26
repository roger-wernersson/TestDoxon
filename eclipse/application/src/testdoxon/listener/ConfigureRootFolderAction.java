package testdoxon.listener;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;

import testdoxon.views.View;

public class ConfigureRootFolderAction extends Action {
	public void run() {
		DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent().getActiveShell(), SWT.NULL);
		
		if(View.rootFolder != null) {
			dialog.setFilterPath(View.rootFolder);
		}
		
		String path = dialog.open();
		
		if(path != null && !path.isEmpty()) {
			path.replaceAll("\\\\", "/");
			View.rootFolder = path;
			this.setToolTipText(path);
			System.out.println(path);
		}
	}
}