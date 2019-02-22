package br.com.bhansen.handler.select;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.dialog.InputDialog;
import br.com.bhansen.dialog.MessageDialog;
import br.com.bhansen.metric.iuc.IUCClass;
import br.com.bhansen.utils.Method;
import br.com.bhansen.utils.Type;
import br.com.bhansen.view.Console;

public class ExtractClass extends SelectionHandler {

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric)	throws Exception {
		Type selection = getType();
		
		String method = new Method(getMethod()).getSignature();

		double metricValue = new Double(InputDialog.open("Extract Classes", "Minimum metric value:", "0.4"));
		
		int mthdNumber = new Integer(InputDialog.open("Extract Classes", "Minimum methods number:", "3"));
		
		MessageDialog.openResultOnConsole();
		
		IUCClass iucClass = new IUCClass(selection, new NullProgressMonitor());
		
		new br.com.bhansen.refactory.ExtractClass(window, iucClass, method, metricValue, mthdNumber);
		
		String result = iucClass.toString();
		
		Console.println(result);
		
		MessageDialog.openResultOnConsole();
		
		return null;
	}

}
