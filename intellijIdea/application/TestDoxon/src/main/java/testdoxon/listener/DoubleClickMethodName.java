package testdoxon.listener;

import testdoxon.model.TDTableItem;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DoubleClickMethodName extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() > 1) {
            JList methodList = (JList) e.getSource();
            Object selected = methodList.getSelectedValue();

            if(selected instanceof TDTableItem) {
                TDTableItem clicked = (TDTableItem) selected;
                System.out.println("DoubleClickMethodName - object: " + clicked.getMethodName());

                // Find row in test class and open that file with the correct row
            }
        }
    }

}
