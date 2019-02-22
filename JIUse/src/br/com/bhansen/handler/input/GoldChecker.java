package br.com.bhansen.handler.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.dialog.ProgressDialog;
import br.com.bhansen.handler.IMoveHandler;
import br.com.bhansen.utils.FileFinder;
import br.com.bhansen.utils.Project;

public class GoldChecker extends IMoveHandler {

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, String type, String metric) throws Exception {

		InputDialog inDlg = new InputDialog(window.getShell(), "iMove - Inform the batch file", "File address", "", null);
		inDlg.open();

		Path inFile = Paths.get(inDlg.getValue());

		inDlg = new InputDialog(window.getShell(), "iMove - Inform the gold file", "File address", "", null);
		inDlg.open();

		Path goldFile = Paths.get(inDlg.getValue());

		MessageDialog.openInformation(window.getShell(), "Result", "Result will be shown on cosole!");

		ProgressDialog.open(window, monitor -> goldCheck(goldFile, inFile, monitor));

		MessageDialog.openInformation(window.getShell(), "Finish", "Finish!");

		return null;
	}

	public static Path getGoldPath(Path path) {
		String dataPath = path.toString();

		dataPath = dataPath.substring(0, dataPath.indexOf("results"));

		String goldDir = Paths.get(dataPath, "gold_sets").toString();

		return getGoldPath(goldDir, path);
	}

	public static Path getGoldPath(String goldDir, Path path) {
		return Paths.get(goldDir, Project.getProjectName(path) + ".txt");
	}

	public static void goldCheck(String goldDir, String startingDir, IProgressMonitor monitor) throws Exception {
		List<Path> paths = FileFinder.find(startingDir, "*.txt");
		
		SubMonitor subMonitor = SubMonitor.convert(monitor, paths.size());

		paths.forEach(path -> {
			try {
				Path goldFile = getGoldPath(goldDir, path);
				goldCheck(goldFile, path, subMonitor.split(1));
			} catch (Exception e) {
				System.out.println("Invalid File: " + path + " Error: " + e);
			}
		});

	}

	public static Set<String> goldCheck(Path goldFile, Path inFile, IProgressMonitor monitor) throws IOException {
		List<String> goldStream = Files.readAllLines(goldFile);
		List<String> inStream = Files.readAllLines(inFile);
		
		Set<String> outSet = new TreeSet<>();

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

		System.out.println("\nGold check finished.\n\n" + inFile.toString().replace(".txt", "_gold.txt"));

		Files.write(Paths.get(inFile.toString().replace(".txt", "_gold.txt")), outSet);
		
		monitor.done();

		return outSet;

	}

	public static void main(String[] args) throws Exception {
		goldCheck("C:\\Users\\bruno\\git\\iMove\\Data\\gold_sets",
				"C:\\Users\\bruno\\git\\iMove\\Results\\M CAMCJ mais IUCJ\\CAMC 50 2", new NullProgressMonitor());
	}

}
