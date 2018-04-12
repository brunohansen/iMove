package br.com.bhansen.handler.input;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.iuc.refactory.EvaluateMoveMethod;

public class Movement extends InputMovement {
	
	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event) throws Exception {
		
		IJavaProject javaProject = askProject(window);
				
		InputDialog inDlg = new InputDialog(window.getShell(), "JIUse - Inform the movement", "Movement", "", null);
		inDlg.open();
								
		EvaluateMoveMethod evaluateMoveMethod = move(window.getShell(), javaProject, inDlg.getValue());
												
		MessageDialog.openInformation(window.getShell(), evaluateMoveMethod.shouldMove()? "Move!!!" : "Don't Move!!!", evaluateMoveMethod.toString());
		
		return null;
	}
		
}
