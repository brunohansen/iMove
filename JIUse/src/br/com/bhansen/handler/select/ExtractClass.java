package br.com.bhansen.handler.select;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.metric.iuc.IUCClass;
import br.com.bhansen.utils.Method;
import br.com.bhansen.utils.Type;

public class ExtractClass extends SelectionHandler {

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric)	throws Exception {
		Type selection = getType();
		
		String method = new Method(getMethod()).getSignature();
		
		InputDialog inDialog = new InputDialog(window.getShell(), "Extract Classes", "Minimum metric value:", "0.4", null);
		inDialog.open();
		double metricValue = new Double(inDialog.getValue());
		
		inDialog = new InputDialog(window.getShell(), "Extract Classes", "Minimum methods number:", "3", null);
		inDialog.open();
		int mthdNumber = new Integer(inDialog.getValue());
		
		MessageDialog.openInformation(window.getShell(), "iMove", "The result dialog will open in a while!");
		
		IUCClass iucClass = new IUCClass(selection);
		
		new br.com.bhansen.refactory.ExtractClass(window, iucClass, method, metricValue, mthdNumber);
		
		String result = iucClass.toString();
		
		System.out.println(result);
		
		MessageDialog.openInformation(window.getShell(), "iMove - " + iucClass.getName(), result);
		
		return null;
	}

}
