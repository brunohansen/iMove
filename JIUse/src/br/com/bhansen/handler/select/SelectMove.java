package br.com.bhansen.handler.select;

import java.util.Set;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.handler.SelectDlg;
import br.com.bhansen.iuc.metric.IUCClass;
import br.com.bhansen.iuc.metric.MetricClass;
import br.com.bhansen.iuc.refactory.MMEvaluatorBuilder;

public class SelectMove extends SelectionHandler {
	
	private static MMEvaluatorBuilder evaluateMoveMethod;
	
	static {
		evaluateMoveMethod = null;
	}

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event) throws Exception {
		
		if(evaluateMoveMethod == null) {
			
			MessageDialog.openInformation(window.getShell(), "JIUse", "The IUC will be calculated for the openned class!\n\n\n The result dialog will open in a while!");
			
			evaluateMoveMethod = new MMEvaluatorBuilder(getSelection());
			
			MessageDialog.openInformation(window.getShell(), "JIUse - Class From Selected!", IUCClass.getClassName(evaluateMoveMethod.getClassFrom()) + "\n\n\n Open the 'To Class' and click on the select to move menu again!");
			
			return null;
		}
		
		if(evaluateMoveMethod != null) {
			
			MessageDialog.openInformation(window.getShell(), "JIUse", "The IUC will be calculated for the openned class!\n\n\n The result dialog will open in a while!");
			
			evaluateMoveMethod.setClassTo(getSelection());
			
			MessageDialog.openInformation(window.getShell(), "JIUse - Class To Selected!", IUCClass.getClassName(evaluateMoveMethod.getClassFrom()));
			
			Set<String> methods = MetricClass.getMethods(evaluateMoveMethod.getClassFrom());
			
			String [] mtds = new String[methods.size()];
			
			mtds = methods.toArray(mtds);
					
			SelectDlg dlg = new SelectDlg(window.getShell(), "JIUse - Choose a method to move!", "Method", mtds);
			dlg.open();
			
			evaluateMoveMethod.move(dlg.getSelection());
											
			MessageDialog.openInformation(window.getShell(), evaluateMoveMethod.shouldMove()? "Move!!!" : "Don't Move!!!", evaluateMoveMethod.toString());
			
			evaluateMoveMethod = null;
		}
		
		return null;
	}
	
}
