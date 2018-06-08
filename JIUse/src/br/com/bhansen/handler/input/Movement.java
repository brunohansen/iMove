package br.com.bhansen.handler.input;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.handler.SelectProjectDlg;
import br.com.bhansen.refactory.MMEvaluatorBuilder;

public class Movement extends InputMovement {
	
	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event) throws Exception {
		
		SelectProjectDlg inDlg = SelectProjectDlg.askProject(window.getShell(), "JIUse - Inform the movement", "Movement");
		
		MMEvaluatorBuilder evaluateMoveMethod = move(inDlg.getProject(), inDlg.getValue());
												
		MessageDialog.openInformation(window.getShell(), evaluateMoveMethod.shouldMove()? "Move!!!" : "Don't Move!!!", evaluateMoveMethod.toString());
		
		return null;
	}
}
