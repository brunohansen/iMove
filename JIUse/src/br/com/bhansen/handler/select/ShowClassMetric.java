package br.com.bhansen.handler.select;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.metric.Metric;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ShowClassMetric extends SelectionHandler {

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric) throws Exception {
		
		IType selection = getType();
				
		MessageDialog.openInformation(window.getShell(), "iMove", "The " + metric + " will be calculated for the openned class!\n\n\n The result dialog will open in a while!");
		
		Metric m = createFactory("class", metric).create(selection);
		
		String result = m.toString();
		
		System.out.println(result);
				
		MessageDialog.openInformation(window.getShell(), "iMove " + metric + " - " + m.getName(), result);
		
		return null;
	}


}
