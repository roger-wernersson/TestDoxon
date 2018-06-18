package testdoxon.utils;

import testdoxon.models.TDFile;

public class DoxonUtils {

	public synchronized String createTestPath(TDFile file) {
		String[] parts = file.getAbsolutePath().split("\\\\");
		String newFile = "";

		for (int i = 0; i < parts.length - 1; i++) {
			if (parts[i].equals("main")) {
				newFile += "test\\";
			} else {
				newFile += parts[i] + "\\";
			}
		}
		return newFile;
	}

	public String findTestFolder(String filepath) {
		String[] parts = filepath.split("\\\\");
		String newFilepath = "";

		for (String part : parts) {
			if (part.equals("main")) {
				newFilepath += "test";
				break;
			}
			newFilepath += part + "/";
		}

		return newFilepath;
	}

}