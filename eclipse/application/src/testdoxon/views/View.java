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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;

import testdoxon.gui.ClassPathsComboContentProvider;
import testdoxon.gui.ConfJumpbackDialog;
import testdoxon.gui.ListDialog;
import testdoxon.gui.MethodLabelProvider;
import testdoxon.gui.MethodTableContentProvider;
import testdoxon.gui.SortByNameSorter;
import testdoxon.gui.StatisticDialog;
import testdoxon.handler.FileCrawlerHandler;
import testdoxon.handler.FileHandler;
import testdoxon.listener.HeaderToolTipListener;
import testdoxon.listener.OpenClassAction;
import testdoxon.listener.OpenMethodAction;
import testdoxon.listener.TDPartListener;
import testdoxon.listener.UpdateOnFileChangedListener;
import testdoxon.listener.UpdateOnSaveListener;
import testdoxon.log.TDLog;
import testdoxon.util.TDGlobals;

public class View extends ViewPart {
	public static final String ID = "testdoxon.views.View";

	private Color widgetColor;

	private TableViewer viewer;
	private Text header;
	private ComboViewer testClassPaths;

	private Action dblClickTableViewer;
	private Action selectionChangedComboViewer;
	private Action configureRootFolder;
	private Action showStatistics;
	private Action showListDialog;
	private Action sortButton;

	
	private MethodTableContentProvider contentProvider;

	private FileHandler fileHandler;
	private FileCrawlerHandler fileCrawlerHandler;

	// Listeners
	private IResourceChangeListener saveFileListener;
	private ISelectionListener fileSelected;
	private IPartListener fileChanged;

	/**
	 * The constructor.
	 */
	public View() {
		this.fileHandler = new FileHandler();
		this.fileCrawlerHandler = new FileCrawlerHandler();
		this.contentProvider = null;

		TDGlobals.currentOpenFile = null;
		TDGlobals.currentTestFile = null;
		TDGlobals.prop = new Properties();
		this.loadImages();
		this.loadProperties();
		
		TDLog.info("Log file: " + System.getProperty("user.dir"), TDLog.INFORMATION);

		this.widgetColor = new Color(null, 255, 255, 230);
	}
	
	private void loadImages() {
		Bundle bundle = Platform.getBundle("TestDoxon");
		URL url = null;
		ImageDescriptor imageDesc = null;
		
		url = FileLocator.find(bundle, new Path("icons/testDox-red8px.png"), null);
		imageDesc = ImageDescriptor.createFromURL(url);
		TDGlobals.redDot = imageDesc.createImage();
		
		url = FileLocator.find(bundle, new Path("icons/testDox-green8px.png"), null);
		imageDesc = ImageDescriptor.createFromURL(url);
		TDGlobals.greenDot = imageDesc.createImage();
		
		url = FileLocator.find(bundle, new Path("icons/testDox-gray8px.png"), null);
		imageDesc = ImageDescriptor.createFromURL(url);
		TDGlobals.grayDot = imageDesc.createImage();
		
		url = FileLocator.find(bundle, new Path("icons/testDox-blue8px.png"), null);
		imageDesc = ImageDescriptor.createFromURL(url);
		TDGlobals.blueDot = imageDesc.createImage();
		
		url = FileLocator.find(bundle, new Path("icons/testDox-yellow8px.png"), null);
		imageDesc = ImageDescriptor.createFromURL(url);
		TDGlobals.yellowDot = imageDesc.createImage();
		
		url = FileLocator.find(bundle, new Path("icons/cog.png"), null);
		TDGlobals.cog = ImageDescriptor.createFromURL(url);
		
		url = FileLocator.find(bundle, new Path("icons/stat.png"), null);
		TDGlobals.stat = ImageDescriptor.createFromURL(url);
		
		url = FileLocator.find(bundle, new Path("icons/list.png"), null);
		TDGlobals.list = ImageDescriptor.createFromURL(url);
		
		url = FileLocator.find(bundle, new Path("icons/sort.png"), null);
		TDGlobals.sort = ImageDescriptor.createFromURL(url);
		
		url = FileLocator.find(bundle, new Path("icons/sortDisabled.png"), null);
		TDGlobals.sortDisabled = ImageDescriptor.createFromURL(url);
		
	}

