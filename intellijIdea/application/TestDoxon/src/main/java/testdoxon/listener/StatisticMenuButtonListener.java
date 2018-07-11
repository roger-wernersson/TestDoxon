package testdoxon.listener;

import testdoxon.gui.StatisticsDialog;
import testdoxon.handler.FileCrawlerHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class StatisticMenuButtonListener implements ActionListener {

    private FileCrawlerHandler fileCrawlerHandler;

    public StatisticMenuButtonListener (FileCrawlerHandler fileCrawlerHandler) {
        this.fileCrawlerHandler = fileCrawlerHandler;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        StatisticsDialog statisticDialog = new StatisticsDialog(fileCrawlerHandler);
        statisticDialog.setVisible(true);
    }

}
