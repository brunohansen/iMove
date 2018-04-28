package br.com.bhansen.handler.select;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.iuc.metric.IUCClass;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ShowIUC extends SelectionHandler {

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event) throws Exception {
		
		MessageDialog.openInformation(window.getShell(), "JIUse", "The IUC will be calculated for the openned class!\n\n\n The result dialog will open in a while!");
		
		IUCClass clazz = new IUCClass(getSelection(), null);
		
		String result = clazz.toString();
		
		System.out.println(result);
				
		MessageDialog.openInformation(window.getShell(), "JIUse IUC - " + clazz.getName(), result);
		
		return null;
	}


}
