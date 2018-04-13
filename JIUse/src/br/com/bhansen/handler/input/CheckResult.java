package br.com.bhansen.handler.input;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CheckResult {
	
	public static void main(String[] args) throws IOException {
		Set<String> deod = goldCheck("/home/hansen/git/jiuse/Results/junit-4.10/v1/small/junit-4.10-small_gold.txt",
									 "/home/hansen/git/jiuse/Results/junit-4.10/v1/small/junit-4.10-small_jdeodorant_iuc.txt",
									 "/home/hansen/git/jiuse/Results/junit-4.10/v1/small/junit-4.10-small_jdeodorant_iuc_gold.txt");
		
		Set<String> move = goldCheck("/home/hansen/git/jiuse/Results/junit-4.10/v1/small/junit-4.10-small_gold.txt",
				 					 "/home/hansen/git/jiuse/Results/junit-4.10/v1/small/junit-4.10-small_jmove_iuc.txt",
				 					 "/home/hansen/git/jiuse/Results/junit-4.10/v1/small/junit-4.10-small_jmove_iuc_gold.txt");
		
		deod.addAll(move);
		
		Path outPath = 	Paths.get("/home/hansen/git/jiuse/Results/junit-4.10/v1/small/junit-4.10-small_both_iuc.txt");
		
		Files.write(outPath, deod);
	}
	
	public static Set<String> goldCheck(String goldFileAddr, String inFileAddr, String outFileAddr) throws IOException {
		Path goldPath = Paths.get(goldFileAddr);
		Path inPath = 	Paths.get(inFileAddr);
		Path outPath = 	Paths.get(outFileAddr);
		
		Stream<String> inStream = Files.lines(inPath);
		Stream<String> goldStream = Files.lines(goldPath);
		
		Set<String> outSet = new TreeSet<>();
				
		try{
		
			Object [] goldArray = goldStream.toArray();
			Supplier<Stream<Object>> goldLines = () -> Stream.of(goldArray);
			
			inStream.forEach(new Consumer<String>() {
				@Override
				public void accept(String inLine) {
	
					String [] reg = inLine.split("\\t", 2);
					
					boolean any = goldLines.get().anyMatch(new Predicate<Object>() {
						public boolean test(Object goldLine) {
							return reg[1].equals(goldLine);
						}
					});
					
					String outLine = reg[0] + ((any)?"0\t":"1\t") + reg[1];
					
					outSet.add(outLine);
					
					System.out.println(outLine);
					
				}
			});
	
			Files.write(outPath, outSet);
		
		} finally {
			inStream.close();
			goldStream.close();
		}
		
		return outSet;
		
	}

}
