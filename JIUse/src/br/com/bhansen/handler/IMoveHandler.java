package br.com.bhansen.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

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
					MessageDialog.openInformation(window.getShell(), "iMove Error", e.getCause().getMessage());
				else
					MessageDialog.openInformation(window.getShell(), "iMove Error", e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();

			MessageDialog.openInformation(null, "iMove", e.getMessage());
		}

		return null;
	}

	protected abstract Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric)
			throws Exception;

}
