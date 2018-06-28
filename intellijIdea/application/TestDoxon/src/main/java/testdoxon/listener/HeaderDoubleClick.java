package testdoxon.listener;

import testdoxon.model.TDTableItem;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HeaderDoubleClick extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getClickCount() > 1) {
            JLabel header = (JLabel) e.getSource();
            String text = header.getText();
            System.out.println("HeaderDoubleClick: " + text);
        }
    }
}
