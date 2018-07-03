package testdoxon.handler;

import java.util.ArrayList;

import org.apache.maven.plugin.AbstractMojo;

import testdoxon.model.TDClass;
import testdoxon.repository.FileRepository;

public class FileHandler {

	private FileRepository fileRepository;

	public FileHandler(AbstractMojo testdoxonMojo) {
		this.fileRepository = new FileRepository(testdoxonMojo);
	}

	public ArrayList<String> saveHTMLToFile(ArrayList<String> htmlFilepaths, String filename, String filepath, String[] fileContent) {
		return fileRepository.saveHTMLToFile(htmlFilepaths, filename, filepath, fileContent);
	}

	public TDClass[] getAllClassesFromRootFolder(String rootFolderPath) {
		TDClass[] classes = this.fileRepository.readFilesFromRootFolder(rootFolderPath);
		return classes;
	}
	
	public void addToJavaDocMenu (String[] htmlFilepaths) {
		fileRepository.addFilepathsToJavaDocMenu(htmlFilepaths);
	}

}
