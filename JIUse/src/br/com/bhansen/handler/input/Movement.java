package br.com.bhansen.handler.input;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.dialog.MessageDialog;
import br.com.bhansen.dialog.ProgressDialog;
import br.com.bhansen.dialog.SelectProjectDlg;
import br.com.bhansen.handler.IMoveHandler;
import br.com.bhansen.refactory.EvaluatorFactory;
import br.com.bhansen.refactory.MoveMethodEvaluator;
import br.com.bhansen.view.MoveMethod;

public class Movement extends IMoveHandler {
	
	private MoveMethodEvaluator evaluator;
	
	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric) throws Exception {
				
		SelectProjectDlg inDlg = SelectProjectDlg.askProject(window.getShell(), "iMove - Inform the movement", "Movement");
		
		ProgressDialog.open(window, monitor ->  evaluator = EvaluatorFactory.create(inDlg.getProject(), inDlg.getValue(), type, metric, monitor));
		
		MoveMethod.show(window, inDlg.getProject(), evaluator.toLineString());
		
		MessageDialog.open(evaluator.toString());			
		
		return null;
	}
}
