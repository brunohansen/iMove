package br.com.bhansen.view;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import br.com.bhansen.dialog.ErrorDialog;
import br.com.bhansen.utils.DependencyMatrix;
import br.com.bhansen.utils.Method;
import br.com.bhansen.utils.Type;

public class CorrelationMatrix extends ViewPart {
	
	private TableViewer viewer;
	private Action saveResultsAction;
	private Action latexAction;

	private DependencyMatrix dependencyMatrix;
	
	class content implements IContentProvider {
		
	}
	
	public static void show(IWorkbenchWindow window, DependencyMatrix dependencyMatrix) throws PartInitException {
		CorrelationMatrix correlationMatrix = (CorrelationMatrix) window.getActivePage().showView("iMove.view.correlationmatrix");
		correlationMatrix.update(dependencyMatrix);
	}

	/**
	 * The constructor.
	 */
	public CorrelationMatrix() {

	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		
		Table table = viewer.getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.heightHint = 200;
        table.setLayoutData(data);
        
        viewer.setContentProvider(new IStructuredContentProvider () {
        	
			@Override
			public Object[] getElements(Object inputElement) {
				return ((DependencyMatrix) inputElement).getRows().toArray();
			}
        	
		});
        
		getSite().setSelectionProvider(viewer);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	public void update(DependencyMatrix dependencyMatrix) {
		this.dependencyMatrix = dependencyMatrix;
		
		this.dependencyMatrix.visit(new DependencyMatrix.Visitor() {

			@Override
			public void visit(List<String> columns, List<String> rows, Map<String, String> methods, Type type, boolean[][] matrix) {
				class Column extends ColumnLabelProvider {
					
					private int index;
					
					public Column(int index) {
						this.index = index;
					}
					
					@Override
					public String getText(Object element) {
						return (matrix[rows.indexOf(element)][index])? "Y": "";
					}
				}
				
				Table table = viewer.getTable();
				
				table.removeAll();
				
				while ( table.getColumnCount() > 0 ) {
				    table.getColumns()[0].dispose();
				}
				
				TableViewerColumn mtd = new TableViewerColumn(viewer, SWT.NONE);
				mtd.getColumn().setText("Methods");
				mtd.getColumn().pack();
				mtd.setLabelProvider(new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						String method = element.toString();
						return Method.getAbbreviatedName(method.substring(method.lastIndexOf(" ") + 1));
					}
				});
				
				for (int i = 0; i < columns.size(); i++) {
					TableViewerColumn col = new TableViewerColumn(viewer, SWT.NONE);
					col.getColumn().setText(Type.getAbbreviatedName(columns.get(i)));
					col.getColumn().pack();
					col.setLabelProvider(new Column(i));
				}	
			}
		});
				
		//viewer.getTable().layout();
		
		viewer.setInput(dependencyMatrix);
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
				CorrelationMatrix.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(saveResultsAction);
		manager.add(latexAction);
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
					JavaUI.openInEditor(dependencyMatrix.getMethod(obj.toString()).getIMethod());
				} catch (Exception e) {
					ErrorDialog.open(e);
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
		
		latexAction = new Action() {
			public void run() {
				FileDialog fd = new FileDialog(getSite().getWorkbenchWindow().getShell(), SWT.SAVE);
				fd.setText("Export to Latex");
				String[] filterExt = { "*.txt" };
				fd.setFilterExtensions(filterExt);
				String selected = fd.open();
				try {
					BufferedWriter out = new BufferedWriter(new FileWriter(selected));
					String txt = dependencyMatrix.toString();
					out.write(txt);
					out.close();
					Console.println(txt);
				} catch (IOException e) {
					ErrorDialog.open(e);
					Console.printStackTrace(e);
				}
			}
		};
		latexAction.setToolTipText("Export to Latex");
		latexAction.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ETOOL_SAVE_EDIT));

	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();

				try {
					JavaUI.openInEditor(dependencyMatrix.getMethod(obj.toString()).getIMethod());
				} catch (Exception e) {
					ErrorDialog.open(e);
					throw new RuntimeException(e);
				}
			}
		});
	}

	@Override
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
				ErrorDialog.open(e);
				Console.printStackTrace(e);
			}
		}
	}
}
