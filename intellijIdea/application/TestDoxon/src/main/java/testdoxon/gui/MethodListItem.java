package testdoxon.gui;

import testdoxon.utils.TestDoxonPluginIcons;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MethodListItem extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Font font = new Font("Dialog", Font.PLAIN, 12);
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setIcon(TestDoxonPluginIcons.blueDot);
        label.setBorder(new EmptyBorder(0, 15, 0, 0));

        //System.out.println("MethodListItem: " + value.toString());

        label.setFont(font);
        return label;
    }


}
