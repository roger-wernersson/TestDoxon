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

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import testdoxon.views.View;

public class HeaderToolTipListener implements Listener {

	private Text header;
	
	public  HeaderToolTipListener(Text header) {
		this.header = header;
	}
	
	@Override
	public void handleEvent(Event arg0) {
		// TODO Auto-generated method stub
		header.setToolTipText(View.currentTestFile.getHeaderName());
	}

}
