package testdoxon.listener;

import testdoxon.gui.StatisticsDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatisticMenuButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        StatisticsDialog statisticDialog = new StatisticsDialog();
        statisticDialog.setVisible(true);
    }

}
