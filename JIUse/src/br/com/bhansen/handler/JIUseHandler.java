package br.com.bhansen.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public abstract class JIUseHandler extends AbstractHandler {
	
	@Override
	public Object execute(ExecutionEvent event) {

		try {
			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
			
			try{
				
				return execute(window, event);
			
			} catch (Exception e) {
				e.printStackTrace();
				
				MessageDialog.openInformation(window.getShell(), "JIUse", e.getMessage());
			}	
		} catch (Exception e) {
			e.printStackTrace();
						
			MessageDialog.openInformation(null, "JIUse", e.getMessage());
		}
		
		return null;
	}
	
	protected abstract Object execute(IWorkbenchWindow window, ExecutionEvent event) throws Exception;

}