	private void loadProperties() {
		InputStream input = null;
		try {
			input = new FileInputStream(TDGlobals.CONFIG_FILE);
			TDGlobals.prop.load(input);
			TDGlobals.rootJumpbacks = Integer.parseInt(TDGlobals.prop.getProperty("jumpback"));
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

	private void initiateListeners() {
		this.saveFileListener = new UpdateOnSaveListener(this.viewer, this);
		this.fileSelected = new UpdateOnFileChangedListener(fileCrawlerHandler, viewer, testClassPaths);
		this.fileChanged = new TDPartListener(this.viewer, this.fileCrawlerHandler, this.testClassPaths);
		this.header.addListener(SWT.MouseHover, new HeaderToolTipListener(this.header, this));

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
		hookActions();
		contributeToActionBars();
		this.initiateListeners();
	}

	private void populatePlugin(Composite parent) {
		this.testClassPaths = new ComboViewer(parent, SWT.NONE);
		GridData gridDataComboBox = new GridData(SWT.FILL, SWT.NONE, true, false);
		this.testClassPaths.getControl().setLayoutData(gridDataComboBox);
		this.testClassPaths.setContentProvider(new ClassPathsComboContentProvider());

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					testClassPaths.setInput(getViewSite());
				} catch (AssertionFailedException e) {
					TDLog.info(e.getMessage(), TDLog.WARNING);
				}
			}
		});

		this.header = new Text(parent, SWT.CENTER | SWT.MULTI | SWT.READ_ONLY);
		this.header.setText("Go to a class or test class");
		this.header.setBackground(widgetColor);
		this.header.setEditable(false);

		GridData gridDataLabel = new GridData(SWT.FILL, SWT.NONE, true, false);
		this.header.setLayoutData(gridDataLabel);

		Display display = Display.getCurrent();
		FontDescriptor fd = FontDescriptor.createFrom(header.getFont());
		Font font = fd.setStyle(SWT.BOLD).createFont(display);
		this.header.setFont(font);

		this.viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		this.contentProvider = new MethodTableContentProvider(this.header, this.fileHandler, parent);
		this.viewer.setContentProvider(this.contentProvider);
		this.viewer.setLabelProvider(new MethodLabelProvider());
		this.viewer.setComparator(new SortByNameSorter());

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					viewer.setInput(getViewSite());
				} catch (AssertionFailedException e) {
					TDLog.info(e.getMessage(), TDLog.WARNING);
				}
			}
		});

		this.viewer.getControl().setBackground(widgetColor);
		GridData gridDataTableView = new GridData(SWT.FILL, SWT.FILL, true, true);
		this.viewer.getControl().setLayoutData(gridDataTableView);
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
		manager.add(sortButton);
		manager.add(new Separator());
		manager.add(configureRootFolder);
		manager.add(showListDialog);
		manager.add(showStatistics);
	}

	private void makeActions() {
		dblClickTableViewer = new OpenMethodAction(this.viewer, this, this.fileHandler);
		selectionChangedComboViewer = new OpenClassAction(this.testClassPaths, this.viewer, this.fileCrawlerHandler);

		configureRootFolder = new Action() {
			public void run() {
				 ConfJumpbackDialog dialog = new
				 ConfJumpbackDialog(Display.getCurrent().getActiveShell());
				 dialog.open();
			}
		};
		configureRootFolder.setText("Configure jumpback");
		configureRootFolder.setImageDescriptor(TDGlobals.cog);
		
		showStatistics = new Action() {
			public void run() {
				StatisticDialog dialog = new 
				StatisticDialog(Display.getCurrent().getActiveShell(), fileCrawlerHandler);
				dialog.open();
			}
		};
		showStatistics.setText("Statistics");
		showStatistics.setImageDescriptor(TDGlobals.stat);
		
		showListDialog = new Action() {
			public void run() {
				ListDialog dialog = new
				ListDialog(Display.getCurrent().getActiveShell(), fileCrawlerHandler);
				dialog.open();
			}
		};
		showListDialog.setText("List non-pair classes");		
		showListDialog.setImageDescriptor(TDGlobals.list);
		
		sortButton = new Action() {
			public void run() {
				if(TDGlobals.shouldSort) {
					TDGlobals.shouldSort = false;
					viewer.setComparator(null);
					sortButton.setImageDescriptor(TDGlobals.sortDisabled);
				} else {
					TDGlobals.shouldSort = true;
					viewer.setComparator(new SortByNameSorter());
					sortButton.setImageDescriptor(TDGlobals.sort);
				}
			}
		};
		sortButton.setText("Sort");
		sortButton.setImageDescriptor(TDGlobals.sort);
		
	}

	private void hookActions() {
		this.viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				dblClickTableViewer.run();
			}
		});

		this.testClassPaths.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent arg0) {
				selectionChangedComboViewer.run();
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