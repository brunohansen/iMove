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
import java.util.function.Consumer;
import java.util.stream.Stream;

public class FileMerger {
	
	private static final String OR = "0";
	private static final String AND = "1";
	
	// cat jtopen-7.8-small/jtopen-7.8-small_jdeodorant_metric_gold.txt jtopen-7.8-large/jtopen-7.8-large_jdeodorant_metric_gold.txt | sort | uniq | wc -l
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
			
			if(iterator.hasNext()) {
				String actual = iterator.next();
				String actualStr = actual.replaceFirst("(\\d|\\.|-)+\\t", "");
				
				while (iterator.hasNext()) {
					String next = iterator.next();
					String nextStr = next.replaceFirst("(\\d|\\.|-)+\\t", "");
					
					if(actualStr.equals(nextStr)) {
						System.out.println(actual);
						System.out.println(next);
						
						if(actual.replaceFirst("(\\d|\\.|-)+-", "").startsWith(operator)) {
							set.add(actual);
						} else {
							set.add(next);
						}
						
						if(iterator.hasNext()) {
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
				
				if(actual != null) {
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
													merge(moveFile, Paths.get(moveFile.toString().replace("small", "large")), OR);
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
	
//	cat ant-1.8.2-all_jdeodorant_metric_gold.txt | cut -f2,3 | sort | uniq -d
//	org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool::getJarBaseName(String):String	org.apache.tools.ant.taskdefs.optional.ejb.EjbJar.Config
//	org.apache.tools.ant.taskdefs.optional.ejb.GenericDeploymentTool::getVendorDDPrefix(String, String):String	org.apache.tools.ant.taskdefs.optional.ejb.EjbJar.Config
	public static void main(String[] args) throws Exception {
		//merge(Paths.get("/home/hansen/git/jiuse/Results/M CAMCJ mais IUCJ/ant-1.8.2/ant-1.8.2-small_jmove_metric_gold.txt"), Paths.get("/home/hansen/git/jiuse/Results/M CAMCJ mais IUCJ/ant-1.8.2/ant-1.8.2-large_jmove_metric_gold.txt"));
		mergeDir("C:\\Users\\bruno\\git\\jiuse\\Results\\M IUCJ");
	}

}
