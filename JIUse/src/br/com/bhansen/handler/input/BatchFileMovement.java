package br.com.bhansen.handler.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.refactory.MoveMethodEvaluator;
import br.com.bhansen.utils.Project;
import br.com.bhansen.view.MoveMethod;

public class BatchFileMovement extends InputMovement {
	
	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric) throws Exception {
		
		MoveMethod moveMethod = (MoveMethod) window.getActivePage().showView("iMove.view.movemethod");
		
		InputDialog inDlg = new InputDialog(window.getShell(), "iMove - Inform the batch file", "File address", "", null);
		inDlg.open();
								
		Path inFile = Paths.get(inDlg.getValue());
				
		MessageDialog.openInformation(window.getShell(), "Result", "Result will be shown on cosole!");
		
		Collection<String> out = metricCheck(inFile, type, metric);
		
		GoldChecker.goldCheck(getGoldPath(inFile), getMetricPath(inFile));
		
		try {
			moveMethod.update(new Project(inFile), out);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		MessageDialog.openInformation(window.getShell(), "Finish", "Finish!");
		
		return null;
	}
	
	public static Path getGoldPath(Path path) {
		String goldPath = path.toString();
		return Paths.get(goldPath.substring(0, goldPath.indexOf("results")), "gold_sets", Project.getProjectName(path) + ".txt");
	}
	
	public Set<String> metricCheck(Path inFile, String type, String metric) throws IOException {
		Stream<String> lines = Files.lines(inFile);
		
		try {
			Project project = new Project(inFile);
			
			Set<String> outSet = new HashSet<>();
			
			System.out.println("\nMetric check: " + inFile + "\n");
			
			lines.forEach(new Consumer<String>() {

				public void accept(String movement) {
					try {
						MoveMethodEvaluator evaluator = create(project, movement, type, metric);
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
			
		} finally {
			lines.close();
		}
	}
	
	public static Path getMetricPath(Path inFile) {
		return Paths.get(inFile.toString().replace(".txt", "_metric.txt"));
	}
}