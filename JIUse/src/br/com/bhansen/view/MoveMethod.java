package br.com.bhansen.view;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import br.com.bhansen.refactory.MoveMethodRefactor;
import br.com.bhansen.utils.Project;

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

public class MoveMethod extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "ssas.views.SampleView";

	private TableViewer viewer;
	private Action saveResultsAction;
	private Action doubleClickAction;

	private Project project;

	/**
	 * The constructor.
	 */
	public MoveMethod() {

	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);

		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(ArrayContentProvider.getInstance());
		// viewer.setLabelProvider(new ViewLabelProvider());

		ColumnViewerToolTipSupport.enableFor(viewer);

		TableViewerColumn shouldMove = new TableViewerColumn(viewer, SWT.NONE);
		shouldMove.getColumn().setWidth(200);
		shouldMove.getColumn().setText("Should Move?");
		shouldMove.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				return (element.toString().split("\t", 2)[0].replaceFirst("(\\d|\\.|-)+-", "").startsWith("0")) ? "Yes"
						: "No";
			}
		});

		TableViewerColumn sourceMethod = new TableViewerColumn(viewer, SWT.NONE);
		sourceMethod.getColumn().setWidth(600);
		sourceMethod.getColumn().setText("Source Method");
		sourceMethod.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return element.toString().split("\t", 3)[1];
			}
		});

		TableViewerColumn targetClass = new TableViewerColumn(viewer, SWT.NONE);
		targetClass.getColumn().setWidth(400);
		targetClass.getColumn().setText("Target Class");
		targetClass.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return element.toString().split("\t", 4)[2];
			}
		});

		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	public void update(Project project, Object movements) {
		this.project = project;
		if(movements instanceof Collection)
			viewer.setInput(movements);
		else
			viewer.setInput(new Object[]{movements});
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				MoveMethod.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(saveResultsAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(new Action() {
			@Override
			public String getText() {
				return "View Method";
			}

			@Override
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();

				try {
					JavaUI.openInEditor(project.findMethod(obj.toString().split("\t", 2)[1]).getIMethod());
				} catch (Exception e) {
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error!", e.getMessage());
					throw new RuntimeException(e);
				}
			}
		});

		manager.add(new Action() {
			@Override
			public String getText() {
				return "View Target Class";
			}

			@Override
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();

				try {
					JavaUI.openInEditor(project.findClassTo(obj.toString().split("\t", 2)[1]).getIType());
				} catch (Exception e) {
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error!", e.getMessage());
					throw new RuntimeException(e);
				}
			}
		});

		manager.add(new Action() {
			@Override
			public String getText() {
				return "Open Move Method Assitant";
			}

			@Override
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();

				try {
					MoveMethodRefactor.moveWizard(project.findMethod(obj.toString().split("\t", 2)[1]), viewer.getControl().getShell());
				} catch (Exception e) {
					MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error!", e.getMessage());
					throw new RuntimeException(e);
				}
			}
		});
	}

	private void makeActions() {
		saveResultsAction = new Action() {
			public void run() {
				saveResults();
			}
		};
		saveResultsAction.setToolTipText("Save Results");
		saveResultsAction.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_SAVE_EDIT));

	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private void saveResults() {
		FileDialog fd = new FileDialog(getSite().getWorkbenchWindow().getShell(), SWT.SAVE);
		fd.setText("Save Results");
		String[] filterExt = { "*.txt" };
		fd.setFilterExtensions(filterExt);
		String selected = fd.open();
		if (selected != null) {
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(selected));
				Table table = viewer.getTable();
				TableColumn[] columns = table.getColumns();
				for (int i = 0; i < columns.length; i++) {
					if (i == columns.length - 1)
						out.write(columns[i].getText());
					else
						out.write(columns[i].getText() + "\t");
				}
				out.newLine();
				for (int i = 0; i < table.getItemCount(); i++) {
					TableItem tableItem = table.getItem(i);
					for (int j = 0; j < table.getColumnCount(); j++) {
						if (j == table.getColumnCount() - 1)
							out.write(tableItem.getText(j));
						else
							out.write(tableItem.getText(j) + "\t");
					}
					out.newLine();
				}
				out.close();
			} catch (IOException e) {
				MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error!", e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
