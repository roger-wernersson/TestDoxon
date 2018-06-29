package testdoxon.utils;

import testdoxon.exceptionHandler.TDException;
import testdoxon.gui.ClassComboBox;
import testdoxon.handler.FileHandler;
import testdoxon.model.TDFile;
import testdoxon.model.TDTableItem;
import testdoxon.model.TestFile;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class DoxonUtils {
    /**
     * @param file
     * @return String
     */
    public static String createTestPath(TDFile file) {

        if (file == null) {
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

        if (file == null) {
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
        if (filepath == null || filepath.isEmpty()) {
            System.out.print("Return null");
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
        for (int i = 0; i < (parts.length - 1); i++) {
            if (parts[i].equals("src")) {
                filepathBuilder.add(parts[i]);
                copy = false;
                break;
            }

            if (copy) {
                filepathBuilder.add(parts[i]);
            }
        }

        String newFilepath = "";
        for (int i = 0; i < (filepathBuilder.size() - TDStatics.rootJumpbacks); i++) {
            newFilepath += filepathBuilder.get(i) + "/";
        }

        TDStatics.rootFolder = newFilepath;
        return newFilepath;
    }

    /**
     * Decides whether a test class is open or not and locates the path
     */
    private void findFileToOpen() {
        if (TDStatics.currentOpenFile != null) {
            // If a test class already is open
            if (TDStatics.currentOpenFile.getName().matches("^Test.*") || TDStatics.currentOpenFile.getName().matches(".*Test\\.java")) {
                TDStatics.currentTestFile = TDStatics.currentOpenFile;
                // If a regular class is open
            } else {
                String testFilepath = DoxonUtils.createTestPath(TDStatics.currentOpenFile);
                String newTestFilepathpre = testFilepath + "Test" + TDStatics.currentOpenFile.getName();
                String newTestFilepathpost = testFilepath + TDStatics.currentOpenFile.getName().replaceAll("\\.java", "") + "Test.java";

                FileHandler filehandler = new FileHandler();
                if (filehandler.fileExists(newTestFilepathpre)) {
                    TDStatics.currentTestFile = new TDFile(new File(newTestFilepathpre));
                } else if (filehandler.fileExists(newTestFilepathpost)) {
                    TDStatics.currentTestFile = new TDFile(new File(newTestFilepathpost));
                } else {
                    TDStatics.currentTestFile = null;
                }
            }

            if (TDStatics.currentTestFile != null) {
                TDStatics.currentTestFile.setHeaderFilepath(TDStatics.currentOpenFile.getAbsolutePath());
            }
        }
    }

    public static void findFileToOpen(JList methodList, JLabel header) {
        DoxonUtils du = new DoxonUtils();
        du.findFileToOpen();

        DoxonUtils.setListItems(methodList, header);
    }

    synchronized public static void setListItems(JList methodList, JLabel header) {
        if (TDStatics.currentTestFile != null) {
            FileHandler fileHandler = new FileHandler();
            try {
                header.setText(TDStatics.currentTestFile.getHeaderName());
                TDTableItem[] items = fileHandler.getMethodsFromFile(TDStatics.currentTestFile.getAbsolutePath());
                if (items != null) {
                    methodList.setListData(items);
                } else {
                    methodList.setListData(new String[]{});
                }
                return;
            } catch (TDException e) {
                e.printStackTrace();
            }
        } else {
            header.setText("Test class not found");
            methodList.setListData(new String[]{});
            return;
        }
    }

    synchronized public static void setComboBoxItems(ClassComboBox testClassesComboBox, TestFile[] classes) {
        testClassesComboBox.removeAllItems();

        for (TestFile testFile : classes) {
            testClassesComboBox.addItem(testFile);
        }
    }
}
