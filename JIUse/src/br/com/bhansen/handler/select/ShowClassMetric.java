package br.com.bhansen.handler.select;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.metric.camc.CAMCClass;
import br.com.bhansen.metric.iuc.IUCClass;
import br.com.bhansen.metric.nhdm.NHDMClass;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ShowClassMetric extends SelectionHandler {

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event) throws Exception {
		
		IType selection = getType();
		
		String metric = event.getParameter("JIUse.metric");
		
		MessageDialog.openInformation(window.getShell(), "iMove", "The " + metric + " will be calculated for the openned class!\n\n\n The result dialog will open in a while!");
		
		AbsMetric clazz = null;
		
		switch (metric) {
			case "IUC":
				clazz = new IUCClass(selection, null);
				break;
			case "CAMC":
				clazz = new CAMCClass(selection, true, null, null);
				break;
			case "NHDM":
				clazz = new NHDMClass(selection, true, null, null);
				break;
			default:
				throw new Exception("Invalid metric!");
		}		
		
		String result = clazz.toString();
		
		System.out.println(result);
				
		MessageDialog.openInformation(window.getShell(), "iMove " + metric + " - " + clazz.getName(), result);
		
		return null;
	}


}
