package br.com.bhansen.handler.select;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.dialog.ProgressDialog;
import br.com.bhansen.refactory.EvaluatorFactory;
import br.com.bhansen.refactory.MoveMethodEvaluator;
import br.com.bhansen.utils.Type;
import br.com.bhansen.view.MoveMethod;

public class SelectMove extends SelectionHandler {
	
	private static Type classFrom; 
	private static String method; 
	private static Type classTo;
	
	private MoveMethodEvaluator evaluator;
	
	static {
		classFrom = null; 
		method = null; 
		classTo = null; 
	}

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric) throws Exception {
				
		if(classFrom == null) {
			
			try {
				classFrom = getType();
				method = getMethod().getSignature();
				
				MessageDialog.openInformation(window.getShell(), "iMove - Method Selected!", method + "\n\n\n Open the 'To Class' and click on the select to move menu again!");
				
				return null;
			} catch (Exception e) {
				classFrom = null;
				throw e;
			}					

		}
		
		if(classFrom != null) {
			try {
				
				classTo = getType();
				
				MessageDialog.openInformation(window.getShell(), "iMove - Class To Selected!", classTo.getName() + "\n\n\n The result dialog will open in a while!" );
				
				ProgressDialog.open(window, monitor -> evaluator = EvaluatorFactory.create(classFrom, method, classTo, type, metric, monitor));
				
				MoveMethod.show(window, classTo.getProject(), evaluator.toLineString());
				
				MessageDialog.openInformation(window.getShell(), evaluator.shouldMove()? "Move!!!" : "Don't Move!!!", evaluator.toString());
				
			} finally {
				classFrom = null;
			}
		}
		
		return null;
	}
	
}
