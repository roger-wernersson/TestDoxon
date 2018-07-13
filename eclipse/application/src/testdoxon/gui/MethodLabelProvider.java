package testdoxon.gui;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import testdoxon.model.TDTableItem;
import testdoxon.util.TDGlobals;

public class MethodLabelProvider extends LabelProvider implements ITableLabelProvider {
	
	
	public MethodLabelProvider () {
		
	}
	
	public String getColumnText(Object obj, int index) {
		if (obj instanceof TDTableItem) {
			TDTableItem _tmp = (TDTableItem) obj;
			return _tmp.getMethodName(); 
		}
		return getText(obj);
	}

	public Image getColumnImage(Object obj, int index) {
		return getImage(obj);
	}

	public Image getImage(Object obj) {
		if(obj instanceof TDTableItem) {
			TDTableItem _tmp = (TDTableItem) obj;
			
			switch(_tmp.getPictureIndex()) {
				case TDTableItem.BAD_METHOD_NAME:
					return TDGlobals.blueDot;
				case TDTableItem.NONE:
					return TDGlobals.redDot;
				case TDTableItem.TEST:
					return TDGlobals.greenDot;
				case TDTableItem.IGNORE:
					return TDGlobals.grayDot;
				case TDTableItem.BOTH_TEST_IGNORE:
					return TDGlobals.yellowDot;
				default:
					return TDGlobals.blueDot;
			}
		}
		
		return TDGlobals.blueDot;
	}
}
