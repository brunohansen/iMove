package br.com.bhansen.handler.input;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.handler.SelectProjectDlg;
import br.com.bhansen.refactory.MMEvaluatorBuilder;

public class FromToMovement extends InputMovement {
			
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event) throws Exception {

		SelectProjectDlg fromDlg = SelectProjectDlg.askProject(window.getShell(), "JIUse - Inform the 'From Class' and the method", "The IUC will be calculated for the 'From Class' and 'ToClass' dialog will open in a while!");
				
		InputDialog toDlg  = new InputDialog(window.getShell(), "JIUse - Inform the 'To Class'", "The IUC will be calculated for the 'To Class' and 'Method' dialog will open in a while!", "", null);
		toDlg.open();
		
		String movement = fromDlg.getValue() + "\t" + toDlg.getValue();
						
		MMEvaluatorBuilder evaluateMoveMethod = move(fromDlg.getProject(), movement);
												
		MessageDialog.openInformation(window.getShell(), evaluateMoveMethod.shouldMove()? "Move!!!" : "Don't Move!!!", evaluateMoveMethod.toString());
		
		return null;
	}

}
