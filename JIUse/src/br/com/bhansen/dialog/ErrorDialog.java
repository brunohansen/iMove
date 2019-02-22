package br.com.bhansen.dialog;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

public class ErrorDialog {
	
	public static void open(Throwable e) {
		MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "iMove Error!", e.getMessage());
	}

}
