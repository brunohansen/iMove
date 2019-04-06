package br.com.bhansen.handler.select;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.config.Config;
import br.com.bhansen.dialog.MessageDialog;
import br.com.bhansen.dialog.ProgressDialog;
import br.com.bhansen.jdt.Type;
import br.com.bhansen.utils.DependencyMatrix;
import br.com.bhansen.view.CorrelationMatrix;

public class ShowCorrelationMatrix extends SelectionHandler {

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, Config.Metric metric, Config.MetricContext context) throws Exception {
		
		Type selection = getType();
				
		MessageDialog.open("The " + metric + " correlation matrix will be generated for the openned class!\n\n\n The result dialog will open in a while!");
				
		DependencyMatrix dm = ProgressDialog.open(window, monitor -> new DependencyMatrix(context, selection, monitor));
		
		CorrelationMatrix.show(window, dm);
		
		return null;
	}


}
