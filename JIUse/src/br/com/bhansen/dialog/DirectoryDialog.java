package br.com.bhansen.dialog;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.ui.PlatformUI;

public class DirectoryDialog {

	public static Path open(String title, String message) {
		org.eclipse.swt.widgets.DirectoryDialog dialog = new org.eclipse.swt.widgets.DirectoryDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		dialog.setText("iMove - " + title);	
		dialog.setMessage(message);

		return Paths.get(dialog.open());
	}

}
