package br.com.bhansen.handler.input;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.handler.SelectProjectDlg;
import br.com.bhansen.refactory.MoveMethodEvaluator;

public class Movement extends InputMovement {
	
	private MoveMethodEvaluator evaluator;
	
	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric) throws Exception {
				
		SelectProjectDlg inDlg = SelectProjectDlg.askProject(window.getShell(), "iMove - Inform the movement", "Movement");
		
		openProgressDialog(window, monitor ->  evaluator = create(inDlg.getProject(), inDlg.getValue(), type, metric, monitor));
		
		showMovements(window, inDlg.getProject(), evaluator.toLineString());
		
		MessageDialog.openInformation(window.getShell(), evaluator.shouldMove()? "Move!!!" : "Don't Move!!!", evaluator.toString());			
		
		return null;
	}
}
