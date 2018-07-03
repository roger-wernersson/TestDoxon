package testdoxon.generator;

import java.util.ArrayList;

import testdoxon.handler.FileHandler;
import testdoxon.model.TDClass;
import testdoxon.model.TDMethod;

public class HtmlParser {

	private TDClass[] classes;
	private FileHandler fileHandler;
	
	public HtmlParser (TDClass[] classes) {
		this.classes = classes;
		this.fileHandler = new FileHandler();
	}
	
	private String[] parseHTML (TDClass _class) {
		ArrayList<String> html = new ArrayList<String>();

		html.add("<html>");
		html.add("<head>");
		html.add("<title>" + _class.getName() + "</title>");
		html.add("</head>");
		html.add("<body>");
		
		html.add("<h1>" + _class.getName() + "</h1>");
		
		html.add("<ul>");
		for(TDMethod method : _class.getMethods()) {
			html.add("<li>" + method.getPictureHTML() + " " + method.getMethodname() + "</li>");
		}
		html.add("</ul>");
		html.add("</body>");
		html.add("</html>");
		
		return html.toArray(new String[html.size()]);
	}
	
	public boolean saveClassesToHTML () {
		boolean retVal = true;
		
		for (TDClass _class : classes) {
			String filename = _class.getName().replaceAll(".java", "");
			filename += ".html";
			
			if (!fileHandler.saveToFile(filename, parseHTML(_class))) {
				retVal = false;
				break;
			}
		}
		
		return retVal;
	}
	
}
