package testdoxon.listener;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import testdoxon.exceptionHandler.TDException;
import testdoxon.handler.FileHandler;
import testdoxon.log.TDLog;
import testdoxon.model.TDTableItem;
import testdoxon.utils.TDStatics;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DoubleClickMethodName extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 1) {
            JList methodList = (JList) e.getSource();
            Object selected = methodList.getSelectedValue();

            if (selected instanceof TDTableItem) {
                TDTableItem clicked = (TDTableItem) selected;

                if (TDStatics.currentTestFile != null) {
                    FileHandler fileHandler = new FileHandler();
                    try {
                        Project[] projects = ProjectManager.getInstance().getOpenProjects();
                        Project currProject = projects[0];

                        int lineNumber = fileHandler.getLineNumberOfSpecificMethod(TDStatics.currentTestFile.getAbsolutePath(), clicked.getMethodName());

                        VirtualFile fileToOpen = LocalFileSystem.getInstance().findFileByIoFile(TDStatics.currentTestFile.getFile());
                        FileEditorManagerImpl editorManager = (FileEditorManagerImpl) FileEditorManager.getInstance(currProject);
                        OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(currProject, fileToOpen, lineNumber - 1, 0);
                        editorManager.openEditor(openFileDescriptor, true);

                    } catch (TDException e1) {
                        TDLog.info(e1.getMessage(), TDLog.ERROR);
                    }

                }
            }
        }
    }

}
