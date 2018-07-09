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

package testdoxon.util;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;

import testdoxon.handler.FileHandler;
import testdoxon.log.TDLog;
import testdoxon.model.TDFile;
import testdoxon.views.View;

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
			if (View.currentOpenFile.getName().matches("^Test.*") || View.currentOpenFile.getName().matches(".*Test.java")) {
				View.currentTestFile = View.currentOpenFile;
				// If a regular class is open
			} else {
				
				String testFilepath = DoxonUtils.createTestPath(View.currentOpenFile);
				String newTestFilepathPre = testFilepath + "Test" + View.currentOpenFile.getName();
				
				String newTestFilepathPost = testFilepath + View.currentOpenFile.getName().replaceAll("\\.java", "") + "Test.java";

				FileHandler filehandler = new FileHandler();
				if (filehandler.fileExists(newTestFilepathPre)) {
					View.currentTestFile = new TDFile(new File(newTestFilepathPre));
				} else if (filehandler.fileExists(newTestFilepathPost)) {
					View.currentTestFile = new TDFile(new File(newTestFilepathPost));
				}else {
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
						TDLog.info(e.getMessage(), TDLog.WARNING);
					}

				}
			});
		}
	}

}