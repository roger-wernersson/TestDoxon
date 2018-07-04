package testdoxon.plugin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import testdoxon.generator.HtmlParser;
import testdoxon.handler.FileHandler;
import testdoxon.model.TDClass;
import testdoxon.util.TDGlobals;

@Mojo(name = "testdoxon")
public class TestdoxonPlugin extends AbstractMojo {

	@Parameter(property = "javaDocReportOutputDirectory")
	private String javaDocReportOutputDirectory = TDGlobals.JAVA_DOC_REPORT_OUTPUT_DIR;

	@Parameter(property = "javaDocDestDir")
	private String javaDocDestDir = TDGlobals.JAVA_DOC_DESTINATION_DIR;

	@Parameter(property = "sourceRootFolder")
	private String sourceRootFolder = TDGlobals.SOURCE;

	public void execute() throws MojoExecutionException, MojoFailureException {	
		
		// Read in configuration
		this.setDestinationFolder();
		
		FileHandler fileHandler = new FileHandler(this);

		// Read in all methodnames from test classes
		getLog().info("TestDoxon->Reads in all test classes");
		TDClass[] classes = fileHandler.getAllClassesFromRootFolder(TDGlobals.SOURCE);

		// Generate HTML
		getLog().info("TestDoxon->Parsing html and saves it to " + TDGlobals.DESTINATION);
		HtmlParser parser = new HtmlParser(classes, this);
		String[] htmlFilepaths = parser.saveClassesToHTML();

		// Save pictures
		this.saveImages();

		// Modify javadoc menu
		getLog().info("TestDoxon->Modifying JavaDoc menu");
		fileHandler.addToJavaDocMenu(htmlFilepaths);

		getLog().info("TestDoxon->Done");
	}

	private void setDestinationFolder() {
		TDGlobals.SOURCE = sourceRootFolder;
		
		if ((javaDocReportOutputDirectory.charAt(javaDocReportOutputDirectory.length() - 1) != '/') || 
				javaDocReportOutputDirectory.charAt(javaDocReportOutputDirectory.length() - 1) != '\\') {
			
			javaDocReportOutputDirectory += "/";
		}
		
		TDGlobals.DESTINATION = javaDocReportOutputDirectory + javaDocDestDir;
		TDGlobals.DESTINATION = TDGlobals.DESTINATION.replaceAll("\\\\", "/");
	}

	private void saveImages() {

		// Create path
		String path = TDGlobals.DESTINATION + "/testdoxon/td_pics";
		File file = new File(path);
		file.mkdirs();

		BufferedImage bufferedImageRed = null;
		BufferedImage bufferedImageBlue = null;
		BufferedImage bufferedImageGray = null;
		BufferedImage bufferedImageGreen = null;
		BufferedImage bufferedImageYellow = null;

		try {
			bufferedImageRed = ImageIO.read(TestdoxonPlugin.class.getClassLoader().getResource("images/red.png"));
			bufferedImageBlue = ImageIO.read(TestdoxonPlugin.class.getClassLoader().getResource("images/blue.png"));
			bufferedImageGray = ImageIO.read(TestdoxonPlugin.class.getClassLoader().getResource("images/gray.png"));
			bufferedImageGreen = ImageIO.read(TestdoxonPlugin.class.getClassLoader().getResource("images/green.png"));
			bufferedImageYellow = ImageIO.read(TestdoxonPlugin.class.getClassLoader().getResource("images/yellow.png"));

			File outImage = null;

			outImage = new File(path + "/red.png");
			ImageIO.write(bufferedImageRed, "png", outImage);

			outImage = new File(path + "/blue.png");
			ImageIO.write(bufferedImageBlue, "png", outImage);

			outImage = new File(path + "/gray.png");
			ImageIO.write(bufferedImageGray, "png", outImage);

			outImage = new File(path + "/green.png");
			ImageIO.write(bufferedImageGreen, "png", outImage);

			outImage = new File(path + "/yellow.png");
			ImageIO.write(bufferedImageYellow, "png", outImage);

		} catch (IOException e) {
			getLog().info("Could not read Image");
		}

	}

}
