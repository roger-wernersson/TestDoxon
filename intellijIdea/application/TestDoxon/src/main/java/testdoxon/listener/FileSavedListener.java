package testdoxon.listener;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.util.messages.MessageBusConnection;
import com.sun.jna.platform.FileMonitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FileSavedListener implements ApplicationComponent, BulkFileListener {

    private final MessageBusConnection messageBusConnection;

    public FileSavedListener() {
        this.messageBusConnection = ApplicationManager.getApplication().getMessageBus().connect();
    }

    public void startToListen() {
        this.messageBusConnection.subscribe(VirtualFileManager.VFS_CHANGES, this);
    }

    @Override
    public void after(@NotNull List<? extends VFileEvent> events) {

        for(VFileEvent event : events) {
            System.out.println(event.toString());
        }

        //System.out.println(file.getName() + " - " + file.getCanonicalPath());
    }
}
