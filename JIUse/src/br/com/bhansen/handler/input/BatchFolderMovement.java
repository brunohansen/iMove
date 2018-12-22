package br.com.bhansen.handler.input;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

public class BatchFolderMovement extends BatchFileMovement {

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric)
			throws Exception {

		InputDialog inDlg = new InputDialog(window.getShell(), "iMove - Inform the batch directory",
				"Directory address", "", null);
		inDlg.open();

		MessageDialog.openInformation(window.getShell(), "Result", "Result will be saved in file and shown on cosole!");

		List<Path> paths = FileFinder.find(inDlg.getValue(), "*.txt");

		paths.forEach(path -> {
			Path goldFile = getGoldPath(path);
			goldCheck(goldFile, path, type, metric);
		});

		MessageDialog.openInformation(window.getShell(), "Finish", "Finish!");

		return null;
	}

	public void goldCheck(Path goldFile, Path sysFile, String type, String metric) {
		try {
			metricCheck(sysFile, type, metric);
			GoldChecker.goldCheck(goldFile, getMetricPath(sysFile));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}