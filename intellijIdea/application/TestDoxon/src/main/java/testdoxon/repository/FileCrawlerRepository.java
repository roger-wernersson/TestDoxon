package testdoxon.repository;

import testdoxon.gui.ClassComboBox;
import testdoxon.log.TDLog;
import testdoxon.model.TestFile;
import testdoxon.utils.DoxonUtils;
import testdoxon.utils.TDStatics;

import java.io.File;
import java.util.ArrayList;

public class FileCrawlerRepository {
    private ArrayList<TestFile> testFiles;
    private ArrayList<TestFile> prodFiles;
    private ArrayList<String> foldersToCheck;

    /**
     * Constructor
     */
    public FileCrawlerRepository() {
        this.testFiles = new ArrayList<>();
        this.foldersToCheck = new ArrayList<>();
        this.prodFiles = new ArrayList<>();
    }

    /**
     * Recursively walk through folders
     *
     * @param path
     */
    public void checkFolderHierarchy(String path, ClassComboBox testClassesComboBox) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Long last = System.currentTimeMillis();
                testFiles.clear();
                foldersToCheck.clear();
                prodFiles.clear();

                listFolder(path);

                while (foldersToCheck.size() != 0) {
                    for (String f : new ArrayList<String>(foldersToCheck)) {
                        listFolder(f);
                        foldersToCheck.remove(f);
                    }
                }

                TDLog.info("Testclasses in memory: " + testFiles.size(), TDLog.INFORMATION);
                DoxonUtils.setComboBoxItems(testClassesComboBox, testFiles.toArray(new TestFile[testFiles.size()]));
                TDStatics.ms_recursiveRead = System.currentTimeMillis() - last;
            }
        });
        thread.start();

    }

    /**
     * Checks content of folders, saves all TestXx...x.java classes
     *
     * @param path
     */
    private void listFolder(String path) {
        File file = new File(path);
        if (file != null && file.isDirectory()) {
            File[] files = file.listFiles();

            if (files != null) {
                for (File f : files) {
                    if (f.isFile()) {
                        if (f.getName().matches("^Test.*\\.java") || f.getName().matches(".*Test\\.java")) {
                            this.testFiles.add(new TestFile(f.getName(), f.getAbsolutePath()));
                        } else if (f.getName().matches(".*\\.java")) {
                            this.prodFiles.add(new TestFile(f.getName(), f.getAbsolutePath()));
                        }
                    } else if (f.isDirectory()) {
                        this.foldersToCheck.add(f.getAbsolutePath());
                    }
                }
            } else {
                TDLog.info("Too mant jumpbacks, will not load classes", TDLog.ERROR);
            }
        }
    }

    public boolean listContains(String path) {
        for (TestFile testfile : testFiles) {
            if (testfile.getFilepath().equals(path)) {
                return true;
            }
        }
        return false;
    }

    public void addToList(TestFile testFile) {
        this.testFiles.add(testFile);
    }

    /**
     * @return ArrayList<TestFile>
     */
    public ArrayList<TestFile> getAllTestFiles() {
        return this.testFiles;
    }

    public int getNrOfTestClasses() { return this.testFiles.size(); }
    public int getNrOfProdClasses() { return this.prodFiles.size(); }
}
