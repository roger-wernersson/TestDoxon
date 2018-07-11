package testdoxon.gui;

import testdoxon.handler.FileCrawlerHandler;
import testdoxon.utils.TDStatics;

import javax.swing.*;
import java.awt.*;

public class StatisticsDialog extends JDialog {
    final int WIDTH = 250;
    final int HEIGHT = 150;

    public StatisticsDialog (FileCrawlerHandler fileCrawlerHandler) {
        super();
        this.setSize(new Dimension(this.WIDTH, this.HEIGHT));
        this.setTitle("Statistics...");
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        Container container = this.getContentPane();

        Font font = new Font("Dialog", Font.BOLD, 12);
        Dimension leftDimension = new Dimension(200, 30);
        Dimension rightDimension = new Dimension(50, 30);

        JPanel con = new JPanel();
        con.setLayout(new GridBagLayout());

        GridBagConstraints a = new GridBagConstraints();
        a.gridy = 0;
        a.gridx = 0;

        JLabel label = new JLabel ("Test classes in memory");
        label.setBackground(new Color(0, 102, 153));
        label.setOpaque(true);
        label.setForeground(new Color(255, 255, 255));
        label.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
        label.setPreferredSize(leftDimension);
        label.setFont(font);

        GridBagConstraints b = new GridBagConstraints();
        b.gridy = 0;
        b.gridx = 1;

        JLabel labelTestClasses = new JLabel (Integer.toString(fileCrawlerHandler.getNrOfTestClasses()));
        labelTestClasses.setHorizontalAlignment(JLabel.CENTER);
        labelTestClasses.setPreferredSize(rightDimension);
        labelTestClasses.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 1;
        c.gridx = 0;

        JLabel label2 = new JLabel ("Prod. classes in memory");
        label2.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
        label2.setPreferredSize(leftDimension);
        label2.setBackground(new Color(0, 102, 153));
        label2.setOpaque(true);
        label2.setForeground(new Color(255, 255, 255));
        label2.setFont(font);

        GridBagConstraints d = new GridBagConstraints();
        d.gridy = 1;
        d.gridx = 1;

        JLabel labelProduction = new JLabel (Integer.toString(fileCrawlerHandler.getNrOfProdClasses()));
        labelProduction.setHorizontalAlignment(JLabel.CENTER);
        labelProduction.setPreferredSize(rightDimension);
        labelProduction.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));

        GridBagConstraints e = new GridBagConstraints();
        e.gridy = 2;
        e.gridx = 0;

        JLabel label3 = new JLabel ("Last lookup time (ms)");
        label3.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
        label3.setPreferredSize(leftDimension);
        label3.setBackground(new Color(0, 102, 153));
        label3.setOpaque(true);
        label3.setForeground(new Color(255, 255, 255));
        label3.setFont(font);

        GridBagConstraints f = new GridBagConstraints();
        f.gridy = 2;
        f.gridx = 1;

        JLabel labelFindTime = new JLabel (Long.toString(TDStatics.ms_recursiveRead));
        labelFindTime.setHorizontalAlignment(JLabel.CENTER);
        labelFindTime.setPreferredSize(rightDimension);
        labelFindTime.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));

        con.add(label, a);
        con.add(labelTestClasses, b);

        con.add(label2, c);
        con.add(labelProduction, d);

        con.add(label3, e);
        con.add(labelFindTime, f);

        container.add(con);
        this.pack();
    }


}
