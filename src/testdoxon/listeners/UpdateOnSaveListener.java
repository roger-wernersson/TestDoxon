package testdoxon.listeners;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.ViewPart;

import testdoxon.models.TDFile;
import testdoxon.views.View;

public class UpdateOnSaveListener implements IResourceChangeListener {
	private TableViewer viewer;
	private ViewPart view;

	public UpdateOnSaveListener(TableViewer viewer, ViewPart view) {
		this.viewer = viewer;
		this.view = view;
	}

	@Override
	public void resourceChanged(IResourceChangeEvent arg0) {
		// TODO Auto-generated method stub
		IResourceDelta resourceDelta = arg0.getDelta();
		IResourceDeltaVisitor recourceDeltaVisitor = new IResourceDeltaVisitor() {
			boolean changed = false;

			@Override
			public boolean visit(IResourceDelta arg0) throws CoreException {
				IResource resource = arg0.getResource();
				if (resource.getType() == IResource.FILE && !changed) {
					if (arg0.getKind() == IResourceDelta.CHANGED) {
						updateTable();
						changed = true;
					}
				}
				return true;
			}
		};

		try {
			resourceDelta.accept(recourceDeltaVisitor);
		} catch (CoreException e) {
			System.out.println(e.getMessage());
		}
	}

	private void updateTable() {
		IEditorPart iEditorPart = view.getSite().getWorkbenchWindow().getActivePage().getActivePart().getSite()
				.getPage().getActiveEditor();

		if (iEditorPart != null) {
			File file = iEditorPart.getEditorInput().getAdapter(File.class);
			if (file != null) {
				// Always update on save
				View.currentOpenFile = new TDFile(file);
				View.currentTestFile = View.currentOpenFile;
				View.currentTestFile.setHeaderFilepath(View.currentOpenFile.getAbsolutePath());

				if (View.currentTestFile != null && this.viewer != null) {
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							try {
								viewer.setInput(View.currentTestFile);
							} catch (AssertionFailedException e) {
								// Do nothing
							}
						}
					});
				}
			}
		}
	}

}
