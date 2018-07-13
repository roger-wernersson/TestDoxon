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
            //filename.setMargin(new Insets(10,20,10,20));
            filename.setMaximumSize(new Dimension(300, 20));
            filename.setMinimumSize(new Dimension(600, 600));

            /*GridBagConstraints a = new GridBagConstraints();
            a.gridx = 0;
            a.gridy = 0;
            panel.add(filename, BorderLayout.LINE_START);*/

            JTextArea classPackage = new JTextArea(testFile.getPackage());
            classPackage.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
            //classPackage.setMargin(new Insets(10,20,10,20));
            classPackage.setMaximumSize(new Dimension(300, 20));
            classPackage.setMinimumSize(new Dimension(600, 600));

            /*GridBagConstraints b = new GridBagConstraints();
            b.gridx = 1;
            b.gridy = 0;
            panel.add(classPackage, BorderLayout.CENTER);*/

            JTextArea filePath = new JTextArea(testFile.getFilepath());
            filePath.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
            //filePath.setMargin(new Insets(10,20,10,20));
            filePath.setMaximumSize(new Dimension(300, 20));
            filePath.setMinimumSize(new Dimension(600, 600));

            /*GridBagConstraints c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 0;
            panel.add(filePath, BorderLayout.LINE_END);*/

            //panel.setPreferredSize(new Dimension(600, 20));

            panel.updateUI();

            return panel;

        }

        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
}
