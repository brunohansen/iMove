package br.com.bhansen.dialog;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IWorkbenchWindow;

public class ProgressDialog {
	
	public interface Runnable {
		public void run(IProgressMonitor monitor) throws Exception;
	}
	
	public static void open(IWorkbenchWindow window, Runnable runnable) throws Exception {
		new ProgressMonitorDialog(window.getShell()).run(true, false, new IRunnableWithProgress() {
			
			@Override
			public void run(IProgressMonitor monitor) {
				try {
					runnable.run(monitor);
				} catch (Exception e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		});
	}

}
