package br.com.bhansen.handler.input;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
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

import br.com.bhansen.iuc.refactory.EvaluateMoveMethod;

public class BatchFileMovement extends InputMovement {
	
	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event) throws Exception {
		
		InputDialog inDlg = new InputDialog(window.getShell(), "JIUse - Inform the batch file", "File address", "", null);
		inDlg.open();
								
		Path inFile = Paths.get(inDlg.getValue());
		
		MessageDialog.openInformation(window.getShell(), "Result", "Result will be shown on cosole!");
		
		iucCheck(inFile);
		goldCheck(getGoldPath(inFile), getIUCPath(inFile));
		
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
	
	public void iucCheck(Path inFile) throws IOException {
		Stream<String> lines = Files.lines(inFile);
		
		try {
			IJavaProject project = getProject(getProjectName(inFile));
			
			Set<String> outSet = new HashSet<>();
			
			System.out.println("\nIUC check: " + inFile + "\n");
			
			lines.forEach(new Consumer<String>() {

				public void accept(String movement) {
					try {
						EvaluateMoveMethod evaluateMoveMethod = move(project, movement);
						String str = evaluateMoveMethod.toLineString();
						outSet.add(str);
						System.out.println(str);
					} catch (Exception e) {
						String str = movement + "\t Error: " + e.getMessage();
						outSet.add(str);
						System.out.println(str);
					}
				}
			});
			
			System.out.println("\nIUC check finished.\n");
			
			Files.write(getIUCPath(inFile), outSet);
			
		} finally {
			lines.close();
		}
	}
	
	public static Path getIUCPath(Path inFile) {
		return Paths.get(inFile.toString().replace(".txt", "_iuc.txt"));
	}
		
	public static Set<String> goldCheck(Path goldFile, Path inFile) throws IOException {
		Stream<String> inStream = Files.lines(inFile);
		Stream<String> goldStream = Files.lines(goldFile);

		Set<String> outSet = new TreeSet<>();

		try {

			Object[] goldArray = goldStream.toArray();
			Supplier<Stream<Object>> goldLines = () -> Stream.of(goldArray);
			
			System.out.println("\nGold check: " + inFile + "\n");

			inStream.forEach(new Consumer<String>() {
				@Override
				public void accept(String inLine) {

					String[] reg = inLine.split("\\t", 2);

					boolean exact = goldLines.get().anyMatch(new Predicate<Object>() {
						public boolean test(Object goldLine) {
							return reg[1].equals(goldLine);
						}
					});
					
					boolean origin = goldLines.get().anyMatch(new Predicate<Object>() {
						public boolean test(Object goldLine) {
							String[] in = reg[1].split("\\t", 2);
							String[] gold = ((String) goldLine).split("\\t", 2);
							return in[0].equals(gold[0]);
						}
					});

					String outLine = reg[0] + ((origin) ? "0" : "1") + ((exact) ? "0\t" : "1\t") + reg[1];

					outSet.add(outLine);

					System.out.println(outLine);

				}
			});
			
			System.out.println("\nGold check finished.\n");

			Files.write(Paths.get(inFile.toString().replace(".txt", "_gold.txt")), outSet);
			
		} finally {
			inStream.close();
			goldStream.close();
		}

		return outSet;

	}
}