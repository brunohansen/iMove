package br.com.bhansen.dialog;

import org.eclipse.ui.PlatformUI;

public class InputDialog {
	
	public static String open(String title, String message, String initialValue) {
		org.eclipse.jface.dialogs.InputDialog inDlg = new org.eclipse.jface.dialogs.InputDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "iMove - " + title, message, initialValue, null);
		inDlg.open();

		return inDlg.getValue();
	}
	
	public static String open(String title, String message) {
		return open(title, message, "");
	}

}
