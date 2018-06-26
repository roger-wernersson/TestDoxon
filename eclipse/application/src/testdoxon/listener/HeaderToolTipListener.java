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

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;

import testdoxon.views.View;

public class HeaderToolTipListener implements Listener {

	private Text header;
	
	public  HeaderToolTipListener(Text header, ViewPart view) {
		this.header = header;
		this.header.addListener(SWT.MouseDoubleClick, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				if(View.currentTestFile != null) {					
					IPath location = Path.fromOSString(View.currentTestFile.getAbsolutePath());
					IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(location);
					
					IWorkbenchPage iWorkbenchPage = view.getSite().getPage();

					@SuppressWarnings("rawtypes")
					HashMap<String, Comparable> map = new HashMap<String, Comparable>();

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
		});
	}
	
	@Override
	public void handleEvent(Event arg0) {
		header.setToolTipText(View.currentTestFile.getAbsolutePath());
	}

}
