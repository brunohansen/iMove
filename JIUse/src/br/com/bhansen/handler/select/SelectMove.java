package br.com.bhansen.handler.select;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.refactory.MoveMethodEvaluator;
import br.com.bhansen.utils.MethodHelper;
import br.com.bhansen.utils.TypeHelper;

public class SelectMove extends SelectionHandler {
	
	private static IType classFrom; 
	private static String method; 
	private static IType classTo;
	
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
				method = MethodHelper.getSignature(getMethod());
				
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
				
				MessageDialog.openInformation(window.getShell(), "iMove - Class To Selected!", TypeHelper.getClassName(classTo) + "\n\n\n The result dialog will open in a while!" );
								
				MoveMethodEvaluator evaluator = createEvaluator(classFrom, method, classTo, type, metric);
												
				MessageDialog.openInformation(window.getShell(), evaluator.shouldMove()? "Move!!!" : "Don't Move!!!", evaluator.toString());
				
			} finally {
				classFrom = null;
			}
		}
		
		return null;
	}
	
}
