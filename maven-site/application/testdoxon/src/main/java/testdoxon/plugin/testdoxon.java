package testdoxon.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import testdoxon.generator.HtmlParser;
import testdoxon.handler.FileHandler;
import testdoxon.model.TDClass;
import testdoxon.util.TDConstants;

@Mojo(name = "testdoxon")
public class testdoxon extends AbstractMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		FileHandler fileHandler = new FileHandler(this);
		
		// Check javadoc config file
		
		// Read in all methodnames from test classes
		getLog().info("Reads in all test classes");
		TDClass[] classes = fileHandler.getAllClassesFromRootFolder("C:\\Users\\eschras\\eclipse-workspace\\uhjuhj\\src\\test");
		
		// Generate HTML
		getLog().info("Parsing html and saves it to " + TDConstants.JAVA_DOC_FILEPATH);
		HtmlParser parser = new HtmlParser(classes, this);
		parser.saveClassesToHTML();
		
		// Save pictures 
		
		// Find all non test classes
		
		// Modify javadoc menu
		
		// Modify javadoc class html
		getLog().info("TestDoxon: Done");
	}

}
