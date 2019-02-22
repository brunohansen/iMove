package br.com.bhansen.handler.select;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.dialog.ProgressDialog;
import br.com.bhansen.metric.Metric;
import br.com.bhansen.refactory.EvaluatorFactory;
import br.com.bhansen.utils.Type;
import br.com.bhansen.view.Console;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ShowClassMetric extends SelectionHandler {

	private Metric m;
	
	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric) throws Exception {
		
		Type selection = getType();
				
		MessageDialog.openInformation(window.getShell(), "iMove", "The " + metric + " will be calculated for the openned class!\n\n\n The result dialog will open in a while!");
		
		ProgressDialog.open(window, monitor -> m = EvaluatorFactory.createMetricFactory("class", metric).create(selection, monitor));
		
		String result = m.toString();
		
		Console.println(result);
				
		MessageDialog.openInformation(window.getShell(), "iMove " + metric + " - " + m.getName(), result);
		
		return null;
	}


}
