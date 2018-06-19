package testdoxon.listeners;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import testdoxon.views.View;

public class HeaderToolTipListener implements Listener {

	private Label header;
	
	public  HeaderToolTipListener(Label header) {
		this.header = header;
	}
	
	@Override
	public void handleEvent(Event arg0) {
		// TODO Auto-generated method stub
		header.setToolTipText(View.currentTestFile.getHeaderName());
	}

}
