package testdoxon.listener;

import testdoxon.gui.ConfigureJumpbacksDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfigureMenuButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        ConfigureJumpbacksDialog configDialog = new ConfigureJumpbacksDialog();
        configDialog.setVisible(true);
    }
}
