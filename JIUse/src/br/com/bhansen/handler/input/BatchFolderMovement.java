package br.com.bhansen.handler.input;

import java.nio.file.Path;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.config.Config;
import br.com.bhansen.config.MoveMethodConfig;
import br.com.bhansen.dialog.DirectoryDialog;
import br.com.bhansen.dialog.MessageDialog;
import br.com.bhansen.dialog.ProgressDialog;
import br.com.bhansen.dialog.ProjectDialog;
import br.com.bhansen.dialog.SyncDialog;
import br.com.bhansen.jdt.Project;
import br.com.bhansen.utils.FileFinder;
import br.com.bhansen.view.Console;

public class BatchFolderMovement extends BatchFileMovement {

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, MoveMethodConfig.Metric metric, Config.MetricContext context) throws Exception {

		List<Path> paths = FileFinder.find(DirectoryDialog.open("Inform the batch folder", "Folder address"), "*.txt");
		
		MessageDialog.open("Result will be saved in file and shown on cosole!");
		
		ProgressDialog.open(window, monitor -> {
			
			SubMonitor subMonitor = SubMonitor.convert(monitor, paths.size());

			paths.forEach(path -> {
				Project project;
				
				try {
					project = new Project(path);
				} catch (Exception e) {
					project = SyncDialog.open(() -> ProjectDialog.open("Select project for the file:\n" + path));
				}
				
				goldCheck(project, path, subMonitor.split(1));
			});
			
			return null;
		});
		
		MessageDialog.openFinish();

		return null;
	}

	public void goldCheck(Project project, Path sysFile, IProgressMonitor monitor) {
		try {
			SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
			
			metricCheck(project, sysFile, subMonitor.split(90));
			
			try {
				Path goldFile = GoldChecker.getGoldPath(sysFile);
				GoldChecker.goldCheck(goldFile, getMetricPath(sysFile), subMonitor.split(10));
			} catch (Exception e) {
				Console.printStackTrace(e);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}