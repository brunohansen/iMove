package br.com.bhansen.dialog;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.ui.IWorkbenchWindow;

public class ProgressDialog<T> {

	private T result;

	public interface Runnable<T> {
		public T run(IProgressMonitor monitor) throws Exception;
	}

	private T run(IWorkbenchWindow window, Runnable<T> runnable) throws Exception {
		new ProgressMonitorDialog(window.getShell()).run(true, false, monitor -> {
			try {
				result = runnable.run(monitor);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		});

		return result;
	}

	public static <T> T open(IWorkbenchWindow window, Runnable<T> runnable) throws Exception {
		return new ProgressDialog<T>().run(window, runnable);
	}

}
