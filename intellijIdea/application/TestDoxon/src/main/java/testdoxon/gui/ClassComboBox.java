package testdoxon.gui;

import testdoxon.model.TestFile;

import javax.swing.*;

public class ClassComboBox extends JComboBox {

    public void addItems(TestFile[] items) {
        for(TestFile testFile : items) {
            super.addItem(testFile.getPackage());
        }
    }
}
