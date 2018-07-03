package testdoxon.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import testdoxon.generator.HtmlParser;
import testdoxon.handler.FileHandler;
import testdoxon.model.TDClass;
import testdoxon.util.TDGlobals;

@Mojo(name = "testdoxon")
public class testdoxon extends AbstractMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		FileHandler fileHandler = new FileHandler(this);

		// Read in properties
		this.checkProperties();

		if (!TDGlobals.prop.getProperty("source").equals(".")) {
			// Read in all methodnames from test classes
			getLog().info("TestDoxon->Reads in all test classes");
			TDClass[] classes = fileHandler.getAllClassesFromRootFolder(TDGlobals.prop.getProperty("source"));

			// Generate HTML
			getLog().info("TestDoxon->Parsing html and saves it to " + TDGlobals.prop.getProperty("destination"));
			HtmlParser parser = new HtmlParser(classes, this);
			String[] htmlFilepaths = parser.saveClassesToHTML();

			// Save pictures

			// Find all non test classes

			// Modify javadoc menu
			getLog().info("TestDoxon->Modifying JavaDoc menu");
			fileHandler.addToJavaDocMenu(htmlFilepaths);
			
			// Modify javadoc class html
			
		} else {
			getLog().info("Error: Set source folder in source.cgf");
		}
		getLog().info("TestDoxon->Done");
	}

	private void checkProperties() {
		File file = new File(TDGlobals.CONFIG_FILE);
		if (!file.exists()) {
			this.createProperties();
		} else {
			this.loadProperties();
		}
	}

	private void createProperties() {
		OutputStream out = null;
		try {
			out = new FileOutputStream(TDGlobals.CONFIG_FILE);
			TDGlobals.prop.setProperty("source", ".");
			TDGlobals.prop.setProperty("destination", ".");
			TDGlobals.prop.store(out, null);
		} catch (IOException e) {
			// Do nothing
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// Do nothing
				}
			}
		}
	}

	private void loadProperties() {
		InputStream input = null;
		try {
			input = new FileInputStream(TDGlobals.CONFIG_FILE);
			TDGlobals.prop.load(input);
		} catch (IOException e) {
			// Do nothing
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					// Do nothing
				}
			}
		}
	}

}
