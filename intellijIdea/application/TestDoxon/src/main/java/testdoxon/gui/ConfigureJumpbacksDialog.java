package testdoxon.gui;

import com.intellij.ui.components.JBScrollPane;
import testdoxon.utils.DoxonUtils;
import testdoxon.utils.TDStatics;
import testdoxon.utils.TestDoxonPluginIcons;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ConfigureJumpbacksDialog extends JDialog implements ChangeListener {
    private final int height = 220;
    private final int width = 520;

    private String descText;
    private JTextArea description;

    public ConfigureJumpbacksDialog() {
        super();
        this.setSize(this.width, this.height);
        this.setResizable(false);
        this.setTitle("Configure jumpbacks...");
        this.setLocationRelativeTo(null);

        Container container = this.getContentPane();
        JPanel panel = new JPanel();

        JLabel header = new JLabel("Number of jumpbacks from src folder:");
        header.setFont(new Font("Dialog", Font.BOLD, 12));
        panel.add(header);

        SpinnerModel model = new SpinnerNumberModel(TDStatics.rootJumpbacks, 0, 10, 1);
        JSpinner jumpbacksSpinner = new JSpinner(model);
        jumpbacksSpinner.addChangeListener(this);
        panel.add(jumpbacksSpinner);

        String path = TDStatics.rootFolder;
        if (path == null || path.isEmpty()) {
            path = "Select a class to see root folder.";
        }

        descText = "Current root folder:\n" + path + "\n\nThis option will change the path from where TestDoxon finds test classes.\nDefault root folder is set to the src folder in the current project.\nIf you desire to include test classes from other bundles, change\nthe number of jumpbacks.";
        this.description = new JTextArea();
        this.description.setEnabled(false);
        this.description.setText(this.descText);
        this.description.setAutoscrolls(true);
        this.description.setBackground(UIManager.getColor("Panel.background"));

        JBScrollPane descScroll = new JBScrollPane();
        descScroll.setVisible(true);
        descScroll.createHorizontalScrollBar();
        descScroll.createVerticalScrollBar();
        descScroll.setViewportView(this.description);
        descScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        panel.add(descScroll);

        JLabel warning = new JLabel("!! WARNING - Software may become slow !!");
        warning.setForeground(new Color(255, 0, 0));
        panel.add(warning);

        container.add(panel);

    }

    private void saveProperty(int jumpbacks) {
        OutputStream out = null;

        try {
            out = new FileOutputStream(TDStatics.CONFIG_FILE);

            TDStatics.prop.setProperty("jumpback", Integer.toString(jumpbacks));
            TDStatics.prop.store(out, null);
        } catch (IOException e) {
            // Do nothing
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e1) {
                    // Do nothing
                }
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSpinner spinner = (JSpinner) e.getSource();

        TDStatics.rootJumpbacks = Integer.parseInt(spinner.getValue().toString());
        String newPath = DoxonUtils.findRootFolder(TDStatics.orgRootFolder);

        if (newPath == null || newPath.isEmpty()) {
            newPath = "Invalid number of jumpbacks.";
            TDStatics.rootFolder = newPath;
        } else {
            this.saveProperty(Integer.parseInt(spinner.getValue().toString()));
        }

        this.descText = "Current root folder:\n" + newPath + "\n\nThis option will change the path from where TestDoxon finds test classes.\nDefault root folder is set to the src folder in the current project.\nIf you desire to include test classes from other bundles, change\nthe number of jumpbacks.";
        this.description.setText(this.descText);
    }
}
