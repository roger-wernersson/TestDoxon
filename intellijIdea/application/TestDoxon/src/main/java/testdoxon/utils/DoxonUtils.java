package testdoxon.utils;

public class DoxonUtils {
    /**
     *
     * @param file
     * @return String
     */
    public static String createTestPath(TDFile file) {

        if(file == null) {
            return null;
        }
        String[] parts;
        if (System.getProperty("os.name").contains("Windows")) {
            parts = file.getAbsolutePath().split("\\\\");
        } else {
            String filepath = file.getAbsolutePath();
            filepath.replaceAll("( )", "\\$0");
            parts = filepath.split("/");
        }

        String newFile = "";

        for (int i = 0; i < parts.length - 1; i++) {
            if (parts[i].equals("main")) {
                newFile += "test/";
            } else {
                newFile += parts[i] + "/";
            }
        }
        return newFile;
    }

    public static String createTestPath(String file) {

        if(file == null) {
            return null;
        }
        String[] parts;
        if (System.getProperty("os.name").contains("Windows")) {
            parts = file.split("\\\\");
        } else {
            String filepath = file;
            filepath.replaceAll("( )", "\\$0");
            parts = filepath.split("/");
        }

        String newFile = "";

        for (int i = 0; i < parts.length - 1; i++) {
            if (parts[i].equals("main")) {
                newFile += "test/";
            } else {
                newFile += parts[i] + "/";
            }
        }
        return newFile;
    }

    /**
     * Locates test folder
     *
     * @param filepath
     * @return String
     */
    public static String findRootFolder(String filepath) {
        if(filepath == null || filepath.isEmpty()) {
            return null;
        }

        View.orgRootFolder = filepath;

        String[] parts;
        if (System.getProperty("os.name").contains("Windows")) {
            parts = filepath.split("\\\\");
        } else {
            filepath.replaceAll("( )", "\\$0");
            parts = filepath.split("/");
        }

        ArrayList<String> filepathBuilder = new ArrayList<>();

        boolean copy = true;
        for(int i = 0; i < (parts.length - 1); i++) {
            if (parts[i].equals("src")) {
                filepathBuilder.add(parts[i]);
                copy = false;
                break;
            }

            if(copy) {
                filepathBuilder.add(parts[i]);
            }
        }

        String newFilepath = "";
        for(int i = 0; i < (filepathBuilder.size() - View.rootJumpbacks); i++) {
            newFilepath += filepathBuilder.get(i) + "/";
        }

        View.rootFolder = newFilepath;
        return newFilepath;
    }

    /**
     *
     * @param pos
     * @param text
     * @return String
     */
    public static String getWordUnderCaret(int pos, StyledText text) {
        int lineOffset = pos - text.getOffsetAtLine(text.getLineAtOffset(pos));
        String line = text.getLine(text.getLineAtOffset(pos));
        String[] words = line.split("[ \t\\\\(\\\\);\\\\.{}]");
        String desiredWord = "";

        for (String word : words) {
            if (lineOffset <= word.length()) {
                desiredWord = word;
                break;
            }
            lineOffset -= word.length() + 1;
        }

        return desiredWord;
    }

    /**
     * Decides whether a test class is open or not and locates the path
     *
     * @param viewer
     */
    public static void findFileToOpen(TableViewer viewer) {
        if (View.currentOpenFile != null) {
            // If a test class already is open
            if (View.currentOpenFile.getName().matches("^Test.*")) {
                View.currentTestFile = View.currentOpenFile;
                // If a regular class is open
            } else {
                String newTestFilepath = DoxonUtils.createTestPath(View.currentOpenFile) + "Test"
                        + View.currentOpenFile.getName();

                FileHandler filehandler = new FileHandler();
                if (filehandler.fileExists(newTestFilepath)) {
                    View.currentTestFile = new TDFile(new File(newTestFilepath));
                } else {
                    View.currentTestFile = null;
                }
            }

            if (View.currentTestFile != null) {
                View.currentTestFile.setHeaderFilepath(View.currentOpenFile.getAbsolutePath());
            }

            Display.getDefault().syncExec(new Runnable() {
                @Override
                public void run() {
                    try {
                        viewer.setInput(View.currentTestFile);
                    } catch (AssertionFailedException e) {
                        // Do nothing
                    }

                }
            });
        }
    }
}
