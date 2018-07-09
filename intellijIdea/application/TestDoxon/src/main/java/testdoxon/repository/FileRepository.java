package testdoxon.repository;

import testdoxon.exceptionHandler.TDException;
import testdoxon.log.TDLog;
import testdoxon.model.TDTableItem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileRepository {
    /**
     * constructor
     */
    public FileRepository() {
    }

    /**
     * Returns null if method name is not found
     *
     * @param filePath
     * @return String[]
     * @throws TDException
     */
    public TDTableItem[] fetchMethodNames(String filePath) throws TDException {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        } else {
            String[] fileContent = this.readFileContent(filePath);
            TDTableItem[] methodNames = this.extractMethodNames(fileContent);

            if (methodNames.length == 0) {
                return null;
            }
            return methodNames;
        }
    }

    /**
     * @param filePath
     * @param methodName
     * @return int
     * @throws TDException
     */
    public int findLineNumberOfMethod(String filePath, String methodName) throws TDException {
        if (filePath == null || filePath.isEmpty()) {
            return -1;
        } else {
            String[] fileContent = this.readFileContent(filePath);
            return this.findLineNumberOfMethod(fileContent, methodName) + 1;
        }
    }

    /**
     * @param filePath
     * @return String[]
     * @throws TDException
     */
    private String[] readFileContent(String filePath) throws TDException {
        ArrayList<String> input = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            String line = br.readLine();
            while (line != null) {
                input.add(line);
                line = br.readLine();
            }

            br.close();
        } catch (IOException e) {
            TDLog.info(e.getMessage(), TDLog.ERROR);
            throw new TDException(TDException.FILE_NOT_FOUND);
        }

        String[] retVal = new String[input.size()];
        retVal = input.toArray(retVal);

        return retVal;
    }

    /**
     * Gives the name of a method with parameters. If parameters are not present the
     * method name will be returned separated with spaces.
     *
     * @param fileContent
     * @return String[]
     */
    private TDTableItem[] extractMethodNames(String[] fileContent) {
        ArrayList<TDTableItem> methodNames = new ArrayList<>();

        for (int i = 0; i < fileContent.length; i++) {
            if (fileContent[i].matches("^[ \t\n]*(public)?[ \t\n]*void[ \t\n]+.*\\(.*\\).*")) {
                // Match on method name, now strip unwanted stuf!
                fileContent[i] = fileContent[i].replaceAll("(public|void|\\{|\\})", "");
                fileContent[i] = fileContent[i].replaceAll("^[ \t\n]*", "");

                boolean hasTest = lookForAtTest(fileContent, i);
                boolean hasIgnore = lookForAtIgnore(fileContent, i);
                boolean hasTestInName = fileContent[i].matches("^test.*");

                fileContent[i] = fileContent[i].replaceAll("test", "");

                if (fileContent[i].matches(".*\\(\\).*")) {
                    fileContent[i] = fileContent[i].replaceAll("\\(.*\\).*", "");
                    fileContent[i] = fileContent[i].replaceAll("([A-Z0-9][a-z0-9]*)", "$0 ");
                }

                methodNames.add(new TDTableItem(fileContent[i], hasTest, hasIgnore, hasTestInName));
            }
        }

        TDTableItem[] retVal = new TDTableItem[methodNames.size()];
        retVal = methodNames.toArray(retVal);

        return retVal;
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

    /**
     * @param fileContent
     * @param methodName
     * @return int
     */
    private int findLineNumberOfMethod(String[] fileContent, String methodName) {
        if (!methodName.matches(".*\\(.*\\).*")) {
            methodName = methodName.replaceAll(" ", "");
            methodName += "[ \t\n]*\\([ \t\n]*\\)";

        } else {
            methodName = methodName.replaceAll("([\\(\\)])", "\\\\$0");
        }

        final String regex = "^[ \t\n]*public.*void.*(test|should)" + methodName + ".*";

        int result = -1;
        for (int i = 0; i < fileContent.length - 1; i++) {
            if (fileContent[i].matches(regex)) {
                result = i;
                break;
            }
        }

        return result;
    }
}
