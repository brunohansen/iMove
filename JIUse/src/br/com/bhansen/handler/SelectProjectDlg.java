package br.com.bhansen.handler;


import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.widgets.Shell;

public class SelectProjectDlg extends SelectDlg {
		
	protected SelectProjectDlg(Shell parentShell, String dialogTitle, String dialogMessage, String[] options) {
		super(parentShell, dialogTitle, dialogMessage, options);
	}

	protected static IWorkspaceRoot getRoot() {
		// Get the root of the workspace
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		return root;
	}
	
	public static SelectProjectDlg askProject(Shell parentShell, String dialogTitle, String dialogMessage) {
		IWorkspaceRoot root = getRoot();
		
		// Get all projects in the workspace
		IProject[] projects = root.getProjects();
		String[] projNames = new String[projects.length];
		for (int i = 0; i < projects.length; i++) {
			projNames[i] = projects[i].getName();			
		}
		
		SelectProjectDlg dlg = new SelectProjectDlg(parentShell, dialogTitle, dialogMessage, projNames);
		dlg.open();
		
		return dlg;
	}
	
	public IJavaProject getProject() {
		IProject project = getRoot().getProject(this.getSelection());
		return JavaCore.create(project);
	}

}
