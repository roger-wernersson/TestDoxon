package testdoxon.model;

import testdoxon.utils.DoxonUtils;

import java.io.File;

public class TDFile {
    File file;
    String headerFilepath;
    String filepath;

    public TDFile(File file) {
        this.file = file;
        this.headerFilepath = null;

        if (this.file.getName().matches("^Test.*") || this.file.getName().matches(".*Test\\.java")) {
            this.filepath = this.file.getAbsolutePath().replaceAll("\\\\", "/");
        } else {
            this.filepath = DoxonUtils.createTestPath(file.getAbsolutePath()) + "Test" + file.getName();
        }
    }

    public String getAbsolutePath() {
        return this.file.getAbsolutePath();
    }

    public String getPath() {
        return this.filepath;
    }

    public File getFile() { return this.file; }

    public String getName() {
        return this.file.getName();
    }

    public void setHeaderFilepath(String filepath) {
        this.headerFilepath = filepath;
    }

    /**
     *
     * @return String
     */
    public String getHeaderName() {
        String packageName = null;
        if(this.headerFilepath == null) {
            this.headerFilepath = file.getAbsolutePath();
        }
        if (this.headerFilepath != null) {
            String[] parts;
            if (System.getProperty("os.name").contains("Windows")) {
                parts = this.headerFilepath.split("\\\\");
            } else {
                String filepath = this.headerFilepath;
                filepath.replaceAll("( )", "\\$0");
                parts = filepath.split("/");
            }
            ;
            boolean startCopy = false;
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].equals("src")) {
                    startCopy = true;
                    packageName = "";
                    // i += 3;
                    continue;
                } else if (parts[i].equals("main")) {
                    continue;
                } else if (parts[i].equals("java")) {
                    continue;
                } else if (parts[i].equals("resources")) {
                    continue;
                }

                if (startCopy) {
                    packageName += parts[i];
                    if (i != (parts.length - 1)) {
                        packageName += ".";
                    }
                }
            }
        }
        if (packageName != null) {
            return packageName;
        }
        return this.file.getName();
    }
}
