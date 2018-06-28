package testdoxon.listener;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;
import testdoxon.handler.FileCrawlerHandler;

public class EditorFileChangedListener implements ApplicationComponent, FileEditorManagerListener {
    private final MessageBusConnection messageBusConnection;
    private FileCrawlerHandler fileCrawlerHandler;

    public EditorFileChangedListener (FileCrawlerHandler fileCrawlerHandler) {
        this.fileCrawlerHandler = fileCrawlerHandler;
        this.messageBusConnection = ApplicationManager.getApplication().getMessageBus().connect();
    }

    public void startToListen() {
        this.messageBusConnection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, this);
    }

    @Override
    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
        VirtualFile file = event.getNewFile();
        System.out.println(file.getName() + " - " + file.getCanonicalPath());
    }


}
