package testdoxon.handler;

import org.apache.maven.plugin.AbstractMojo;

import testdoxon.model.TDClass;
import testdoxon.repository.FileRepository;

public class FileHandler {

	private FileRepository fileRepository;

	public FileHandler(AbstractMojo testdoxonMojo) {
		this.fileRepository = new FileRepository(testdoxonMojo);
	}

	public boolean saveToFile(String filename, String[] fileContent) {
		return fileRepository.saveToFile(filename, fileContent);
	}

	public TDClass[] getAllClassesFromRootFolder(String rootFolderPath) {
		TDClass[] classes = this.fileRepository.readFilesFromRootFolder(rootFolderPath);
		return classes;
	}

}
