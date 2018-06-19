/**
Copyright 2018 Delicate Sound Of Software AB, All Rights Reserved

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package testdoxon.views;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import testdoxon.exceptionHandler.TDException;
import testdoxon.handler.FileCrawlerHandler;
import testdoxon.handler.FileHandler;
import testdoxon.listener.HeaderToolTipListener;
import testdoxon.listener.OpenMethodAction;
import testdoxon.listener.TDPartListener;
import testdoxon.listener.UpdateOnFileChangedListener;
import testdoxon.listener.UpdateOnSaveListener;
import testdoxon.model.TDFile;

@SuppressWarnings("deprecation")
public class View extends ViewPart {
	public static final String ID = "testdoxon.views.View";

	private Color widgetColor;

	private TableViewer viewer;
	private Label header;

	private Action doubleClickAction;

	private ViewContentProvider viewContentProvider;

	private FileHandler fileHandler;
	private FileCrawlerHandler fileCrawlerHandler;

	public static TDFile currentOpenFile;
	public static TDFile currentTestFile;

	// Listeners
	private IResourceChangeListener saveFileListener;

	private ISelectionListener fileSelected;
	private IPartListener fileChanged;

	class ViewContentProvider implements IStructuredContentProvider {
		private TDFile file = null;

		/**
		 * Checks the current files name and package name
		 * 
		 * @param v
		 * @param oldInput
		 * @param newInput
		 */
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			if (newInput instanceof TDFile) {
				this.file = (TDFile) newInput;
			}
		}

		public void dispose() {
		}

		/**
		 * Updates the header with the current testfile name and package name. If file
		 * is not found return is "file not found"
		 * 
		 * @param parent
		 * @return String[]
		 */
		public Object[] getElements(Object parent) {
			if (this.file != null) {
				try {
					header.setText(this.file.getHeaderName());
					String[] retVal = fileHandler.getMethodsFromFile(this.file.getAbsolutePath());
					if(retVal != null && retVal.length > 0) {
						return retVal;
					}
				} catch (TDException e) {
					header.setText(e.getMessage());
				}
			}
			return new String[] {};
		}
	}

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_FORWARD_DISABLED);
		}
	}

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public View() {
		this.fileHandler = new FileHandler();
		this.fileCrawlerHandler = new FileCrawlerHandler();

		View.currentOpenFile = null;
		View.currentTestFile = null;

		this.viewContentProvider = new ViewContentProvider();
		this.widgetColor = new Color(null, 255, 255, 230);
	}

	private void initiateListeners() {
		this.saveFileListener = new UpdateOnSaveListener(this.viewer, this);
		this.fileSelected = new UpdateOnFileChangedListener(fileCrawlerHandler, viewer);
		this.fileChanged = new TDPartListener(this.viewer, this.fileCrawlerHandler);
		this.header.addListener(SWT.MouseHover, new HeaderToolTipListener(this.header));

		// Adding listeners
		ResourcesPlugin.getWorkspace().addResourceChangeListener(saveFileListener, IResourceChangeEvent.POST_BUILD);
		ISelectionService iSelectionService = this.getSite().getWorkbenchWindow().getSelectionService();
		iSelectionService.addPostSelectionListener(fileSelected);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(fileChanged);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		GridLayout gl = new GridLayout(1, false);
		gl.marginLeft = 0;
		gl.marginHeight = 10;
		gl.marginRight = 0;
		gl.marginBottom = 0;
		gl.verticalSpacing = 15;

		parent.setLayout(gl);
		parent.setBackground(widgetColor);

		this.populatePlugin(parent);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "TestDoxon.viewer");
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		this.initiateListeners();
	}

	private void populatePlugin(Composite parent) {
		header = new Label(parent, SWT.CENTER);
		header.setText("File not found.");
		header.setBackground(widgetColor);

		GridData gridDataLabel = new GridData(SWT.FILL, SWT.NONE, true, false);
		header.setLayoutData(gridDataLabel);
		
		Display display = Display.getCurrent();
		FontDescriptor fd = FontDescriptor.createFrom(header.getFont());
		Font font = fd.setStyle(SWT.BOLD).createFont(display);
		header.setFont(font);

		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(this.viewContentProvider);
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				viewer.setInput(getViewSite());
			}
		});

		viewer.getControl().setBackground(widgetColor);
		GridData gridDataTableView = new GridData(SWT.FILL, SWT.FILL, true, true);
		viewer.getControl().setLayoutData(gridDataTableView);
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				View.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
	}

	private void makeActions() {
		doubleClickAction = new OpenMethodAction(viewer, this, fileHandler);
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	@SuppressWarnings("unused")
	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "TestDoxon", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(@SuppressWarnings("rawtypes") Class arg0) {
		return null;
	}
	
}