package testdoxon.views;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.intellij.util.ui.GridBag;
import groovy.swing.factory.LayoutFactory;
import org.jetbrains.annotations.NotNull;
import testdoxon.gui.MethodListItem;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.Collections;

public class TestDoxonToolWindowFactory implements com.intellij.openapi.wm.ToolWindowFactory {
    private JLabel header;
    private JComboBox testClassesComboBox;
    private JPanel content;
    private JBList testMethodList;

    private Color widgetColor;

    public TestDoxonToolWindowFactory() {
        this.widgetColor = new Color(255, 255, 230);

        this.content = new JPanel();
        this.content.setLayout(new BorderLayout());
        this.content.setBackground(this.widgetColor);

        JPanel top = new JPanel();
        top.setLayout(new GridLayout(2,1));
        top.setBackground(this.widgetColor);

        this.testClassesComboBox = new ComboBox();
        top.add(this.testClassesComboBox);

        this.header = new JBLabel("Open a class.");
        this.header.setHorizontalAlignment(SwingConstants.CENTER);
        this.header.setBounds(0,0, 100, 20);
        this.header.setFont(new Font("Dialog", Font.BOLD, 12));
        top.add(this.header);

        this.testMethodList = new JBList();
        this.testMethodList.setBackground(this.widgetColor);

        content.add(top, BorderLayout.PAGE_START);
        content.add(this.testMethodList, BorderLayout.CENTER);

        this.addDummyData();

    }

    public void createToolWindowContent(@NotNull Project project, @NotNull com.intellij.openapi.wm.ToolWindow toolWindow) {
        ContentManager contentManager = toolWindow.getContentManager();
        Content content = contentManager.getFactory().createContent(this.content, "", true);
        contentManager.addContent(content);
    }

    private void addDummyData() {

        this.testClassesComboBox.addItem("one");
        this.testClassesComboBox.addItem("two");
        this.testClassesComboBox.addItem("three");

        this.testMethodList.setCellRenderer(new MethodListItem());
        this.testMethodList.setListData(new String[] {"one", "two", "three"});
    }

}
