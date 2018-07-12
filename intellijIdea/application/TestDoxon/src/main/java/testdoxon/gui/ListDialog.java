package testdoxon.gui;

import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import testdoxon.handler.FileCrawlerHandler;
import testdoxon.model.TestFile;

import javax.swing.*;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

public class ListDialog extends JDialog implements ActionListener {

    private int WIDTH = 600;
    private int HEIGHT = 400;
    private FileCrawlerHandler fileCrawlerHandler;
    private JLabel description;
    private JBTable classesTable;

    public ListDialog (FileCrawlerHandler fileCrawlerHandler) {
        super();
        this.fileCrawlerHandler = fileCrawlerHandler;

        this.setSize(new Dimension(this.WIDTH, this.HEIGHT));
        this.setTitle("List single files...");
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        Container container = this.getContentPane();

        JPanel con = new JPanel();
        con.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JRadioButton testClassRButton = new JRadioButton("Test classes");
        testClassRButton.setSelected(true);
        testClassRButton.addActionListener(this);

        JRadioButton prodClassRButton = new JRadioButton("Production classes");
        prodClassRButton.setSelected(false);
        prodClassRButton.addActionListener(this);

        ButtonGroup rButtonGroup = new ButtonGroup();
        rButtonGroup.add(testClassRButton);
        rButtonGroup.add(prodClassRButton);

        topPanel.add(testClassRButton);
        topPanel.add(prodClassRButton);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(2, 1));
        this.description = new JLabel("Description");
        this.description.setHorizontalAlignment(JLabel.CENTER);

        panel2.add(topPanel);
        panel2.add(this.description);

        con.add(panel2, BorderLayout.PAGE_START);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Filename");
        model.addColumn("Package");
        model.addColumn("Filepath");
        this.classesTable = new JBTable(model);
        this.classesTable.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));

        JBScrollPane listScroll = new JBScrollPane();
        listScroll.createVerticalScrollBar();
        listScroll.setViewportView(this.classesTable);

        con.add(listScroll, BorderLayout.CENTER);

        container.add(con);

        this.showAllTestClasses();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Test classes")) {
            this.showAllTestClasses();
        } else {
            this.showAllProdClasses();
        }
    }

    private void showAllTestClasses (){
        DefaultTableModel model = (DefaultTableModel) this.classesTable.getModel();
        model.setRowCount(0);

        TestFile[] testFiles = this.fileCrawlerHandler.getAllSingleTestClasses();
        if (testFiles != null && testFiles.length > 0) {
            for (TestFile testFile : testFiles) {
                model.addRow(new Object[]{testFile.getFilename(), testFile.getPackage(), testFile.getFilepath()});
            }
        }
        this.description.setText("(" + testFiles.length + ") Test classes without a corresponding production class");
    }

    private void showAllProdClasses (){
        System.out.println("Show alla PROD classes!");

        DefaultTableModel model = (DefaultTableModel) this.classesTable.getModel();
        model.setRowCount(0);

        TestFile[] prodFiles = this.fileCrawlerHandler.getAllSingleProdClasses();
        if (prodFiles != null && prodFiles.length > 0) {
            for (TestFile testFile : prodFiles) {
                model.addRow(new Object[]{testFile.getFilename(), testFile.getPackage(), testFile.getFilepath()});
            }
        }

        this.description.setText("(" + prodFiles.length + ") Production classes without a corresponding test class");
    }
}
