package testdoxon.listener;


import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.ui.components.JBList;
import testdoxon.gui.ClassComboBox;
import testdoxon.handler.FileCrawlerHandler;
import testdoxon.model.TDFile;
import testdoxon.model.TestFile;
import testdoxon.utils.DoxonUtils;
import testdoxon.utils.TDStatics;

import javax.swing.*;
import java.io.File;

public class CaretMovedListener implements CaretListener {

    private FileCrawlerHandler fileCrawlerHandler;
    private JLabel header;
    private JBList testMethodList;
    private ClassComboBox testClassesComboBox;

    public CaretMovedListener (FileCrawlerHandler fileCrawlerHandler, JLabel header, JBList testMethodList, ClassComboBox testClassesComboBox) {
        this.fileCrawlerHandler = fileCrawlerHandler;
        this.header = header;
        this.testMethodList = testMethodList;
        this.testClassesComboBox = testClassesComboBox;
    }

    @Override
    public void caretPositionChanged(CaretEvent e) {
        e.getCaret().selectWordAtCaret(true);
        String word = e.getCaret().getSelectedText();

        if (word.length() > 0 && Character.isUpperCase(word.charAt(0))) {
            String fileToLookFor = "Test" + word + ".java";

            if (TDStatics.currentOpenFile != null) {
                // Check current open file
                // HERE -->

                String newTestFilepath = fileCrawlerHandler.getTestFilepathFromFilename(fileToLookFor,
                        TDStatics.currentOpenFile.getAbsolutePath(), TDStatics.currentOpenFile.getName(),
                        this.testClassesComboBox);

                if (newTestFilepath != null && !newTestFilepath.equals(TDStatics.currentTestFile.getAbsolutePath())) {
                    TDStatics.currentTestFile = new TDFile(new File(newTestFilepath));
                    TDStatics.currentTestFile.setHeaderFilepath(TDStatics.currentTestFile.getAbsolutePath());

                    if (TDStatics.currentTestFile != null) {
                        DoxonUtils.setListItems(this.testMethodList, this.header);
                    }
                }
            }
        } else {
            if(TDStatics.currentTestFile != null && TDStatics.currentOpenFile != null) {
                if (!TDStatics.currentTestFile.getPath().equals(TDStatics.currentOpenFile.getPath())) {

                    // Show current class test class.
                    DoxonUtils.findFileToOpen(this.testMethodList, this.header);

                    // Update combo viewer to show all test classes
                    TestFile[] classes = this.fileCrawlerHandler.getAllTestClassesAsTestFileArray();
                    DoxonUtils.setComboBoxItems(this.testClassesComboBox, classes);
                }
            }
        }

    }
}
