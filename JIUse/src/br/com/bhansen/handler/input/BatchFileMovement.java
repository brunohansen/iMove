package br.com.bhansen.handler.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.dialog.ProgressDialog;
import br.com.bhansen.handler.IMoveHandler;
import br.com.bhansen.refactory.EvaluatorFactory;
import br.com.bhansen.refactory.MoveMethodEvaluator;
import br.com.bhansen.utils.Project;
import br.com.bhansen.view.MoveMethod;

public class BatchFileMovement extends IMoveHandler {
	
	private Collection<String> out;

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric) throws Exception {

		InputDialog inDlg = new InputDialog(window.getShell(), "iMove - Inform the batch file", "File address", "", null);
		inDlg.open();

		Path inFile = Paths.get(inDlg.getValue());

		MessageDialog.openInformation(window.getShell(), "Result", "Result will be shown on cosole!");
		
		ProgressDialog.open(window, monitor -> out = metricCheck(inFile, type, metric, monitor));
		
		MoveMethod.show(window, new Project(inFile), out);
		
		try {
			GoldChecker.goldCheck(GoldChecker.getGoldPath(inFile), getMetricPath(inFile), new NullProgressMonitor());
		} catch (Exception e ) {
			e.printStackTrace();
		}
		
		MessageDialog.openInformation(window.getShell(), "Finish", "Finish!");
		
		return null;
	}

	public Set<String> metricCheck(Path inFile, String type, String metric, IProgressMonitor monitor)
			throws IOException {
		List<String> lines = Files.readAllLines(inFile);

		SubMonitor subMonitor = SubMonitor.convert(monitor, lines.size());

		Project project = new Project(inFile);

		Set<String> outSet = new HashSet<>();

		System.out.println("\nMetric check: " + inFile + "\n");

		lines.forEach(new Consumer<String>() {

			public void accept(String movement) {
				SubMonitor loopMonitor = subMonitor.split(1);
				try {
					MoveMethodEvaluator evaluator = EvaluatorFactory.create(project, movement, type, metric, loopMonitor);
					String str = evaluator.toLineString();
					outSet.add(str);
					System.out.println(str);
				} catch (Exception e) {
					String str = "E\t" + movement + "\t Error: " + e.getMessage();
					outSet.add(str);
					System.out.println(str);
				}
			}
		});

		System.out.println("\nMetric check finished.\n");

		Files.write(getMetricPath(inFile), outSet);

		return outSet;
	}

	public static Path getMetricPath(Path inFile) {
		return Paths.get(inFile.toString().replace(".txt", "_metric.txt"));
	}
}