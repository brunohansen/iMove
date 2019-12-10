package br.com.bhansen.handler.select;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.config.Config;
import br.com.bhansen.config.MoveMethodConfig;
import br.com.bhansen.dialog.MessageDialog;
import br.com.bhansen.dialog.ProgressDialog;
import br.com.bhansen.jdt.Method;
import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.metric.Metric;
import br.com.bhansen.metric.MetricFactory;

public class ShowMethodMetric extends SelectionHandler {
	
	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, MoveMethodConfig.Metric metric, Config.MetricContext context) throws Exception {
		
		Type selection = getType();
		String strMethod = new Method(getMethod()).getSignature();
		
		MessageDialog.open("The " + (metric.equals("IUC")? "MUC" : "MDC") + " will be calculated for the selcted method!\n\n\n The result dialog will open in a while!");
		
		Metric m = ProgressDialog.open(window, monitor -> MetricFactory.createMethodMetricFactory(metric, context).create(selection, strMethod, monitor));
						
		MessageDialog.open((metric.equals("IUC")? "MUC" : "MDC") + " - " + m.getName(), strMethod + ": " + AbsMetric.round(m.getMetric()));
		
		return null;
	}


}
