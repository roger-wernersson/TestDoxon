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
		
		html.add("body {");
		html.add("color: #353833;");
		html.add("font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif;");
		html.add("margin: 20px;");
		html.add("}");
		
		html.add("h1 {");
		html.add("font-size: 18px;");
		html.add("margin-top: 10px;");
		html.add("padding-top: 0px;");
		html.add("color: #2c4557;");
		html.add("}");
		
		html.add("h2 {");
		html.add("font-size: 16px;");
		html.add("margin-top: 0px;");
		html.add("padding-top: 0px;");
		html.add("font-style: italic;");
		html.add("}");
		
		html.add("h3 {");
		html.add("font-size: 14px;");
		html.add("font-weight: normal;");
		html.add("margin-top: 0px;");
		html.add("margin-bottom: 0px;");
		html.add("padding-bottom: 0px;");
		html.add("}");
		
		html.add("hr {");
		html.add("font-size: 14px;");
		html.add("font-weight: normal;");
		html.add("margin-top: 10px;");
		html.add("margin-bottom: 40px;");
		html.add("}");
		
		html.add(".method {");
		html.add("margin: 5px 0px 0px 15px;");
		html.add("font-family: 'DejaVu Sans Mono', monospace;");
		html.add("padding: 0px;");
		html.add("}");
		
		html.add("#summaryDiv {");
		html.add("background-color: #f8f8f8;");
		html.add("border: 1px solid #ededed;");
		html.add("padding: 15px;");
		html.add("}");
		
		html.add("#summaryHead {");
		html.add("background-color: #dee3e9;");
		html.add("font-size: 13px;");
		html.add("padding: 8px;");
		html.add("font-weight: bold;");
		html.add("overflow: hidden;");
		html.add("border-left: 1px solid #EEE;");
		html.add("border-right: 1px solid #EEE;");
		html.add("border-top: 1px solid #EEE;");
		html.add("}");
		
		html.add("#summaryBody {");
		html.add("background-color: #fff;");
		html.add("font-size: 13px;");
		html.add("padding: 8px;");
		html.add("border-left: 1px solid #EEE;");
		html.add("border-right: 1px solid #EEE;");
		html.add("border-bottom: 1px solid #EEE;");
		html.add("}");
		
		html.add("</style>");
		html.add("</head>");
		html.add("<body>");
				
		html.add("<h3>" + _class.getPackage() + "</h3>");
		html.add("<h1>Class " + _class.getName() + "</h1>");
		
		html.add("<hr />");
		
		html.add("<div id=\"summaryDiv\">");
		html.add("<h2>Test Method summary</h2>");
		
		html.add("<div id=\"summaryHead\">Test method name</div>");
		
		html.add("<div id=\"summaryBody\">");
		for(TDMethod method : _class.getMethods()) {			
			html.add("<p class=\"method\" title=\"" + method.getToolTip() + "\">" + method.getPictureHTML() + " " + method.getMethodname() + "</p>");
		}
		
		html.add("</div>");
		html.add("</div>");
		//html.add("");
		html.add("</body>");
		html.add("</html>");
		
		return html.toArray(new String[html.size()]);
	}
	
	public String[] saveClassesToHTML () {
		ArrayList<String> htmlFilepaths = new ArrayList<String>();
		for (TDClass _class : classes) {
			String filename = _class.getName().replaceAll(".java", "");
			filename += ".html";
			htmlFilepaths = fileHandler.saveHTMLToFile(htmlFilepaths, filename, _class.getFilepath(), parseHTML(_class));
		}
		
		return htmlFilepaths.toArray(new String[htmlFilepaths.size()]);
	}
	
}
