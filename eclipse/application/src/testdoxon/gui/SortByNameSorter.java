package testdoxon.gui;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

import testdoxon.model.TDTableItem;

public class SortByNameSorter extends ViewerComparator {
	
	@Override
	public int compare(Viewer viewer, Object o1, Object o2) {
		
		if(o1 instanceof TDTableItem && o2 instanceof TDTableItem) {
			TDTableItem firstItem = (TDTableItem) o1;
			TDTableItem secondItem = (TDTableItem) o2;
			
			return firstItem.getMethodName().compareTo(secondItem.getMethodName());
		}
		
		return -1;	
	}
	
}
