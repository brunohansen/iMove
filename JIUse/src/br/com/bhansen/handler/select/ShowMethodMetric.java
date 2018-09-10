package br.com.bhansen.handler.select;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.metric.Metric;

public class ShowMethodMetric extends SelectionHandler {

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric) throws Exception {
		
		IType selection = getType();
		IMethod method = getMethod();
		String strMethod = AbsMetric.getSignature(method);
				
		MessageDialog.openInformation(window.getShell(), "iMove", "The " + metric + " will be calculated for the selcted method!\n\n\n The result dialog will open in a while!");
		
		Metric m = createFactory("method", metric).create(selection, strMethod);	
						
		MessageDialog.openInformation(window.getShell(), "iMove " + metric + " - " + m.getName(), strMethod + ": " + m.getMetric());
		
		return null;
	}


}
