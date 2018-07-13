/**
Copyright 2018 Delicate Sound Of Software AB, All Rights Reserved

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package testdoxon.handler;

import java.util.ArrayList;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.widgets.Display;

import testdoxon.log.TDLog;
import testdoxon.model.TestFile;
import testdoxon.repository.FileCrawlerRepository;

public class FileCrawlerHandler {

	private FileCrawlerRepository fileCrawlerRepository;
	//private ListViewer list;

	public FileCrawlerHandler() {
		this.fileCrawlerRepository = new FileCrawlerRepository();
	}

	/**
	 * 
	 * @param path
	 */
	public void getAllTestClasses(String path, ComboViewer testClassPathsComboBox) {
		this.fileCrawlerRepository.checkFolderHierarchy(path, testClassPathsComboBox);
	}

	/**
	 * Returns null if nothing is found or something is wrong
	 * 
	 * @param filename
	 * @param currentFilepath
	 * @param currentFilename
	 * @return String
	 */
	public String getTestFilepathFromFilename(String filename, String currentFilepath, String currentFilename, ComboViewer testClassPathsComboBox) {
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
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					try {
						testClassPathsComboBox.setInput(foundedFilepaths.toArray(new TestFile[foundedFilepaths.size()]));
					} catch (AssertionFailedException e) {
						TDLog.info(e.getMessage(), TDLog.WARNING);
					}
				}
			});
		}
		
		
			

		return testFilepath;
	}

	public void printAllTestfiles() {
		ArrayList<TestFile> testfiles = fileCrawlerRepository.getAllTestFiles();
		for (TestFile f : testfiles) {
			System.out.println(f.toString());
		}
	}
	
	public TestFile[] getAllTestClassesAsTestFileArray () {
		ArrayList<TestFile> testClasses = fileCrawlerRepository.getAllTestFiles();
		return testClasses.toArray(new TestFile[testClasses.size()]);
	}
	
	public ArrayList<TestFile> contains(String filename) {
		ArrayList<TestFile> matches = new ArrayList<>();
		
		for(TestFile testfile : fileCrawlerRepository.getAllTestFiles()) {
			if(testfile.getFilename().equals(filename)) {
				matches.add(testfile);
			}
		}
		
		return matches;
	}
	
	public boolean listContains(String path) {
		return this.fileCrawlerRepository.listContains(path);
	}
	
	public void addToList (TestFile testFile) {
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