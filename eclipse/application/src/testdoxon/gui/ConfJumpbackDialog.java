package testdoxon.gui;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;

import testdoxon.log.TDLog;
import testdoxon.util.DoxonUtils;
import testdoxon.views.View;

public class ConfJumpbackDialog extends Dialog {
	
	private Text description;
	private String descText;
	
	public ConfJumpbackDialog (Shell parent) {
		super(parent);
		
		Bundle bundle = Platform.getBundle("TestDoxon");
		URL url = null;
		ImageDescriptor imageDesc = null;
		
		url = FileLocator.find(bundle, new Path("icons/testDox16px.png"), null);
		imageDesc = ImageDescriptor.createFromURL(url);
		Image testDox_img = imageDesc.createImage();
		
		ConfJumpbackDialog.setDefaultImage(testDox_img);
		
		String path = View.rootFolder;
		if(path == null || path.isEmpty()) {
			path = "Select a class to see root folder.";
		}
		
		this.descText = "Current root folder:\n" + path + "\n\nThis option will change the path from where TestDoxon finds test classes.\nDefault root folder is set to the src folder in the current project.\nIf you desire to include test classes from other bundles, change\nthe number of jumpbacks.";
	}
	
	

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "OK", true);
	}



	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		Label name = new Label(container, SWT.NONE);
		name.setText("Number of jumpbacks from src folder:");
		
		Spinner spinner = new Spinner(container, SWT.BORDER);
		spinner.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		spinner.setMaximum(10);
		spinner.setIncrement(1);
		spinner.setMinimum(0);
		spinner.setSelection(View.rootJumpbacks);
		
		this.description = new Text(container, SWT.MULTI | SWT.READ_ONLY);
		this.description.setText(this.descText);		
		spinner.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent arg0) {
				if(arg0.getSource() instanceof Spinner) {
					Spinner _tmp = (Spinner) arg0.getSource();
					View.rootJumpbacks = _tmp.getSelection();
					String newPath = DoxonUtils.findRootFolder(View.orgRootFolder);
					
					if(newPath == null || newPath.isEmpty()) {
						newPath = "Invalid number of jumpbacks.";
						View.rootFolder = newPath;
					} else {
						this.saveProperty(_tmp.getSelection());
					}
					
					descText = "Current root folder:\n" + newPath + "\n\nThis option will change the path from where TestDoxon finds test classes.\nDefault root folder is set to the src folder in the current project.\nIf you desire to include test classes from other bundles, change\nthe number of jumpbacks.";
					description.setText(descText);
				}
			}
			
			private void saveProperty (int jumpbacks) {
				OutputStream out = null;
				
				try {
					out = new FileOutputStream(View.CONFIG_FILE);
					
					View.prop.setProperty("jumpback", Integer.toString(jumpbacks));
					View.prop.store(out, null);
					
				} catch (IOException e) {
					TDLog.info(e.getMessage(), TDLog.ERROR);
				} finally {
					if(out != null) {
						try {
							out.close();
						} catch (IOException e1) {
							TDLog.info(e1.getMessage(), TDLog.ERROR);
						}
					}
				}
			}
		});
		
		Label warning = new Label(container, SWT.CENTER);
		warning.setText("!! WARNING - Software may become slow !!");

		return container;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450,  300);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Configure...");
	}
	
	

}
