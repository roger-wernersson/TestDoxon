package testdoxon.listener;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileEditor.impl.FileEditorManagerImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import testdoxon.utils.TDStatics;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HeaderDoubleClick extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 1) {
            if (TDStatics.currentTestFile != null) {
                Project[] projects = ProjectManager.getInstance().getOpenProjects();
                Project currProject = projects[0];

                VirtualFile fileToOpen = LocalFileSystem.getInstance().findFileByIoFile(TDStatics.currentTestFile.getFile());
                FileEditorManagerImpl editorManager = (FileEditorManagerImpl) FileEditorManager.getInstance(currProject);
                OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(currProject, fileToOpen);
                editorManager.openEditor(openFileDescriptor, true);
            }
        }
    }
}
