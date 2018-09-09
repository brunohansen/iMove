package br.com.bhansen.handler.select;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.handler.SelectDlg;
import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.metric.iuc.IUCClass;
import br.com.bhansen.refactory.MoveMethodEvaluator;

public class SelectMove extends SelectionHandler {
	
	private static IType classFrom; 
	private static String method; 
	private static IType classTo;
	
	static {
		classFrom = null; 
		method = null; 
		classTo = null; 
	}

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric) throws Exception {
		
		if(classFrom == null) {
			
			MessageDialog.openInformation(window.getShell(), "JIUse", "The IUC will be calculated for the openned class!\n\n\n The result dialog will open in a while!");
			
			classFrom = getType();
			
			MessageDialog.openInformation(window.getShell(), "JIUse - Class From Selected!", IUCClass.getClassName(classFrom) + "\n\n\n Open the 'To Class' and click on the select to move menu again!");
			
			return null;
		}
		
		if(classFrom != null) {
			
			MessageDialog.openInformation(window.getShell(), "JIUse", "The IUC will be calculated for the openned class!\n\n\n The result dialog will open in a while!");
			
			classTo = getType();
			
			MessageDialog.openInformation(window.getShell(), "JIUse - Class To Selected!", IUCClass.getClassName(classTo));
			
			Set<String> methods = getMethods(classTo);
			
			String [] mtds = new String[methods.size()];
			
			mtds = methods.toArray(mtds);
					
			SelectDlg dlg = new SelectDlg(window.getShell(), "JIUse - Choose a method to move!", "Method", mtds);
			dlg.open();
			
			MoveMethodEvaluator evaluator = createEvaluator(classFrom, dlg.getSelection(), classTo, type, metric);
											
			MessageDialog.openInformation(window.getShell(), evaluator.shouldMove()? "Move!!!" : "Don't Move!!!", evaluator.toString());
			
			classFrom = null;
		}
		
		return null;
	}
	
	private static Set<String> getMethods(IType type) throws JavaModelException {
		IMethod[] iMethods = type.getMethods();
		Set<String> methods = new HashSet<>();
				
		for (IMethod method : iMethods) {
			methods.add(AbsMetric.getSignature(method));
		}
		
		return methods;
	}
	
}
