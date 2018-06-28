package testdoxon.listener;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;
import testdoxon.handler.FileCrawlerHandler;
import testdoxon.model.TDFile;
import testdoxon.model.TestFile;
import testdoxon.utils.DoxonUtils;
import testdoxon.utils.TDStatics;
import testdoxon.views.TestDoxonToolWindowFactory;

import javax.swing.*;
import java.io.File;

public class EditorFileChangedListener implements ApplicationComponent, FileEditorManagerListener {
    private final MessageBusConnection messageBusConnection;
    private FileCrawlerHandler fileCrawlerHandler;
    private JBList testMethodList;
    private JLabel header;

    private String lastUpdatedPath;

    private JComboBox testClassesComboBox;
    public EditorFileChangedListener (FileCrawlerHandler fileCrawlerHandler, JComboBox testClassesComboBox, JBList testMethodList, JLabel header) {

        this.testMethodList = testMethodList;
        this.header = header;
        this.testClassesComboBox = testClassesComboBox;
        this.fileCrawlerHandler = fileCrawlerHandler;
        this.messageBusConnection = ApplicationManager.getApplication().getMessageBus().connect();
    }

    public void startToListen() {
        this.messageBusConnection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, this);
    }

    @Override
    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
        VirtualFile vFile = event.getNewFile();
        //this.testClassesComboBox.addItem(file);
        if (vFile.getName().matches(".*\\.java")) {
            File file = new File(vFile.getPath());

            if (TDStatics.currentOpenFile == null || !file.getName().equals(TDStatics.currentOpenFile.getName())) {

                TDStatics.currentOpenFile = new TDFile(file);

                String rootFolder = DoxonUtils.findRootFolder(file.getAbsolutePath());
                boolean updated = false;
                if (this.lastUpdatedPath == null || (rootFolder != null && !this.lastUpdatedPath.equals(rootFolder))) {
                    this.lastUpdatedPath = rootFolder;
                    this.fileCrawlerHandler.getAllTestClasses(rootFolder);
                    updated = true;
                }

                if (file.getName().matches("^Test.*") && !this.fileCrawlerHandler.listContains(file.getAbsolutePath()) && !updated) {
                    this.fileCrawlerHandler.addToList(new TestFile(file.getName(), file.getAbsolutePath()));
                }
                DoxonUtils.setComboBoxItems(this.testClassesComboBox, this.fileCrawlerHandler.getAllTestClassesAsTestFileArray());

                DoxonUtils.findFileToOpen(this.testMethodList, this.header);
            }
        }
    }
}
