package testdoxon.repository;

import testdoxon.model.TDClass;
import testdoxon.model.TDMethod;
import testdoxon.util.TDConstants;

public class FileRepository {

	public FileRepository () {
		
	}

	public String[] readFile (String filepath) {
		return null;
	}
	
	public TDClass extractMethods (String[] fileContent) {
		TDClass tdClass = new TDClass("");
		
		// template
		for (String line : fileContent) {
			if (line == "hej") {
				tdClass.addMethodname(new TDMethod("sdfg", TDConstants.TEST));
			}
		}
		
		return tdClass;
	}
	
}
