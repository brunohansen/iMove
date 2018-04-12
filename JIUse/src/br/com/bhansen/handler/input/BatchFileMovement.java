package br.com.bhansen.handler.input;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.iuc.refactory.EvaluateMoveMethod;

public class BatchFileMovement extends InputMovement {
	
	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event) throws Exception {
		
		IJavaProject javaProject = askProject(window);
				
		InputDialog inDlg = new InputDialog(window.getShell(), "JIUse - Inform the batch file", "File address", "", null);
		inDlg.open();
		
		Stream<String> lines = Files.lines(Paths.get(inDlg.getValue()));
		
		try {
			
			MessageDialog.openInformation(window.getShell(), "Result", "Result will be shown on cosole!");
			
			lines.forEach(new Consumer<String>() {

				public void accept(String movement) {
					try {
						EvaluateMoveMethod evaluateMoveMethod = move(window.getShell(), javaProject, movement);
						System.out.println(evaluateMoveMethod.toLineString());
					} catch (Exception e) {
						System.out.println(movement + "\t Error: " + e.getMessage());
						//e.printStackTrace();
					}
					
				}
				
			});
			
			System.out.println("Finish!");
			
			MessageDialog.openInformation(window.getShell(), "Finish", "Finish!");
			
		} finally {
			lines.close();
		}
		
		return null;
	}
}