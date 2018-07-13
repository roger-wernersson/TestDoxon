package testdoxon.listener;

import com.intellij.ui.components.JBList;
import testdoxon.model.TDFile;
import testdoxon.model.TestFile;
import testdoxon.utils.DoxonUtils;
import testdoxon.utils.TDStatics;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

public class ComboBoxItemChanged implements ItemListener {
    private JTextArea header;
    private JBList testMethodList;

    public ComboBoxItemChanged(JBList testMethodList, JTextArea header) {
        this.testMethodList = testMethodList;
        this.header = header;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object item = e.getItem();
        if (e.getStateChange() == 1) {
            if (item instanceof TestFile) {
                TestFile selected = (TestFile) item;

                // Update JList with method from this test class
                TDStatics.currentTestFile = new TDFile(new File(selected.getFilepath()));
                DoxonUtils.setListItems(this.testMethodList, this.header);
            }
        }
    }

}
