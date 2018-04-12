package br.com.bhansen.handler.input;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.iuc.refactory.EvaluateMoveMethod;

public class FromToMovement extends InputMovement {
			
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event) throws Exception {

		IJavaProject javaProject = askProject(window);
		
		InputDialog inDlg = new InputDialog(window.getShell(), "JIUse - Inform the 'From Class' and the method", "The IUC will be calculated for the 'From Class' and 'ToClass' dialog will open in a while!", "", null);
		inDlg.open();
		
		String movement = inDlg.getValue();
				
		inDlg = new InputDialog(window.getShell(), "JIUse - Inform the 'From Class'", "The IUC will be calculated for the 'To Class' and 'Method' dialog will open in a while!", "", null);
		inDlg.open();
		
		movement += "\t" + inDlg.getValue();
						
		EvaluateMoveMethod evaluateMoveMethod = move(window.getShell(), javaProject, movement);
												
		MessageDialog.openInformation(window.getShell(), evaluateMoveMethod.shouldMove()? "Move!!!" : "Don't Move!!!", evaluateMoveMethod.toString());
		
		return null;
	}

}
