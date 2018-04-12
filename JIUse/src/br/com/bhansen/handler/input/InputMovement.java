package br.com.bhansen.handler.input;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.handler.JIUseHandler;
import br.com.bhansen.handler.SelectDlg;
import br.com.bhansen.iuc.refactory.EvaluateMoveMethod;
import org.eclipse.swt.widgets.Shell;

public abstract class InputMovement extends JIUseHandler {
		
	protected IJavaProject askProject(IWorkbenchWindow window) {
		IWorkspaceRoot root = getRoot();
		
		// Get all projects in the workspace
		IProject[] projects = root.getProjects();
		String[] projNames = new String[projects.length];
		for (int i = 0; i < projects.length; i++) {
			projNames[i] = projects[i].getName();			
		}
		
		SelectDlg dlg = new SelectDlg(window.getShell(), "JIUse - Choose a project!", "Method", projNames);
		dlg.open();
		
		IProject project = root.getProject(dlg.getSelection());
		return JavaCore.create(project);
	}

	protected IType findType(IJavaProject javaProject, String fullyQualifiedName) throws Exception {
		IType iType = javaProject.findType(fullyQualifiedName);
		
		if(iType != null) {
			return iType;
		} else {
			throw new Exception("Class " + fullyQualifiedName + " not found!");
		}
	}
	
	protected EvaluateMoveMethod move(Shell shell, IJavaProject javaProject, String movement) throws Exception {
		String [] parts = movement.split("::");
		
		IType classFrom = findType(javaProject, parts[0]);
		
		parts = parts[1].split("\t");
		
		IType classTo = findType(javaProject, parts[1]);
						
		return new EvaluateMoveMethod(shell, classFrom, parts[0], classTo);
	}

}