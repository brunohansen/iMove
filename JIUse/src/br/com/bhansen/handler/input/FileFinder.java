package br.com.bhansen.handler.input;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class FileFinder {

    public static class Finder extends SimpleFileVisitor<Path> {

        private final PathMatcher matcher;
        private final List<Path> matches;

        Finder(String pattern) {
            matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
            matches = new ArrayList<>();
        }

        // Compares the glob pattern against
        // the file or directory name.
        void find(Path file) {
            Path name = file.getFileName();
            if (name != null && matcher.matches(name)) {
                matches.add(file);
            }
        }


        // Invoke the pattern matching
        // method on each file.
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            find(file);
            return CONTINUE;
        }

        // Invoke the pattern matching
        // method on each directory.
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            find(dir);
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            System.err.println(exc);
            return CONTINUE;
        }
    }
    
    public static Set<String> getClassNames(Path file) throws IOException {
    	Set<String> classes = new HashSet<>();
    	
    	Files.lines(file).forEach(s -> { classes.add(s.split("::")[0]); classes.add(s.split("\t")[1]);});
    	
    	return classes;
    }

    public static void main(String[] args) throws Exception {
    	
    	Path startingDir = Paths.get("C:\\Users\\bruno\\runtime-EclipseApplication\\jedit-3.0\\src");
    	
    	Path inFile = Paths.get("C:\\Users\\bruno\\git\\iMove\\Data\\results\\jedit-3.0\\jedit-3.0_methodbook.txt");
    	
    	Map<String, String> fullyQualifiedNames = new HashMap<>();

    	for (String name : getClassNames(inFile)) {
	          Finder finder = new Finder(name + ".java");
	          Files.walkFileTree(startingDir, finder);
	          
	          if(finder.matches.size() == 1) {
	        	  String fullyQualifiedName = finder.matches.get(0).toString().split("src\\\\", 2)[1].replaceAll("\\\\", ".").replaceFirst("\\.java", "");
	        	  fullyQualifiedNames.put(name, fullyQualifiedName);
	          } else {
	        	  throw new Exception("Invalid number of matches!");
	          }
		}
    	
    	String content = Files.lines(inFile).collect(Collectors.joining("\n"));
    	
    	for (Entry<String, String> e : fullyQualifiedNames.entrySet()) {
			content = content.replaceAll(e.getKey() + "::", e.getValue() + "::");
			content = content.replaceAll("\t" + e.getKey(), "\t" + e.getValue());
		}
    	
    	Files.write(inFile, content.getBytes());

    	System.out.println(content);
    }
    
}
