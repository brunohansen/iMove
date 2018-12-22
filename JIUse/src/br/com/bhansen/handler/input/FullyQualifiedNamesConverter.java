package br.com.bhansen.handler.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import br.com.bhansen.handler.input.FileFinder;

public class FullyQualifiedNamesConverter {

	public static Set<String> getClassNames(Path file) throws IOException {
		Set<String> classes = new HashSet<>();

		Files.lines(file).forEach(s -> {
			classes.add(s.split("::")[0]);
			classes.add(s.split("\t")[1]);
		});

		return classes;
	}

	public static void main(String[] args) throws Exception {

		String startingDir = "C:\\Users\\bruno\\runtime-EclipseApplication\\jfreechart-0.9.6\\src";

		Path inFile = Paths.get("C:\\Users\\bruno\\git\\iMove\\Data\\results\\jfreechart-0.9.6\\jfreechart-0.9.6.txt");

		Map<String, String> fullyQualifiedNames = new HashMap<>();

		for (String name : getClassNames(inFile)) {
			List<Path> paths = FileFinder.find(startingDir, name + ".java");

			if (paths.size() == 1) {
				String fullyQualifiedName = paths.get(0).toString().split("src\\\\", 2)[1].replaceAll("\\\\", ".").replaceFirst("\\.java", "");
				fullyQualifiedNames.put(name, fullyQualifiedName);
			} else {
				throw new Exception("Invalid number of matches! " + name + ":" + paths.size());
			}
		}

		String content = Files.lines(inFile).collect(Collectors.joining("\n"));

		for (Entry<String, String> e : fullyQualifiedNames.entrySet()) {
			content = content.replaceAll("^" + e.getKey() + "::", e.getValue() + "::");
			content = content.replaceAll("\n" + e.getKey() + "::", "\n" + e.getValue() + "::");
			content = content.replaceAll("\t" + e.getKey() + "\t", "\t" + e.getValue() + "\t");
		}

		Files.write(inFile, content.getBytes());

		System.out.println(content);
	}

}
