package testdoxon.gui;

import testdoxon.model.TestFile;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class SingleClassListItem extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof TestFile) {
            TestFile testFile = (TestFile) value;
            JPanel panel = new JPanel ();
            panel.setLayout(new BorderLayout());

            JTextArea filename = new JTextArea(testFile.getFilename());
            filename.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
            filename.setMaximumSize(new Dimension(300, 20));
            filename.setMinimumSize(new Dimension(600, 600));

            JTextArea classPackage = new JTextArea(testFile.getPackage());
            classPackage.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
            classPackage.setMaximumSize(new Dimension(300, 20));
            classPackage.setMinimumSize(new Dimension(600, 600));

            JTextArea filePath = new JTextArea(testFile.getFilepath());
            filePath.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
            filePath.setMaximumSize(new Dimension(300, 20));
            filePath.setMinimumSize(new Dimension(600, 600));

            panel.updateUI();

            return panel;

        }

        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
