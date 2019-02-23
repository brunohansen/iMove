package br.com.bhansen.handler.select;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.dialog.MessageDialog;
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
				
		MessageDialog.open("The " + metric + " will be calculated for the openned class!\n\n\n The result dialog will open in a while!");
		
		ProgressDialog.open(window, monitor -> m = EvaluatorFactory.createMetricFactory("class", metric).create(selection, monitor));
		
		Console.println(m.toDetailedString());
				
		MessageDialog.open(metric + " - " + m.getName(), m.toString());
		
		return null;
	}


}
