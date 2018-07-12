package testdoxon.listener;

import testdoxon.gui.ListDialog;
import testdoxon.handler.FileCrawlerHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListMenuButtonListener implements ActionListener {

    private FileCrawlerHandler fileCrawlerHandler;

    public ListMenuButtonListener(FileCrawlerHandler fileCrawlerHandler) {
        this.fileCrawlerHandler =fileCrawlerHandler;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ListDialog listDialog = new ListDialog(this.fileCrawlerHandler);
        listDialog.setVisible(true);
    }
}
