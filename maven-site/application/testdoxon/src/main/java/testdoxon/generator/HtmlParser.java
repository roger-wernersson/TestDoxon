package testdoxon.generator;

import java.util.ArrayList;

import org.apache.maven.plugin.AbstractMojo;

import testdoxon.handler.FileHandler;
import testdoxon.model.TDClass;
import testdoxon.model.TDMethod;

public class HtmlParser {

	private TDClass[] classes;
	private FileHandler fileHandler;
	
	public HtmlParser (TDClass[] classes, AbstractMojo testdoxonMojo) {
		this.classes = classes;
		this.fileHandler = new FileHandler(testdoxonMojo);
	}
	
	private String[] parseHTML (TDClass _class) {
		ArrayList<String> html = new ArrayList<String>();

		html.add("<html>");
		html.add("<head>");
		html.add("<title>" + _class.getName() + "</title>");
		html.add("<style>");
		html.add(".method {");
		html.add("margin: 0px 0px 0px 15px;");
		html.add("padding: 0px;");
		html.add("font-family: verdana;");
		html.add("}");
		html.add("</style>");
		html.add("</head>");
		html.add("<body>");
		
		html.add("<h1>" + _class.getName() + "</h1>");
		
		for(TDMethod method : _class.getMethods()) {
			html.add("<p class=\"method\">" + method.getPictureHTML() + " " + method.getMethodname() + "</p>");
		}
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
