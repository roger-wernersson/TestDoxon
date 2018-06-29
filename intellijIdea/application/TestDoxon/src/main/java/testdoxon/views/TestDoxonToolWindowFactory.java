package testdoxon.views;

import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;
import testdoxon.gui.ClassComboBox;
import testdoxon.gui.MethodListItem;
import testdoxon.handler.FileCrawlerHandler;
import testdoxon.handler.FileHandler;
import testdoxon.listener.*;
import testdoxon.model.TDFile;
import testdoxon.model.TestFile;
import testdoxon.utils.DoxonUtils;
import testdoxon.utils.TDStatics;
import testdoxon.utils.TestDoxonPluginIcons;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class TestDoxonToolWindowFactory implements com.intellij.openapi.wm.ToolWindowFactory {
    private JLabel header;
    private ClassComboBox testClassesComboBox;
    private JPanel content;
    private JBList testMethodList;

    private FileHandler fileHandler;
    private FileCrawlerHandler fileCrawlerHandler;

    private Color widgetColor;

    public TestDoxonToolWindowFactory() {
        this.fileHandler = new FileHandler();
        this.fileCrawlerHandler = new FileCrawlerHandler();

        this.widgetColor = new Color(255, 255, 230);
        this.initializeWidgets();
        this.createListeners();
        this.loadProperties();
        this.setupPlugin();
    }

    public void createToolWindowContent(@NotNull Project project, @NotNull com.intellij.openapi.wm.ToolWindow toolWindow) {
        ContentManager contentManager = toolWindow.getContentManager();
        Content content = contentManager.getFactory().createContent(this.content, "", true);
        contentManager.addContent(content);
    }

    private void loadProperties() {
        InputStream input = null;
        try {
            input = new FileInputStream(TDStatics.CONFIG_FILE);
            TDStatics.prop.load(input);
            TDStatics.rootJumpbacks = Integer.parseInt(TDStatics.prop.getProperty("jumpback"));
        } catch (IOException e) {
            // Do nothing
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    // Do nothing
                }
            }
        }
    }

    private void initializeWidgets() {
        this.content = new JPanel();
        this.content.setLayout(new BorderLayout());
        this.content.setBackground(this.widgetColor);

        JPanel top = new JPanel();
        top.setLayout(new GridLayout(3, 1));
        top.setBackground(this.widgetColor);

        JMenuBar menu = new JMenuBar();
        JMenuItem configure = new JMenuItem();
        configure.setIcon(TestDoxonPluginIcons.LOGO);
        configure.addActionListener(new ConfigureMenuButtonListener());
        menu.add(configure);
        top.add(menu);

        this.testClassesComboBox = new ClassComboBox();
        top.add(this.testClassesComboBox);

        this.header = new JBLabel("Selected a class");
        this.header.setHorizontalAlignment(SwingConstants.CENTER);
        this.header.setForeground(new Color(0, 0, 0));
        this.header.setFont(new Font("Dialog", Font.BOLD, 12));
        top.add(this.header);

        this.testMethodList = new JBList();
        this.testMethodList.setBackground(this.widgetColor);
        this.testMethodList.setCellRenderer(new MethodListItem());

        JBScrollPane scrollPane = new JBScrollPane();
        scrollPane.createVerticalScrollBar();
        scrollPane.createVerticalScrollBar();
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        scrollPane.setViewportView(this.testMethodList);

        content.add(top, BorderLayout.PAGE_START);
        content.add(scrollPane, BorderLayout.CENTER);
    }

    private void createListeners() {
        this.testMethodList.addMouseListener(new DoubleClickMethodName());
        this.testClassesComboBox.addItemListener(new ComboBoxItemChanged(this.testMethodList, this.header));
        this.header.addMouseListener(new HeaderDoubleClick());

        FileSavedListener fileSavedListener = new FileSavedListener(this.testMethodList, this.header);
        fileSavedListener.startToListen();

        EditorFileChangedListener editorFileChangedListener = new EditorFileChangedListener(this.fileCrawlerHandler, this.testClassesComboBox, this.testMethodList, this.header);
        editorFileChangedListener.startToListen();

        EditorFactory.getInstance().getEventMulticaster().addCaretListener(new CaretMovedListener(this.fileCrawlerHandler, this.header, this.testMethodList, this.testClassesComboBox));
    }

    private void setupPlugin() {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        if (projects != null) {
            VirtualFile[] files = FileEditorManager.getInstance(projects[0]).getSelectedFiles();

            if (files != null && files.length > 0) {
                File currentFile = new File(files[0].getPath());
                TDStatics.currentOpenFile = new TDFile(new File(currentFile.getPath()));

                // Read in all test classes
                String rootFolder = DoxonUtils.findRootFolder(currentFile.getPath());
                if (rootFolder != null) {
                    this.fileCrawlerHandler.getAllTestClasses(rootFolder);

                    // Update combobox
                    TestFile[] classes = fileCrawlerHandler.getAllTestClassesAsTestFileArray();
                    DoxonUtils.setComboBoxItems(this.testClassesComboBox, classes);
                }

                // Update method list
                DoxonUtils.findFileToOpen(this.testMethodList, this.header);
            }
        }
    }
}
