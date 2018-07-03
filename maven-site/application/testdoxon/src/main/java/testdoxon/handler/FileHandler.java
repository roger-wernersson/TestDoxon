package testdoxon.handler;

import java.util.ArrayList;

import testdoxon.model.TDClass;
import testdoxon.repository.FileRepository;

public class FileHandler {

	private FileRepository fileRepository;

	public FileHandler() {
		this.fileRepository = new FileRepository();
	}

	public TDClass[] getAllClassesFromFolder(String filepath) {
		ArrayList<TDClass> classes = new ArrayList<TDClass>();

		// Loop thru every folder and file
		for (int i = 0; i < 10; i++) {
			this.getAllClassesFromFolder(classes, filepath);
		}

		return classes.toArray(new TDClass[classes.size()]);
	}

	public boolean saveToFile(String filename, String[] fileContent) {
		return fileRepository.saveToFile(filename, fileContent);
	}

	private ArrayList<TDClass> getAllClassesFromFolder(ArrayList<TDClass> classes, String filepath) {
		String[] fileContent = this.fileRepository.readFile(filepath);
		TDClass _class = this.fileRepository.extractMethods(fileContent);

		if (_class != null) {
			classes.add(_class);
		}

		return classes;
	}

}
