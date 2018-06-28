package testdoxon.views;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;
import testdoxon.gui.ClassComboBox;
import testdoxon.gui.MethodListItem;
import testdoxon.model.TDFile;

import javax.swing.*;
import java.awt.*;


public class TestDoxonToolWindowFactory implements com.intellij.openapi.wm.ToolWindowFactory {
    private JLabel header;
    private JComboBox testClassesComboBox;
    private JPanel content;
    private JBList testMethodList;

    private Color widgetColor;

    public TestDoxonToolWindowFactory() {
        this.widgetColor = new Color(255, 255, 230);
        this.initializeWidgets();
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
        //this.testMethodList.setListData(new String[] {"one", "two", "three"});
    }

    private void initializeWidgets() {
        this.content = new JPanel();
        this.content.setLayout(new BorderLayout());
        this.content.setBackground(this.widgetColor);

        JPanel top = new JPanel();
        top.setLayout(new GridLayout(2,1));
        top.setBackground(this.widgetColor);

        this.testClassesComboBox = new ClassComboBox();
        top.add(this.testClassesComboBox);

        this.header = new JBLabel("Open a class.");
        this.header.setHorizontalAlignment(SwingConstants.CENTER);
        this.header.setBounds(0,0, 100, 20);
        this.header.setFont(new Font("Dialog", Font.BOLD, 12));
        top.add(this.header);


        this.testMethodList = new JBList();
        this.testMethodList.setBackground(this.widgetColor);

        JBScrollPane scrollPane = new JBScrollPane();
        scrollPane.createVerticalScrollBar();
        scrollPane.createVerticalScrollBar();
        scrollPane.setBorder(null);
        scrollPane.setViewportView(this.testMethodList);

        content.add(top, BorderLayout.PAGE_START);
        content.add(scrollPane, BorderLayout.CENTER);
    }

}
