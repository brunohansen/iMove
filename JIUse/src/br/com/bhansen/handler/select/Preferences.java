package br.com.bhansen.handler.select;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.config.Config;
import br.com.bhansen.config.MoveMethodConfig;
import br.com.bhansen.dialog.ConfigDialog;
import br.com.bhansen.handler.IMoveHandler;

public class Preferences extends IMoveHandler {

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, MoveMethodConfig.Metric metric, Config.MetricContext context)	throws Exception {
		ConfigDialog.openDlg();
		
		return null;
	}
	
}
