package br.com.bhansen.dialog;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.ui.PlatformUI;

public class FileDialog {

	public static Path open(String title) {
		org.eclipse.swt.widgets.FileDialog dialog = new org.eclipse.swt.widgets.FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		dialog.setText("iMove - " + title);	
		dialog.setFilterExtensions(new String[]{"*.txt"});

		return Paths.get(dialog.open());
	}

}
