package testdoxon.gui;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Label;

import testdoxon.exceptionHandler.TDException;
import testdoxon.handler.FileHandler;
import testdoxon.model.TDFile;
import testdoxon.model.TDTableItem;

public class TestMethodTableContentProvider implements IStructuredContentProvider {
	private TDFile file;
	private Label header;
	private FileHandler fileHandler;

	public TestMethodTableContentProvider (Label header, FileHandler fileHandler) {
		this.file = null;;
		this.header = header;
		this.fileHandler = fileHandler;
	}
	

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		if (newInput instanceof TDFile) {
			this.file = (TDFile) newInput;
		}
	}

	public void dispose() {
	}

	/**
	 * Updates the header with the current test class name and package name. If file
	 * is not found return is "file not found"
	 * 
	 * @param parent
	 * @return String[]
	 */
	public Object[] getElements(Object parent) {
		if (this.file != null) {
			try {
				header.setText(this.file.getHeaderName());
				TDTableItem[] retVal = fileHandler.getMethodsFromFile(this.file.getAbsolutePath());
				if (retVal != null && retVal.length > 0) {
					return retVal;
				}
			} catch (TDException e) {
				header.setText(e.getMessage());
			}
		}
		return new String[] {};
	}
}
