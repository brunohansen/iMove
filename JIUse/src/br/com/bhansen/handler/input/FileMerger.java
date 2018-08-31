package br.com.bhansen.handler.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class FileMerger {
	
	// cat jtopen-7.8-small/jtopen-7.8-small_jdeodorant_iuc_gold.txt jtopen-7.8-large/jtopen-7.8-large_jdeodorant_iuc_gold.txt | sort | uniq | wc -l
	public static Set<String> merge(Path fileOne, Path fileTwo) throws IOException {
		Stream<String> streamOne = Files.lines(fileOne);
		Stream<String> streamTwo = Files.lines(fileTwo);

		Set<String> outSet = new TreeSet<>();

		try {
			Object[] arrayOne = streamOne.toArray();
			Object[] arrayTwo = streamTwo.toArray();
			
			System.out.println("\nMerge: " + fileOne + " + " + fileTwo + "\n");
			
			outSet.addAll(Arrays.asList(Arrays.copyOf(arrayOne, arrayOne.length, String[].class)));
			outSet.addAll(Arrays.asList(Arrays.copyOf(arrayTwo, arrayTwo.length, String[].class)));
			
			Path out = Paths.get(fileOne.toString().replace("small", "all"));
			
			System.out.println("\nMerge finished.\n");

			Files.write(out, outSet);
			
		} finally {
			streamOne.close();
			streamTwo.close();
		}

		return outSet;

	}
	
	public static void mergeDir(String dir) throws Exception {
		Path inDir = Paths.get(dir);
		
		if(! Files.isDirectory(inDir)) {
			throw new Exception("Directory not found!" + inDir);
		}
		
		Files.list(inDir).forEach(
				new Consumer<Path>() {

					@Override
					public void accept(Path softDir) {
						try {
							if(! Files.isDirectory(softDir)) return;
							
							Files.list(softDir).forEach(
									new Consumer<Path>() {

										@Override
										public void accept(Path moveFile) {
											try {
												if(Files.isDirectory(moveFile)) return;
												
												if(moveFile.toString().contains("small")) {
													merge(moveFile, Paths.get(moveFile.toString().replace("small", "large")));
												}
											} catch (Exception e) {
												throw new RuntimeException(e);
											}											
										}
									}
							);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}						
					}
				}
		);
	}
	
	public static void main(String[] args) throws Exception {
		//merge(Paths.get("/home/hansen/git/jiuse/Results/M CAMCJ mais IUCJ/ant-1.8.2/ant-1.8.2-small_jmove_iuc_gold.txt"), Paths.get("/home/hansen/git/jiuse/Results/M CAMCJ mais IUCJ/ant-1.8.2/ant-1.8.2-large_jmove_iuc_gold.txt"));
		mergeDir("/home/hansen/git/jiuse/Results/M CAMCJ");
	}

}
