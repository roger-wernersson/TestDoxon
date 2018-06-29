package testdoxon.model;

public class TestFile {
    String filepath;
    String filename;

    public TestFile(String filename, String filepath) {
        this.filepath = filepath;
        this.filename = filename;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return this.filepath;
    }

    public String getFilename() {
        return this.filename;
    }

    public boolean compareFilename(String filename) {
        return this.filename.equals(filename);
    }

    public String toString() {
        return this.getPackage();
    }

    public String getPackage() {
        String packageName = null;
        String[] parts;
        if(System.getProperty("os.name").contains("Windows")) {
            parts = this.filepath.split("\\\\");
        } else {
            String _tmp = this.filepath;
            _tmp.replaceAll("( )", "\\$0");
            parts = _tmp.split("/");
        }

        boolean startCopy = false;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("src")) {
                startCopy = true;
                packageName = "";
                //i += 3;
                continue;
            } else if (parts[i].equals("main")) {
                continue;
            } else if(parts[i].equals("java")) {
                continue;
            } else if(parts[i].equals("resources")) {
                continue;
            }

            if (startCopy) {
                packageName += parts[i];
                if (i != (parts.length - 1)) {
                    packageName += ".";
                }
            }
        }

        if(packageName != null) {
            return packageName;
        }
        return this.filename;
    }
}
