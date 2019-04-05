package br.com.bhansen.dialog;

import org.eclipse.swt.widgets.Shell;

import br.com.bhansen.jdt.Project;

public class SelectProjectDlg extends SelectDlg {

	protected SelectProjectDlg(Shell parentShell, String dialogTitle, String dialogMessage, String[] options) {
		super(parentShell, dialogTitle, dialogMessage, options);
	}

	public static SelectProjectDlg askProject(Shell parentShell, String dialogTitle, String dialogMessage) {

		SelectProjectDlg dlg = new SelectProjectDlg(parentShell, dialogTitle, dialogMessage, Project.getProjectsNames());
		dlg.open();

		return dlg;
	}

	public Project getProject() {
		return new Project(this.getSelection());
	}

}
