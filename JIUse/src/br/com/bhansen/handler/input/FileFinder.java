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
import java.util.List;

public class FileFinder extends SimpleFileVisitor<Path> {

	private final PathMatcher matcher;
	private final List<Path> matches;

	FileFinder(String pattern) {
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

	static List<Path> find(String startingDir, String pattern) throws Exception {
		Path path = Paths.get(startingDir);
		
		if (!Files.isDirectory(path)) {
			throw new Exception("Directory not found!" + path);
		}
		
		FileFinder finder = new FileFinder(pattern);
		
		Files.walkFileTree(path, finder);
		
		return finder.matches;
	}

}
