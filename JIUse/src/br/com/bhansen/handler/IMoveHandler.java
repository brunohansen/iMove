package br.com.bhansen.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import br.com.bhansen.dialog.ErrorDialog;
import br.com.bhansen.view.Console;

public abstract class IMoveHandler extends AbstractHandler {
		
	@Override
	public Object execute(ExecutionEvent event) {

		try {
			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
			Console.create(window);
			
			try {
				return execute(window, event, event.getParameter("iMove.type"), event.getParameter("iMove.metric"));
				
			} catch (Exception e) {
				Console.printStackTrace(e);
				if(e.getCause() != null)
					ErrorDialog.open(e.getCause());
				else
					ErrorDialog.open(e);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ErrorDialog.open(e);
		}

		return null;
	}

	protected abstract Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric)
			throws Exception;

}
