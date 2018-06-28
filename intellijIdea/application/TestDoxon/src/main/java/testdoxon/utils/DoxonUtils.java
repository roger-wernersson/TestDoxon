package testdoxon.utils;

import testdoxon.exceptionHandler.TDException;
import testdoxon.handler.FileHandler;
import testdoxon.model.TDFile;
import testdoxon.model.TDTableItem;
import testdoxon.model.TestFile;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

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

        TDStatics.orgRootFolder = filepath;

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
        for(int i = 0; i < (filepathBuilder.size() - TDStatics.rootJumpbacks); i++) {
            newFilepath += filepathBuilder.get(i) + "/";
        }

        TDStatics.rootFolder = newFilepath;
        return newFilepath;
    }

    /**
     *
     * @param pos
     * @param text
     * @return String
     */
    /*public static String getWordUnderCaret(int pos, StyledText text) {
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
    }*/

    /**
     * Decides whether a test class is open or not and locates the path
     *
     */
    public static void findFileToOpen(JList methodList, JLabel header) {
        if (TDStatics.currentOpenFile != null) {
            // If a test class already is open
            if (TDStatics.currentOpenFile.getName().matches("^Test.*")) {
                TDStatics.currentTestFile = TDStatics.currentOpenFile;
                // If a regular class is open
            } else {
                String newTestFilepath = DoxonUtils.createTestPath(TDStatics.currentOpenFile) + "Test"
                        + TDStatics.currentOpenFile.getName();

                FileHandler filehandler = new FileHandler();
                if (filehandler.fileExists(newTestFilepath)) {
                    TDStatics.currentTestFile = new TDFile(new File(newTestFilepath));
                } else {
                    TDStatics.currentTestFile = null;
                }
            }

            if (TDStatics.currentTestFile != null) {
                TDStatics.currentTestFile.setHeaderFilepath(TDStatics.currentOpenFile.getAbsolutePath());
            }


           DoxonUtils.setListItems(methodList, header);
        }
    }

    public static void setListItems(JList methodList, JLabel header) {
        FileHandler fileHandler = new FileHandler();
        try {
            header.setText(TDStatics.currentTestFile.getHeaderName());
            TDTableItem[] items = fileHandler.getMethodsFromFile(TDStatics.currentTestFile.getAbsolutePath());
            methodList.setListData(items);
        } catch (TDException e) {
            e.printStackTrace();
        }
    }

    public static void setComboBoxItems (JComboBox testClassesComboBox, TestFile[] classes) {
        testClassesComboBox.removeAllItems();

        for(TestFile testFile : classes) {
            testClassesComboBox.addItem(testFile);
        }
    }
}
