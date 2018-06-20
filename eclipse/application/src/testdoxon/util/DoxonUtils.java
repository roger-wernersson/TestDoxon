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

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;

import testdoxon.model.TDFile;
import testdoxon.views.View;

public class DoxonUtils {
	
	/**
	 * 
	 * @param file
	 * @return String
	 */
	public static String createTestPath(TDFile file) {
		String[] parts = file.getAbsolutePath().split("\\\\");
		String newFile = "";

		for (int i = 0; i < parts.length - 1; i++) {
			if (parts[i].equals("main")) {
				newFile += "test\\";
			} else {
				newFile += parts[i] + "\\";
			}
		}
		return newFile;
	}

	/**
	 * Locates test folder
	 * @param filepath
	 * @return String
	 */
	public static String findTestFolder(String filepath) {
		String[] parts = filepath.split("\\\\");
		String newFilepath = "";

		for (String part : parts) {
			if (part.equals("main")) {
				newFilepath += "test";
				break;
			}
			newFilepath += part + "/";
		}

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
				View.currentTestFile = new TDFile(new File(newTestFilepath));
			}
			View.currentTestFile.setHeaderFilepath(View.currentOpenFile.getAbsolutePath());

			if (View.currentTestFile != null) {
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

}