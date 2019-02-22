package br.com.bhansen.handler.input;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.dialog.InputDialog;
import br.com.bhansen.dialog.MessageDialog;
import br.com.bhansen.dialog.ProgressDialog;
import br.com.bhansen.utils.FileFinder;
import br.com.bhansen.utils.Project;
import br.com.bhansen.view.Console;

public class BatchFolderMovement extends BatchFileMovement {

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric) throws Exception {

		List<Path> paths = FileFinder.find(InputDialog.open("Inform the batch folder", "Folder address"), "*.txt");
		
		MessageDialog.open("Result will be saved in file and shown on cosole!");
		
		ProgressDialog.open(window, monitor -> {
			
			SubMonitor subMonitor = SubMonitor.convert(monitor, paths.size());

			paths.forEach(path -> {
				Path goldFile = GoldChecker.getGoldPath(path);
				goldCheck(goldFile, path, type, metric, subMonitor.split(1));
			});
		});
		
		MessageDialog.openFinish();

		return null;
	}

	public void goldCheck(Path goldFile, Path sysFile, String type, String metric, IProgressMonitor monitor) {
		try {
			SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
			
			metricCheck(new Project(sysFile), sysFile, type, metric, subMonitor.split(90));
			
			try {
				GoldChecker.goldCheck(goldFile, getMetricPath(sysFile), subMonitor.split(10));
			} catch (IOException e) {
				Console.printStackTrace(e);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}