package testdoxon.gui;

import testdoxon.model.TDTableItem;
import testdoxon.utils.TestDoxonPluginIcons;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MethodListItem extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof TDTableItem) {
            TDTableItem item = (TDTableItem) value;
            Font font = new Font("Dialog", Font.PLAIN, 12);
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            label.setText(item.getMethodName());
            switch (item.getPictureIndex()) {
                case TDTableItem.MISSING_TEST_IN_NAME:
                    label.setIcon(TestDoxonPluginIcons.blueDot);
                    break;
                case TDTableItem.NONE:
                    label.setIcon(TestDoxonPluginIcons.redDot);
                    break;
                case TDTableItem.TEST:
                    label.setIcon(TestDoxonPluginIcons.greenDot);
                    break;
                case TDTableItem.IGNORE:
                    label.setIcon(TestDoxonPluginIcons.grayDot);
                    break;
                case TDTableItem.BOTH_TEST_IGNORE:
                    label.setIcon(TestDoxonPluginIcons.yellowDot);
                    break;
                default:
                    label.setIcon(TestDoxonPluginIcons.blueDot);
            }

            label.setBorder(new EmptyBorder(0, 15, 0, 0));
            label.setFont(font);
            label.setForeground(new Color(0, 0, 0));
            return label;
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }

}
