package br.com.bhansen.handler.input;

import java.io.File;
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
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.refactory.MoveMethodEvaluator;
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
			moveMethod.update(getProject(getProjectName(inFile)), out);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		MessageDialog.openInformation(window.getShell(), "Finish", "Finish!");
		
		return null;
	}
	
	public static String getProjectName(Path path) {
		String projName = path.toString(); 
		projName = projName.substring(projName.lastIndexOf(File.separator) + 1);
		projName = projName.substring(0, projName.indexOf("_"));
		
		return projName;
	}
		
	public static Path getGoldPath(Path path) {
		String goldPath = path.toString();
		return Paths.get(goldPath.substring(0, goldPath.indexOf("results")), "gold_sets", getProjectName(path) + ".txt");
	}
	
	protected IWorkspaceRoot getRoot() {
		// Get the root of the workspace
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		return root;
	}
	
	public IJavaProject getProject(String projectName) {
		IProject project = getRoot().getProject(projectName);
		
		if(! project.exists() || ! project.isOpen()) {
			throw new RuntimeException("Project " + projectName + " not exists or closed!");
		}
			
		return JavaCore.create(project);
	}	
	
	public Set<String> metricCheck(Path inFile, String type, String metric) throws IOException {
		Stream<String> lines = Files.lines(inFile);
		
		try {
			IJavaProject project = getProject(getProjectName(inFile));
			
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