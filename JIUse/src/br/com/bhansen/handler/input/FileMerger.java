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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IWorkbenchWindow;

import br.com.bhansen.config.Config;
import br.com.bhansen.config.MoveMethodConfig;
import br.com.bhansen.dialog.DirectoryDialog;
import br.com.bhansen.dialog.MessageDialog;
import br.com.bhansen.handler.IMoveHandler;
import br.com.bhansen.utils.FileFinder;
import br.com.bhansen.view.Console;

public class FileMerger extends IMoveHandler {

	private static final String OR = "0";
	private static final String AND = "1";
	
	@Override
	protected Object execute(IWorkbenchWindow window, ExecutionEvent event, MoveMethodConfig.Metric metric, Config.MetricContext context)	throws Exception {
		
		mergeDir(DirectoryDialog.open("Inform the batch folder", "Folder address"));
		
		//RemoveTest.remTest(DirectoryDialog.open("Inform the batch folder", "Folder address").toString());

		MessageDialog.openFinish();
		
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
			Console.println("\nMerge: " + fileOne + " + " + fileTwo);

			Object[] arrayOne = streamOne.toArray();
			set.addAll(Arrays.asList(Arrays.copyOf(arrayOne, arrayOne.length, String[].class)));

			Object[] arrayTwo = streamTwo.toArray();
			set.addAll(Arrays.asList(Arrays.copyOf(arrayTwo, arrayTwo.length, String[].class)));

			Console.println("Original: " + (arrayOne.length + arrayTwo.length));
			Console.println("Sem duplicatas exatas: " + set.size());

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
						Console.println(actual);
						Console.println(next);

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

			Console.println("Sem movimentacoes duplicadas: " + set.size());

			Path out = Paths.get(fileOne.toString().replace("small", "all"));

			Console.println("\nMerge finished.\n");

			Files.write(out, set);

		} finally {
			streamOne.close();
			streamTwo.close();
		}

		return set;

	}

	public static void mergeDir(Path dir) throws Exception {

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

}
