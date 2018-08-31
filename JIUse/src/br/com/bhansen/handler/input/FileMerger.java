package br.com.bhansen.handler.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class FileMerger {
		
	// cat jtopen-7.8-small/jtopen-7.8-small_jdeodorant_iuc_gold.txt jtopen-7.8-large/jtopen-7.8-large_jdeodorant_iuc_gold.txt | sort | uniq | wc -l
	public static Set<String> merge(Path fileOne, Path fileTwo) throws IOException {
		Stream<String> streamOne = Files.lines(fileOne);
		Stream<String> streamTwo = Files.lines(fileTwo);

		Set<String> outSet = new TreeSet<>();

		try {
			Object[] arrayObj = streamTwo.toArray();			
			String[] arrayTwo = Arrays.copyOf(arrayObj, arrayObj.length, String[].class);
			Supplier<Stream<String>> linesTwo = () -> Stream.of(arrayTwo);
			
			System.out.println("\nMerge: " + fileOne + " + " + fileTwo + "\n");
			
			streamOne.forEach(new Consumer<String>() {
				@Override
				public void accept(String lineOne) {

					boolean contains = linesTwo.get().anyMatch(new Predicate<String>() {
						public boolean test(String lineTwo) {
							return lineOne.replaceFirst("(\\d|\\.|-)+-", "").equals(lineTwo.replaceFirst("(\\d|\\.|-)+-", ""));
						}
					});
					
					if(! contains) {
						outSet.add(lineOne);
					} 
				}
			});
						
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
	
//	cat ant-1.8.2-all_jdeodorant_iuc_gold.txt | cut -f2,3 | sort | uniq -d
//	org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool::getJarBaseName(String):String	org.apache.tools.ant.taskdefs.optional.ejb.EjbJar.Config
//	org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool::getVendorDDPrefix(String, String):String	org.apache.tools.ant.taskdefs.optional.ejb.EjbJar.Config
	public static void main(String[] args) throws Exception {
		//merge(Paths.get("/home/hansen/git/jiuse/Results/M CAMCJ mais IUCJ/ant-1.8.2/ant-1.8.2-small_jmove_iuc_gold.txt"), Paths.get("/home/hansen/git/jiuse/Results/M CAMCJ mais IUCJ/ant-1.8.2/ant-1.8.2-large_jmove_iuc_gold.txt"));
		mergeDir("/home/hansen/git/jiuse/Results/M CAMCJ");
	}

}
