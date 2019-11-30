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
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.config.Config;
import br.com.bhansen.config.MoveMethodConfig;
import br.com.bhansen.dialog.FileDialog;
import br.com.bhansen.dialog.MessageDialog;
import br.com.bhansen.dialog.ProgressDialog;
import br.com.bhansen.handler.IMoveHandler;
import br.com.bhansen.jdt.Project;
import br.com.bhansen.utils.FileFinder;
import br.com.bhansen.utils.Movement;
import br.com.bhansen.view.Console;

public class GoldChecker extends IMoveHandler {

	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, MoveMethodConfig.Metric metric, Config.MetricContext context) throws Exception {

		Path inFile = FileDialog.open("Inform the batch file");

		Path goldFile = FileDialog.open("Inform the gold file");

		MessageDialog.openResultOnConsole();

		ProgressDialog.open(window, monitor -> goldCheck(goldFile, inFile, monitor));

		MessageDialog.openFinish();

		return null;
	}

	public static Path getGoldPath(Path path) {
		try {
			String dataPath = path.toString();

			dataPath = dataPath.substring(0, dataPath.indexOf("results"));

			String goldDir = Paths.get(dataPath, "gold_sets").toString();

			return getGoldPath(goldDir, path);
		} catch (Exception e) {
			Console.println("Can't generate gold path based on directory and file name!");
			
			Path goldPath = Paths.get(path.toString().replace(".txt", "_goldsets.txt"));
			
			if(Files.exists(goldPath)) {
				return goldPath;
			} else {
				throw new RuntimeException("Gold file not found! " + goldPath);
			}
		}
	}

	private static Path getGoldPath(String goldDir, Path path) {
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
				Console.println("Invalid File: " + path + " Error: " + e);
			}
		});

	}

	public static Set<String> goldCheck(Path goldFile, Path inFile, IProgressMonitor monitor) throws IOException {
		List<String> goldStream = Files.readAllLines(goldFile);
		List<String> inStream = Files.readAllLines(inFile);
		
		Set<String> outSet = new TreeSet<>();

		Object[] goldArray = goldStream.toArray();
		Supplier<Stream<Object>> goldLines = () -> Stream.of(goldArray);

		Console.println("\nGold check: " + inFile + "\n");

		inStream.forEach(new Consumer<String>() {
			@Override
			public void accept(String inLine) {
				
				String movement = Movement.getMovementString(inLine);

				boolean exact = goldLines.get().anyMatch(new Predicate<Object>() {
					public boolean test(Object goldLine) {
						return movement.equals(goldLine);
					}
				});

				boolean origin = goldLines.get().anyMatch(new Predicate<Object>() {
					public boolean test(Object goldLine) {
						String[] in = movement.split("\\t", 2);
						String[] gold = ((String) goldLine).split("\\t", 2);
						return in[0].equals(gold[0]);
					}
				});

				String[] reg = inLine.split("\\t", 2);
				String outLine = reg[0] + ((origin) ? "0" : "1") + ((exact) ? "0\t" : "1\t") + reg[1];

				outSet.add(outLine);

				Console.println(outLine);

			}
		});

		Console.println("\nGold check finished.\n\n" + inFile.toString().replace(".txt", "_gold.txt"));

		Files.write(Paths.get(inFile.toString().replace(".txt", "_gold.txt")), outSet);
		
		monitor.done();

		return outSet;

	}
}
