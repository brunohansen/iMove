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
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.config.Config;
import br.com.bhansen.dialog.FileDialog;
import br.com.bhansen.dialog.MessageDialog;
import br.com.bhansen.dialog.ProgressDialog;
import br.com.bhansen.dialog.ProjectDialog;
import br.com.bhansen.handler.IMoveHandler;
import br.com.bhansen.jdt.Project;
import br.com.bhansen.refactory.EvaluatorFactory;
import br.com.bhansen.refactory.MoveMethodEvaluator;
import br.com.bhansen.view.Console;
import br.com.bhansen.view.MoveMethod;

public class BatchFileMovement extends IMoveHandler {
	
	private Project project;

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, Config.Metric metric, Config.MetricContext context) throws Exception {

		Path inFile = FileDialog.open("Inform the batch file");

		MessageDialog.openResultOnConsole();
		
		try {
			project = new Project(inFile);
		} catch (Exception e) {
			project = ProjectDialog.open();
		}
		
		Collection<String> out = ProgressDialog.open(window, monitor -> metricCheck(project, inFile, monitor));
		
		MoveMethod.show(window, project, out);
		
		try {
			GoldChecker.goldCheck(GoldChecker.getGoldPath(inFile), getMetricPath(inFile), new NullProgressMonitor());
		} catch (Exception e ) {
			Console.printStackTrace(e);
		}
		
		MessageDialog.openFinish();
		
		return null;
	}

	public Set<String> metricCheck(Project project, Path inFile, IProgressMonitor monitor)
			throws IOException {
		List<String> lines = Files.readAllLines(inFile);

		SubMonitor subMonitor = SubMonitor.convert(monitor, lines.size());

		Set<String> outSet = new HashSet<>();

		Console.println("\nMetric check: " + inFile + "\n");

		lines.forEach(new Consumer<String>() {

			public void accept(String movement) {
				SubMonitor loopMonitor = subMonitor.split(1);
				try {
					MoveMethodEvaluator evaluator = EvaluatorFactory.create(project, movement, loopMonitor);
					String str = evaluator.toLineString();
					outSet.add(str);
					Console.println(str);
				} catch (Exception e) {
					String str = "E\t" + movement + "\t Error: " + e.getMessage();
					outSet.add(str);
					Console.println(str);
				}
			}
		});

		Console.println("\nMetric check finished.\n");

		Files.write(getMetricPath(inFile), outSet);

		return outSet;
	}

	public static Path getMetricPath(Path inFile) {
		return Paths.get(inFile.toString().replace(".txt", "_metric.txt"));
	}
}