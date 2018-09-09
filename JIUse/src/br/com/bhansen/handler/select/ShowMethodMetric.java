package br.com.bhansen.handler.select;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.metric.camc.CAMCJMethod;
import br.com.bhansen.metric.iuc.IUCJMethod;

public class ShowMethodMetric extends SelectionHandler {

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event) throws Exception {
		
		IType type = getType();
		IMethod method = getMethod();
		String strMethod = AbsMetric.getSignature(method);
		
		String metric = event.getParameter("JIUse.metric");
		
		MessageDialog.openInformation(window.getShell(), "iMove", "The " + metric + " will be calculated for the selcted method!\n\n\n The result dialog will open in a while!");
		
		AbsMetric clazz = null;
		
		switch (metric) {
			case "IUC":
				clazz = new IUCJMethod(type, strMethod);
				break;
			case "CAMC":
				clazz = new CAMCJMethod(type, true, strMethod, null);
				break;
			default:
				throw new Exception("Invalid metric!");
		}		
						
		MessageDialog.openInformation(window.getShell(), "iMove " + metric + " - " + clazz.getName(), strMethod + ": " + clazz.getMetric());
		
		return null;
	}


}
