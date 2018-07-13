package testdoxon.handler;

import testdoxon.gui.ClassComboBox;
import testdoxon.model.TestFile;
import testdoxon.repository.FileCrawlerRepository;
import testdoxon.utils.DoxonUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FileCrawlerHandler {
    private FileCrawlerRepository fileCrawlerRepository;
    //private ListViewer list;

    public FileCrawlerHandler() {
        this.fileCrawlerRepository = new FileCrawlerRepository();
    }

    /**
     * @param path
     */
    public void getAllTestClasses(String path, ClassComboBox testClassesComboBox) {
        this.fileCrawlerRepository.checkFolderHierarchy(path, testClassesComboBox);
    }

    /**
     * Returns null if nothing is found or something is wrong
     *
     * @param filename
     * @param currentFilepath
     * @param currentFilename
     * @return String
     */
    public String getTestFilepathFromFilename(String filename, String currentFilepath, String currentFilename, ClassComboBox testClassPathsComboBox) {
        if(currentFilepath == null) {
            return null;
        }
        // Get the location of where the actual test class should be
        currentFilepath = currentFilepath.replaceAll("\\\\main\\\\", "\\\\test\\\\");
        currentFilepath = currentFilepath.replaceAll("\\\\" + currentFilename, "");
        currentFilepath += "\\" + filename;

        String testFilepath = null;
        ArrayList<TestFile> foundedFilepaths = new ArrayList<>();

        ArrayList<TestFile> testfiles = fileCrawlerRepository.getAllTestFiles();

        for (TestFile f : testfiles) {
            // Find all files that have the same name as the file we are looking for
            if (f.compareFilename(filename)) {
                foundedFilepaths.add(f);
            }
        }

        // If the exact location has not been found - just return the first file in the
        // array
        if (foundedFilepaths.size() > 0) {
            boolean found = false;
            for (TestFile testFile : foundedFilepaths) {
                if (testFile.getFilepath().equals(currentFilepath)) {
                    testFilepath = testFile.getFilepath();
                    found = true;
                }
            }

            if (!found) {
                testFilepath = foundedFilepaths.get(0).getFilepath();
            }

            // Update combo viewer with all current finds
            DoxonUtils.setComboBoxItems(testClassPathsComboBox, foundedFilepaths.toArray(new TestFile[foundedFilepaths.size()]));
        }

        return testFilepath;
    }

    public void printAllTestfiles() {
        ArrayList<TestFile> testfiles = fileCrawlerRepository.getAllTestFiles();
        for (TestFile f : testfiles) {
            System.out.println(f.toString());
        }
    }

    public TestFile[] getAllTestClassesAsTestFileArray() {
        ArrayList<TestFile> testClasses = fileCrawlerRepository.getAllTestFiles();
        return testClasses.toArray(new TestFile[testClasses.size()]);
    }

    public ArrayList<TestFile> contains(String filename) {
        ArrayList<TestFile> matches = new ArrayList<>();

        for (TestFile testfile : fileCrawlerRepository.getAllTestFiles()) {
            if (testfile.getFilename().equals(filename)) {
                matches.add(testfile);
            }
        }

        return matches;
    }

    public boolean listContains(String path) {
        return this.fileCrawlerRepository.listContains(path);
    }
    public void addToList(TestFile testFile) {
        this.fileCrawlerRepository.addToList(testFile);
    }
    public int getNrOfTestClasses() { return this.fileCrawlerRepository.getNrOfTestClasses(); }
    public int getNrOfProdClasses() { return this.fileCrawlerRepository.getNrOfProdClasses(); }

    public TestFile[] getAllSingleTestClasses () {
        ArrayList<TestFile> testFiles = fileCrawlerRepository.getTestFiles();
        ArrayList<TestFile> prodFiles = fileCrawlerRepository.getProdFiles();

        ArrayList<TestFile> singleTestClasses = new ArrayList<>();

        for (int i = 0; i < testFiles.size(); i++) {
            boolean found = false;
            for (int j = 0; j < prodFiles.size(); j++) {
                if (testFiles.get(i).getFilenameWithoutTest().equals(prodFiles.get(j).getFilename())) {
                    found = true;
                }
            }

            if (!found) {
                singleTestClasses.add(testFiles.get(i));
            }
        }

        return singleTestClasses.toArray(new TestFile[singleTestClasses.size()]);
    }

    public TestFile[] getAllSingleProdClasses () {
        ArrayList<TestFile> testFiles = fileCrawlerRepository.getTestFiles();
        ArrayList<TestFile> prodFiles = fileCrawlerRepository.getProdFiles();

        ArrayList<TestFile> singleProdClasses = new ArrayList<>();

        for (int i = 0; i < prodFiles.size(); i++) {
            boolean found = false;
            for (int j = 0; j <testFiles.size(); j++) {
                if (prodFiles.get(i).getFilename().equals(testFiles.get(j).getFilenameWithoutTest())) {
                    found = true;
                }
            }

            if (!found) {
                singleProdClasses.add(prodFiles.get(i));
            }
        }

        return singleProdClasses.toArray(new TestFile[singleProdClasses.size()]);
    }
}
