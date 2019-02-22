package br.com.bhansen.handler.input;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

public class BatchFolderMovement extends BatchFileMovement {

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric)
			throws Exception {

		InputDialog inDlg = new InputDialog(window.getShell(), "iMove - Inform the batch directory", "Directory address", "", null);
		inDlg.open();

		MessageDialog.openInformation(window.getShell(), "Result", "Result will be saved in file and shown on cosole!");

		List<Path> paths = FileFinder.find(inDlg.getValue(), "*.txt");
		
		openProgressDialog(window, monitor -> {
			
			SubMonitor subMonitor = SubMonitor.convert(monitor, paths.size());

			paths.forEach(path -> {
				Path goldFile = GoldChecker.getGoldPath(path);
				goldCheck(goldFile, path, type, metric, subMonitor.split(1));
			});
		});
		
		MessageDialog.openInformation(window.getShell(), "Finish", "Finish!");

		return null;
	}

	public void goldCheck(Path goldFile, Path sysFile, String type, String metric, IProgressMonitor monitor) {
		try {
			SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
			
			metricCheck(sysFile, type, metric, subMonitor.split(90));
			GoldChecker.goldCheck(goldFile, getMetricPath(sysFile), subMonitor.split(10));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}