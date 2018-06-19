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

import testdoxon.exceptionHandlers.TDException;
import testdoxon.handlers.FileCrawlerHandler;
import testdoxon.handlers.FileHandler;
import testdoxon.listeners.HeaderToolTipListener;
import testdoxon.listeners.OpenMethodAction;
import testdoxon.listeners.TDPartListener;
import testdoxon.listeners.UpdateOnFileChangedListener;
import testdoxon.listeners.UpdateOnSaveListener;
import testdoxon.models.TDFile;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

@SuppressWarnings("deprecation")
public class View extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
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
	// private CaretListener wordListener;

	private ISelectionListener fileSelected;
	private IPartListener fileChanged;

	/*
	 * The content provider class is responsible for providing objects to the view.
	 * It can wrap existing objects in adapters or simply return objects as-is.
	 * These objects may be sensitive to the current input of the view, or ignore it
	 * and always show the same content (like Task List, for example).
	 */

	class ViewContentProvider implements IStructuredContentProvider {
		// private String filePath = "";
		// private String fileName = "";

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
					return fileHandler.getMethodsFromFile(this.file.getAbsolutePath());
				} catch (TDException e) {
					header.setText(e.getMessage());
					return new String[] {};
				}
			} else {
				return new String[] {};
			}
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
		// Other plug-ins can contribute there actions here
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