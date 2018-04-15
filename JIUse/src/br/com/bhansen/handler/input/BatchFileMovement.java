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
								
		MessageDialog.openInformation(window.getShell(), "Result", "Result will be shown on cosole!");
		Path inFile = Paths.get(inDlg.getValue());
		
		String projName = inFile.toString();
		projName = projName.substring(projName.lastIndexOf(File.separator), projName.lastIndexOf("_"));
		Path goldFile = Paths.get(inFile.subpath(0, inFile.getNameCount() - 3).toAbsolutePath().toString(), "gold_sets", projName + ".txt");
		
		iucCheck(inFile);
		goldCheck(goldFile, inFile);
		
		MessageDialog.openInformation(window.getShell(), "Finish", "Finish!");
		
		return null;
	}
	
	protected IWorkspaceRoot getRoot() {
		// Get the root of the workspace
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		return root;
	}
	
	public IJavaProject getProject(String projectName) {
		IProject project = getRoot().getProject(projectName);
		return JavaCore.create(project);
	}
	
	
	public void iucCheck(Path inFile) throws IOException {
		Stream<String> lines = Files.lines(inFile);
		
		try {
			String projName = inFile.toString();
			projName = projName.substring(projName.lastIndexOf(File.separator), projName.lastIndexOf("_"));
			
			IJavaProject project = getProject(projName);
			
			Set<String> outSet = new HashSet<>();
			
			System.out.println("IUC check: " + inFile);
			
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
			
			System.out.println("IUC check finished.");
			
			Files.write(Paths.get(inFile.toString().replace(".txt", "_iuc.txt")), outSet);
			
		} finally {
			lines.close();
		}
	}
	
	public Set<String> goldCheck(Path goldFile, Path inFile) throws IOException {
		Stream<String> inStream = Files.lines(inFile);
		Stream<String> goldStream = Files.lines(goldFile);

		Set<String> outSet = new TreeSet<>();

		try {

			Object[] goldArray = goldStream.toArray();
			Supplier<Stream<Object>> goldLines = () -> Stream.of(goldArray);
			
			System.out.println("Gold check: " + inFile);

			inStream.forEach(new Consumer<String>() {
				@Override
				public void accept(String inLine) {

					String[] reg = inLine.split("\\t", 2);

					boolean any = goldLines.get().anyMatch(new Predicate<Object>() {
						public boolean test(Object goldLine) {
							return reg[1].equals(goldLine);
						}
					});

					String outLine = reg[0] + ((any) ? "0\t" : "1\t") + reg[1];

					outSet.add(outLine);

					System.out.println(outLine);

				}
			});
			
			System.out.println("Gold check finished.");

			Files.write(Paths.get(inFile.toString().replace(".txt", "_gold.txt")), outSet);
			
		} finally {
			inStream.close();
			goldStream.close();
		}

		return outSet;

	}
}