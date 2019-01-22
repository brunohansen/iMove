package br.com.bhansen.handler.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class FileMerger extends AbstractHandler {

	private static final String OR = "0";
	private static final String AND = "1";

	@Override
	public Object execute(ExecutionEvent event) {

		try {
			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

			InputDialog inDlg = new InputDialog(window.getShell(), "iMove - Inform the batch directory",
					"Directory address", "", null);
			inDlg.open();
			
			//mergeDir(inDlg.getValue());
			
			RemoveTest.remTest(inDlg.getValue());

			MessageDialog.openInformation(window.getShell(), "Finish", "Finish!");

		} catch (Exception e) {
			e.printStackTrace();

			MessageDialog.openInformation(null, "iMove", e.getMessage());
		}

		return null;
	}

	// cat jtopen-7.8-small/jtopen-7.8-small_jdeodorant_metric_gold.txt
	// jtopen-7.8-large/jtopen-7.8-large_jdeodorant_metric_gold.txt | sort |
	// uniq | wc -l
	public static Set<String> merge(Path fileOne, Path fileTwo, String operator) throws IOException {
		Stream<String> streamOne = Files.lines(fileOne);
		Stream<String> streamTwo = Files.lines(fileTwo);

		Set<String> set = new TreeSet<>();

		try {
			System.out.println("\nMerge: " + fileOne + " + " + fileTwo);

			Object[] arrayOne = streamOne.toArray();
			set.addAll(Arrays.asList(Arrays.copyOf(arrayOne, arrayOne.length, String[].class)));

			Object[] arrayTwo = streamTwo.toArray();
			set.addAll(Arrays.asList(Arrays.copyOf(arrayTwo, arrayTwo.length, String[].class)));

			System.out.println("Original: " + (arrayOne.length + arrayTwo.length));
			System.out.println("Sem duplicatas exatas: " + set.size());

			List<String> list = new ArrayList<>(set);
			list.sort(new Comparator<String>() {

				@Override
				public int compare(String s1, String s2) {
					return s1.replaceFirst("(\\d|\\.|-)+\\t", "").compareTo(s2.replaceFirst("(\\d|\\.|-)+\\t", ""));
				}
			});

			set = new TreeSet<>();

			Iterator<String> iterator = list.iterator();

			if (iterator.hasNext()) {
				String actual = iterator.next();
				String actualStr = actual.replaceFirst("(\\d|\\.|-)+\\t", "");

				while (iterator.hasNext()) {
					String next = iterator.next();
					String nextStr = next.replaceFirst("(\\d|\\.|-)+\\t", "");

					if (actualStr.equals(nextStr)) {
						System.out.println(actual);
						System.out.println(next);

						if (actual.replaceFirst("(\\d|\\.|-)+-", "").startsWith(operator)) {
							set.add(actual);
						} else {
							set.add(next);
						}

						if (iterator.hasNext()) {
							next = iterator.next();
							nextStr = next.replaceFirst("(\\d|\\.|-)+\\t", "");
						} else {
							actual = null;
							break;
						}
					} else {
						set.add(actual);
					}

					actual = next;
					actualStr = nextStr;
				}

				if (actual != null) {
					set.add(actual);
				}
			}

			System.out.println("Sem movimentacoes duplicadas: " + set.size());

			Path out = Paths.get(fileOne.toString().replace("small", "all"));

			System.out.println("\nMerge finished.\n");

			Files.write(out, set);

		} finally {
			streamOne.close();
			streamTwo.close();
		}

		return set;

	}

	public static void mergeDir(String dir) throws Exception {

		List<Path> files = FileFinder.find(dir, "*.txt");

		files.forEach(file -> {
			if (file.toString().contains("small")) {
				try {
					merge(file, Paths.get(file.toString().replace("small", "large")), OR);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});

	}

	// cat ant-1.8.2-all_jdeodorant_metric_gold.txt | cut -f2,3 | sort | uniq -d
	// org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool::getJarBaseName(String):String
	// org.apache.tools.ant.taskdefs.optional.ejb.EjbJar.Config
	// org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool::getVendorDDPrefix(String,
	// String):String org.apache.tools.ant.taskdefs.optional.ejb.EjbJar.Config
	public static void main(String[] args) throws Exception {
		// merge(Paths.get("/home/hansen/git/jiuse/Results/M CAMCJ mais
		// IUCJ/ant-1.8.2/ant-1.8.2-small_jmove_metric_gold.txt"),
		// Paths.get("/home/hansen/git/jiuse/Results/M CAMCJ mais
		// IUCJ/ant-1.8.2/ant-1.8.2-large_jmove_metric_gold.txt"));
		mergeDir("C:\\Users\\bruno\\git\\jiuse\\Results\\M IUCJ");
	}

}
