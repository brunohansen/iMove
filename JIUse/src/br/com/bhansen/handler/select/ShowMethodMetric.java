package br.com.bhansen.handler.select;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.metric.Metric;
import br.com.bhansen.utils.Method;

public class ShowMethodMetric extends SelectionHandler {

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric) throws Exception {
		
		IType selection = getType();
		String strMethod = new Method(getMethod()).getSignature();
				
		MessageDialog.openInformation(window.getShell(), "iMove", "The " + metric + " will be calculated for the selcted method!\n\n\n The result dialog will open in a while!");
		
		Metric m = createFactory("method", metric).create(selection, strMethod);	
						
		MessageDialog.openInformation(window.getShell(), "iMove " + metric + " - " + m.getName(), strMethod + ": " + new BigDecimal(m.getMetric()).setScale(6, RoundingMode.HALF_EVEN));
		
		return null;
	}


}
