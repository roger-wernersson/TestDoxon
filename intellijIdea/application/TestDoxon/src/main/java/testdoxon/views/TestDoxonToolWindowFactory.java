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
import testdoxon.gui.ConfigureJumpbacksDialog;
import testdoxon.gui.MethodListItem;
import testdoxon.handler.FileCrawlerHandler;
import testdoxon.handler.FileHandler;
import testdoxon.listener.*;
import testdoxon.log.TDLog;
import testdoxon.model.TDFile;
import testdoxon.model.TestFile;
import testdoxon.utils.DoxonUtils;
import testdoxon.utils.TDStatics;
import testdoxon.utils.TestDoxonPluginIcons;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class TestDoxonToolWindowFactory implements com.intellij.openapi.wm.ToolWindowFactory {
    private JTextArea header;
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

        TDLog.info("Log file: " + System.getProperty("user.dir"), TDLog.INFORMATION);
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
            TDLog.info(e.getMessage(), TDLog.ERROR);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    TDLog.info(e.getMessage(), TDLog.ERROR);
                }
            }
        }
    }

    private void initializeWidgets() {
        this.content = new JPanel();
        this.content.setLayout(new BorderLayout());
        this.content.setBackground(this.widgetColor);

        JPanel topNorth = new JPanel();
        topNorth.setLayout(new GridLayout(2, 1));
        topNorth.setBackground(this.widgetColor);

        JMenuBar menu = new JMenuBar();

        // Icon made by <a href="https://www.flaticon.com/authors/gregor-cresnar">Gregor Cresnar</a> from www.flaticon.com
        // Configure button
        JButton configure = new JButton();
        configure.setIcon(TestDoxonPluginIcons.COG);
        configure.setMaximumSize(new Dimension(30, 50));
        configure.addActionListener(new ConfigureMenuButtonListener());
        menu.add(configure);

        // Icon made by <a href="https://www.flaticon.com/authors/freepik">Freepik</a> from www.flaticon.com
        // Statistics button
        JButton statistic = new JButton();
        statistic.setIcon(TestDoxonPluginIcons.STAT);
        statistic.setMaximumSize(new Dimension(30, 50));
        statistic.addActionListener(new ConfigureMenuButtonListener());
        menu.add(statistic);

        topNorth.add(menu);

        this.testClassesComboBox = new ClassComboBox();
        topNorth.add(this.testClassesComboBox);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(this.widgetColor);

        topPanel.add(topNorth, BorderLayout.PAGE_START);

        this.header = new JTextArea("Selected a class");
        //this.header.setHorizontalAlignment(SwingConstants.CENTER);
        this.header.setLineWrap(true);
        this.header.setWrapStyleWord(true);
        this.header.setBackground(this.widgetColor);
        this.header.setForeground(new Color(0, 0, 0));
        this.header.setMargin(new Insets(5,10,5,10));
        this.header.setEditable(false);
        this.header.setFont(new Font("Dialog", Font.BOLD, 12));
        topPanel.add(this.header, BorderLayout.CENTER);

        this.testMethodList = new JBList();
        this.testMethodList.setBackground(this.widgetColor);
        this.testMethodList.setCellRenderer(new MethodListItem());

        JBScrollPane scrollPane = new JBScrollPane();
        scrollPane.createVerticalScrollBar();
        scrollPane.createVerticalScrollBar();
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        scrollPane.setViewportView(this.testMethodList);

        content.add(topPanel, BorderLayout.PAGE_START);
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
                    this.fileCrawlerHandler.getAllTestClasses(rootFolder, this.testClassesComboBox);

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
