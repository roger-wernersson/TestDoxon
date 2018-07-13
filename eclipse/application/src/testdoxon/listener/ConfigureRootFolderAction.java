package testdoxon.listener;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;

import testdoxon.util.TDGlobals;

public class ConfigureRootFolderAction extends Action {
	public void run() {
		DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent().getActiveShell(), SWT.NULL);
		
		if(TDGlobals.rootFolder != null) {
			dialog.setFilterPath(TDGlobals.rootFolder);
		}
		
		String path = dialog.open();
		
		if(path != null && !path.isEmpty()) {
			path.replaceAll("\\\\", "/");
			TDGlobals.rootFolder = path;
			this.setToolTipText(path);
			System.out.println(path);
		}
	}
}