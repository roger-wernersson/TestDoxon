package testdoxon.models;

import java.io.File;

public class TDFile {
	File file;
	String headerFilepath;

	public TDFile(File file) {
		this.file = file;
		this.headerFilepath = null;
	}

	public String getAbsolutePath() {
		return this.file.getAbsolutePath();
	}

	public String getName() {
		return this.file.getName();
	}

	public void setHeaderFilepath(String filepath) {
		this.headerFilepath = filepath;
	}

	public String getHeaderName() {
		String packageName = "Package name not found.";
		if (this.headerFilepath != null) {
			String[] parts = this.headerFilepath.split("\\\\");
			boolean startCopy = false;
			for (int i = 0; i < parts.length; i++) {
				if (parts[i].equals("src")) {
					startCopy = true;
					packageName = "";
					i += 3;
				}

				if (startCopy) {
					packageName += parts[i];
					if (i != (parts.length - 1)) {
						packageName += ".";
					}
				}
			}
		}
		return packageName;
	}

}
