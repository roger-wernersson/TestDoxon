package testdoxon.gui;

import javax.swing.*;
import java.awt.*;

public class MethodListItem extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        Font font = new Font("Dialog", Font.PLAIN, 12);
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        System.out.println("MethodListItem: " + value.toString());

        //label.setIcon
        //label.setHorizontalAlignment(JLabel.);
        label.setFont(font);
        return label;
        //return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }


}
