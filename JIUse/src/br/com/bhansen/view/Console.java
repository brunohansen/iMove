package br.com.bhansen.view;

import java.io.PrintStream;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;

public class Console {

	private static final String CONSOLE_NAME = "iMove.view.console";
	private static MessageConsole instance = null;
	private static PrintStream stream = null;
	
	public static void create(IWorkbenchWindow window) {
		if (instance == null) {
			instance = findConsole(CONSOLE_NAME);
			stream = new PrintStream(instance.newOutputStream());

			IWorkbenchPage page = window.getActivePage();
			
			try {
				IConsoleView view = (IConsoleView) page.showView(IConsoleConstants.ID_CONSOLE_VIEW);
				view.display(instance);
			} catch (PartInitException e) {
				new RuntimeException(e);
			}
		}
	}

	private static MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				return (MessageConsole) existing[i];
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return myConsole;
	}

	private static PrintStream getStream() {
		return stream;
	}
	
	public static void println(String message) {
		getStream().println(message);
	}
	
	public static void println(Object o) {
		println(o.toString());
	}
	
	public static void printStackTrace(Exception e) {
		e.printStackTrace(getStream());
	}

}
