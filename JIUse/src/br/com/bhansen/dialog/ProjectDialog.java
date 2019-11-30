package br.com.bhansen.dialog;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import br.com.bhansen.jdt.Project;

public class ProjectDialog {
	
	public static Project open() {
		return open("Project's name:");
	}
	
	public static Project open(String message) {
		ElementListSelectionDialog dlg = new ElementListSelectionDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), new LabelProvider());
		dlg.setTitle("iMove Select Project");
		dlg.setMessage(message);
		dlg.setElements(Project.getProjectsNames());
		dlg.setMultipleSelection(false);
		dlg.setEmptySelectionMessage("Select!");
		dlg.open();
		
		return new Project(dlg.getResult()[0].toString());
	}

}
