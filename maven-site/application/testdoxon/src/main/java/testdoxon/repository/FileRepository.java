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
import testdoxon.util.TDGlobals;

public class FileRepository {

	private ArrayList<String> foldersToCheck;
	private AbstractMojo testdoxonMojo;

	public FileRepository(AbstractMojo testdoxonMojo) {
		this.testdoxonMojo = testdoxonMojo;
		this.foldersToCheck = new ArrayList<String>();
	}

	public TDClass[] readFilesFromRootFolder(String rootFolder) {
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

	private ArrayList<TDClass> listFolder(String path, ArrayList<TDClass> classes) {
		File file = new File(path);
		if (file != null && file.isDirectory()) {
			File[] files = file.listFiles();

			for (File f : files) {
				if (f.isFile()) {
					if (f.getName().matches("^Test.*\\.java") || f.getName().matches(".*Test\\.java")) {
						try {
							classes.add(extractMethods(f.getName(), f.getAbsolutePath(), readFile(f.getAbsolutePath())));
						} catch (TDException e) {
							// Do nothing
						}
					}
				} else if (f.isDirectory()) {
					this.foldersToCheck.add(f.getAbsolutePath());
				}
			}
		}

		return classes;
	}

	private String[] readFile(String filepath) throws TDException {
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

	private TDClass extractMethods(String filename, String filepath, String[] fileContent) {
		TDClass tdClass = new TDClass(filename, filepath);

		for (int i = 0; i < fileContent.length; i++) {
			// 1. Filter out all method names
			Pattern pattern = Pattern
					.compile("^[ \t\n]*(public)?[ \t\n]+void[ \t\n]+(test|should)([A-Z0-9]+.*\\(.*\\))");
			Matcher matcher = pattern.matcher(fileContent[i]);

			if (matcher.find()) {
				String _strMatch = matcher.group(0);
				_strMatch = _strMatch.replaceAll("(public|void|test)", "");
				_strMatch = _strMatch.replaceAll("^[ \t\n]*", "");

				boolean hasTest = lookForAtTest(fileContent, i);
				boolean hasIgnore = lookForAtIgnore(fileContent, i);

				short picIndex = -1;
				if (hasTest && hasIgnore) {
					picIndex = TDGlobals.TEST_IGNORE;
				} else if (hasTest && !hasIgnore) {
					picIndex = TDGlobals.TEST;
				} else if (!hasTest && hasIgnore) {
					picIndex = TDGlobals.IGNORE;
				} else if (!hasTest && !hasIgnore) {
					picIndex = TDGlobals.NONE;
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

	public ArrayList<String> saveHTMLToFile(ArrayList<String> htmlFilepaths, String filename, String filepath, String[] fileContent) {
		String _package = this.createPackage(filepath);
		if (this.saveToFile(TDGlobals.prop.getProperty("destination") + "/" + _package + filename, fileContent)) {
			htmlFilepaths.add(_package + filename);
		}
		
		return htmlFilepaths;
	}
	
	private boolean saveToFile (String filepath, String[] fileContent) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(filepath);

			for (String line : fileContent) {
				out.println(line);
			}
			
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	private String createPackage(String filepath) {		
		// Extract package name
		String packageName = this.getPackage(filepath);
		String destination = TDGlobals.prop.getProperty("destination") + "/" + packageName;
		String[] parts = destination.split("/");
		
		String _filepath = "";
		for (int i = 0; i < parts.length; i++) {
			_filepath += parts[i] + "/";
			
			File file = new File(_filepath);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		
		return packageName;
	}

	private String getPackage (String filepath) {
		String[] parts;
		if (System.getProperty("os.name").contains("Windows")) {
			parts = filepath.split("\\\\");
		} else {
			filepath.replaceAll("( )", "\\$0");
			parts = filepath.split("/");
		}
		
		String packageName = "";
		boolean startCopy = false;
		for (int i = 0; i < parts.length - 1; i++) {
			if (parts[i].equals("java")) {
				startCopy = true;
				continue;
			}
			
			if (startCopy) {
				packageName += parts[i] + "/";
			}
		}
		
		return packageName;
	}

	public void addFilepathsToJavaDocMenu(String[] htmlFilepaths) {
		try {
			// allclasses-frame.html
			String[] content = this.readFile(TDGlobals.prop.getProperty("destination") + "/allclasses-frame.html");
			String[] newContent = this.modifyMenu(htmlFilepaths, content, true);
			this.saveToFile(TDGlobals.prop.getProperty("destination") + "/allclasses-frame.html", newContent);
			
			// allclasses-noframe.html
			content = this.readFile(TDGlobals.prop.getProperty("destination") + "/allclasses-noframe.html");
			newContent = this.modifyMenu(htmlFilepaths, content, false);
			this.saveToFile(TDGlobals.prop.getProperty("destination") + "/allclasses-noframe.html", newContent);
		} catch (TDException e) {

		}		
	}

	
	private String[] modifyMenu (String[] htmlFilepaths, String[] fileContent, boolean frame) {
		String[] newContent = new String[fileContent.length + htmlFilepaths.length];
		
		int newContentCounter = 0;
		for (int i = 0; i < fileContent.length; i++) {
			newContent[newContentCounter] = fileContent[i];
			if (fileContent[i].equals("<div class=\"indexContainer\">") && fileContent[i+1].equals("<ul>")) {
				newContent[newContentCounter + 1] = fileContent[i + 1];
				newContentCounter += 2;
				i += 2;
				while(fileContent[i].contains("<li>")) {
					newContent[newContentCounter] = fileContent[i];
					i++;
					newContentCounter++;
				}
				
				for (int n = 0; n < htmlFilepaths.length; n++) {
					String[] parts = htmlFilepaths[n].split("/");
					newContent[newContentCounter] = "<li><a href=\"" + htmlFilepaths[n] + "\" title=\"class in com.company\"";
					newContent[newContentCounter] += ((frame) ? "target=\"classFrame\">" : ">");
					newContent[newContentCounter] += parts[parts.length - 1] + "</a></li>";
					newContentCounter++;
				}
				
				i--;
			} else {
				newContentCounter++;
			}
		}
		
		return newContent;
	}
	
}


























