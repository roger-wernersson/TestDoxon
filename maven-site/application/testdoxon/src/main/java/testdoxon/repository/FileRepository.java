package testdoxon.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.plugin.AbstractMojo;

import testdoxon.exception.TDException;
import testdoxon.model.TDClass;
import testdoxon.model.TDMethod;
import testdoxon.util.TDConstants;

public class FileRepository {

	private ArrayList<String> foldersToCheck;
	private AbstractMojo testdoxonMojo;
	
	public FileRepository (AbstractMojo testdoxonMojo) {
		this.testdoxonMojo = testdoxonMojo;
		this.foldersToCheck = new ArrayList<String>();
	}

	public TDClass[] readFilesFromRootFolder (String rootFolder) {
		ArrayList<TDClass> classes = new ArrayList<TDClass>();
		this.foldersToCheck.clear();
		this.listFolder(rootFolder, classes);
		
		while (this.foldersToCheck.size() != 0) {
			for (String f : new ArrayList<String>(this.foldersToCheck)) {
				this.listFolder(f, classes);
				this.foldersToCheck.remove(f);
			}
		}

		return classes.toArray(new TDClass[classes.size()]);
	}
	
	private ArrayList<TDClass> listFolder (String path, ArrayList<TDClass> classes) {
		File file = new File(path);
		if(file != null && file.isDirectory()) {
			File[] files = file.listFiles();
			
			for (File f : files) {
				if (f.isFile()) {
					if (f.getName().matches("^Test.*\\.java") || f.getName().matches(".*Test\\.java")) {
						try {
							testdoxonMojo.getLog().info("Adding file: " + f.getAbsolutePath());
							classes.add(extractMethods(f.getName(), readFile(f.getAbsolutePath())));
						} catch (TDException e) {
							// Do nothing
						}						
					}
				} else if (f.isDirectory()) {
					testdoxonMojo.getLog().info("Directory: " + f.getAbsolutePath());
					this.foldersToCheck.add(f.getAbsolutePath());
				}
			}
		}
		
		return classes;
	}
	
	private String[] readFile (String filepath) throws TDException {
		ArrayList<String> input = new ArrayList<String>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(filepath));

			String line = br.readLine();
			while (line != null) {
				input.add(line);
				line = br.readLine();
			}

			br.close();
		} catch (IOException e) {
			throw new TDException(TDException.FILE_NOT_FOUND);
		}

		String[] retVal = new String[input.size()];
		retVal = input.toArray(retVal);

		return retVal;
	}
	
	private TDClass extractMethods (String filename, String[] fileContent) {
		TDClass tdClass = new TDClass(filename);
		
		for (int i = 0; i < fileContent.length; i++) {
			// 1. Filter out all method names
			Pattern pattern = Pattern.compile("^[ \t\n]*(public)?[ \t\n]+void[ \t\n]+(test|should)([A-Z0-9]+.*\\(.*\\))");
			Matcher matcher = pattern.matcher(fileContent[i]);

			if (matcher.find()) {
				String _strMatch = matcher.group(0);
				_strMatch = _strMatch.replaceAll("(public|void|test)", "");
				_strMatch = _strMatch.replaceAll("^[ \t\n]*", "");
				
				boolean hasTest = lookForAtTest(fileContent, i);
				boolean hasIgnore = lookForAtIgnore(fileContent, i);
				
				short picIndex = -1;
				if (hasTest && hasIgnore) {
					picIndex = TDConstants.TEST_IGNORE;
				} else if (hasTest && !hasIgnore) {
					picIndex = TDConstants.TEST;
				} else if (!hasTest && hasIgnore) {
					picIndex = TDConstants.IGNORE;
				} else if (!hasTest && !hasIgnore) {
					picIndex = TDConstants.NONE;
				}

				// 2. Check if method name have arguments If not - do not continue
				if (!_strMatch.matches(".*\\(\\)")) {
					TDMethod method = new TDMethod(_strMatch, picIndex);
					tdClass.addMethodname(method);
				} else {
					// 3. Extract method name and separate every word with a space and return
					pattern = Pattern.compile("(.*[^ ]).*\\(");
					matcher = pattern.matcher(_strMatch);

					if (matcher.find()) {
						String matchedWord = matcher.group(1).replaceAll("([A-Z0-9][a-z0-9]*)", "$0 ");
						TDMethod method = new TDMethod(matchedWord, picIndex);
						tdClass.addMethodname(method);
					}
				}
			}
		}

		return tdClass;
	}
	
	private boolean lookForAtTest(String[] fileContent, int lineNumber) {
		if ((lineNumber - 1) < 0) {
			return false;
		}
		if ((lineNumber - 2) < 0) {
			return fileContent[lineNumber - 1].matches("[ \t\n]*@Test.*");
		}

		return fileContent[lineNumber - 1].matches("[ \t\n]*@Test.*")
				|| fileContent[lineNumber - 2].matches("[ \t\n]*@Test.*");
	}

	private boolean lookForAtIgnore(String[] fileContent, int lineNumber) {
		if ((lineNumber - 1) < 0) {
			return false;
		}
		if ((lineNumber - 2) < 0) {
			return fileContent[lineNumber - 1].matches("[ \t\n]*@Ignore.*");
		}
		return fileContent[lineNumber - 1].matches("[ \t\n]*@Ignore.*")
				|| fileContent[lineNumber - 2].matches("[ \t\n]*@Ignore.*");
}
	
	public boolean saveToFile(String filename, String[] fileContent) {
		PrintWriter out = null;
		try {
			testdoxonMojo.getLog().info("Saved to: " + System.getProperty("user.dir") + "/" + filename);
			out = new PrintWriter(TDConstants.JAVA_DOC_FILEPATH + "/" + filename);
			
			for (String line : fileContent) {
				out.println(line);
			}
			
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} finally {
			out.close();
		}
	}
	
}
