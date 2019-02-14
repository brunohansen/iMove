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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import br.com.bhansen.utils.Project;

public class GoldChecker extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) {

		try {
			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

			InputDialog inDlg = new InputDialog(window.getShell(), "iMove - Inform the batch file", "File address", "", null);
			inDlg.open();

			Path inFile = Paths.get(inDlg.getValue());

			inDlg = new InputDialog(window.getShell(), "iMove - Inform the gold file", "File address", "", null);
			inDlg.open();

			Path goldFile = Paths.get(inDlg.getValue());

			MessageDialog.openInformation(window.getShell(), "Result", "Result will be shown on cosole!");

			goldCheck(goldFile, inFile);

			MessageDialog.openInformation(window.getShell(), "Finish", "Finish!");

		} catch (Exception e) {
			e.printStackTrace();

			MessageDialog.openInformation(null, "iMove", e.getMessage());
		}

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

	public static void goldCheck(String goldDir, String startingDir) throws Exception {
		List<Path> paths = FileFinder.find(startingDir, "*.txt");

		paths.forEach(path -> {
			try {
				Path goldFile = getGoldPath(goldDir, path);
				goldCheck(goldFile, path);
			} catch (Exception e) {
				System.out.println("Invalid File: " + path + " Error: " + e);
			}
		});
		
	}

	public static Set<String> goldCheck(Path goldFile, Path inFile) throws IOException {
		Stream<String> goldStream = Files.lines(goldFile);
		Stream<String> inStream = Files.lines(inFile);

		Set<String> outSet = new TreeSet<>();

		try {

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

		} finally {
			inStream.close();
			goldStream.close();
		}

		return outSet;

	}
	
	public static void main(String[] args) throws Exception {
		goldCheck("C:\\Users\\bruno\\git\\iMove\\Data\\gold_sets", "C:\\Users\\bruno\\git\\iMove\\Results\\M CAMCJ mais IUCJ\\CAMC 50 2");
	}

}
