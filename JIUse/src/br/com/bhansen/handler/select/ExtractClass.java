package br.com.bhansen.handler.select;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.metric.iuc.IUCClass;
import br.com.bhansen.util.SetUtils;

public class ExtractClass extends SelectionHandler {

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric)	throws Exception {
		IType selection = getType();
		
		InputDialog inDialog = new InputDialog(window.getShell(), "Extract Classes", "Minimum metric value:", "0.4", null);
		inDialog.open();
		double metricValue = new Double(inDialog.getValue());
		
		inDialog = new InputDialog(window.getShell(), "Extract Classes", "Minimum methods number:", "3", null);
		inDialog.open();
		int mthdNumber = new Integer(inDialog.getValue());
		
		MessageDialog.openInformation(window.getShell(), "iMove", "The result dialog will open in a while!");
		
		IUCClass iucClass = new IUCClass(selection);
		
		//Extrair por método
		SetUtils.splitByMaxPairIntersection2(iucClass.getMethods(), metricValue, mthdNumber);
		
		//Extrair por caller
		//SetUtils.splitByMaxPairIntersection2(IUC.getCallerMethods(iucClass.getMethods()), metricValue, mthdNumber);
		
		//Extrair por tree
		//new CallTree(iucClass.getMethods());
		
		String result = iucClass.toString();
		
		System.out.println(result);
		
		MessageDialog.openInformation(window.getShell(), "iMove - " + iucClass.getName(), result);
		
		return null;
	}

}
