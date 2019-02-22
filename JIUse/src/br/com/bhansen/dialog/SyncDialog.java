package br.com.bhansen.dialog;

import org.eclipse.swt.widgets.Display;

public class SyncDialog<T> {
		
	private T result;
	
	public interface Runner<T> {
		public T run();
	}
	
	public static <T> T open(Runner<T> runner) {
		return new SyncDialog<T>().openSync(runner);
	}
		
	private T openSync(Runner<T> runner) {
		Display.getDefault().syncExec(() -> {
			result = runner.run(); 
		});
		
		return result;
	}

}
