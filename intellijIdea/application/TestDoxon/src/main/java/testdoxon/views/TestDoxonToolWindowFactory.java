package testdoxon.views;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;
import testdoxon.gui.ClassComboBox;
import testdoxon.gui.MethodListItem;
import testdoxon.listener.ComboBoxItemChanged;
import testdoxon.listener.DoubleClickMethodName;
import testdoxon.listener.HeaderDoubleClick;
import testdoxon.model.TDTableItem;
import testdoxon.model.TestFile;

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
        this.createListeners();
    }

    public void createToolWindowContent(@NotNull Project project, @NotNull com.intellij.openapi.wm.ToolWindow toolWindow) {
        ContentManager contentManager = toolWindow.getContentManager();
        Content content = contentManager.getFactory().createContent(this.content, "", true);
        contentManager.addContent(content);
    }

    private void addDummyData() {

        TestFile testFile = new TestFile("FileHandler.java", "C:/hej");
        TestFile testFile1 = new TestFile("FileRepository.java", "C:/hej");
        this.testClassesComboBox.addItem(testFile);
        this.testClassesComboBox.addItem(testFile1);


        TDTableItem item = new TDTableItem("Test", true, true);
        this.testMethodList.setListData(new TDTableItem[] {item});
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
        this.testMethodList.setCellRenderer(new MethodListItem());

        JBScrollPane scrollPane = new JBScrollPane();
        scrollPane.createVerticalScrollBar();
        scrollPane.createVerticalScrollBar();
        scrollPane.setBorder(null);
        scrollPane.setViewportView(this.testMethodList);

        content.add(top, BorderLayout.PAGE_START);
        content.add(scrollPane, BorderLayout.CENTER);
    }

    private void createListeners() {
        this.testMethodList.addMouseListener(new DoubleClickMethodName());
        this.testClassesComboBox.addItemListener(new ComboBoxItemChanged());
        this.header.addMouseListener(new HeaderDoubleClick());
    }

}
