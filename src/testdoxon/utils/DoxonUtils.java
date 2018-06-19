package testdoxon.utils;

import java.io.File;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;

import testdoxon.models.TDFile;
import testdoxon.views.View;

public class DoxonUtils {
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