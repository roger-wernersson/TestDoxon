package testdoxon.listener;

import com.intellij.ui.components.JBList;
import testdoxon.gui.ClassComboBox;
import testdoxon.handler.FileCrawlerHandler;
import testdoxon.log.TDLog;
import testdoxon.model.TDFile;
import testdoxon.model.TestFile;
import testdoxon.utils.DoxonUtils;
import testdoxon.utils.TDStatics;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ComboBoxItemChanged implements ActionListener {
    private JTextArea header;
    private JBList testMethodList;
    private ClassComboBox testClassesComboBox;
    private FileCrawlerHandler fileCrawlerHandler;

    public ComboBoxItemChanged(JBList testMethodList, JTextArea header, FileCrawlerHandler fileCrawlerHandler, ClassComboBox testClassesComboBox) {
        this.testMethodList = testMethodList;
        this.header = header;
        this.fileCrawlerHandler = fileCrawlerHandler;
        this.testClassesComboBox = testClassesComboBox;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof ClassComboBox) {
            ClassComboBox testClassesComboBox = (ClassComboBox) e.getSource();
            if(testClassesComboBox.getSelectedItem() instanceof TestFile) {
                TestFile selected = (TestFile) testClassesComboBox.getSelectedItem();

                File file = new File(selected.getFilepath());
                if (!file.exists()) {
                    TDLog.info(selected.getFilename() + " Does not exist. Removing file from database.", TDLog.ERROR);
                    this.fileCrawlerHandler.removeEntry(selected);
                    this.testClassesComboBox.removeItem(selected);
                } else {
                    // Update JList with method from this test class
                    TDStatics.currentTestFile = new TDFile(new File(selected.getFilepath()));
                    DoxonUtils.setListItems(this.testMethodList, this.header);
                }
            }
        }
    }
}
