package br.com.bhansen.metric;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class MethodCorrelationTest extends MetricTest {
	
	@Test
	public void testExampleAClass() {
		Map<String, Set<String>> classA = new HashMap<>();
		
		System.out.println("\n Example A - m3 na ClasseX \n");
		
		classA.put("m1", new HashSet<>(Arrays.asList("p1")));
		classA.put("m2", new HashSet<>(Arrays.asList("p2")));
		classA.put("m3", new HashSet<>(Arrays.asList("p3")));
		
		System.out.println("\n ClasseX \n");
		print(classA);
		
		Map<String, Set<String>> classB = new HashMap<>();

		classB.put("m1", new HashSet<>(Arrays.asList("p3", "p4", "p5")));
		classB.put("m2", new HashSet<>(Arrays.asList("p3", "p4", "p5")));
		
		System.out.println("\n ClasseY \n");
		print(classB);
		
		System.out.println("\n Example A - m3 na ClasseY \n");
		
		classA.clear();
		classA.put("m1", new HashSet<>(Arrays.asList("p1")));
		classA.put("m2", new HashSet<>(Arrays.asList("p2")));
		
		System.out.println("\n ClasseX \n");
		print(classA);

		classB.clear();
		classB.put("m1", new HashSet<>(Arrays.asList("p3", "p4", "p5")));
		classB.put("m2", new HashSet<>(Arrays.asList("p3", "p4", "p5")));
		classB.put("m3", new HashSet<>(Arrays.asList("p3")));
		
		System.out.println("\n ClasseY \n");
		print(classB);
	}
	
//	@Test
	public void testExampleBClass() {
		Map<String, Set<String>> classA = new HashMap<>();
		
		System.out.println("\n Example B - m4 na ClasseX \n");
		
		classA.put("m1", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		classA.put("m2", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		classA.put("m4", new HashSet<>(Arrays.asList("p1")));
		
		System.out.println("\n ClasseX \n");
		print(classA);
		
		Map<String, Set<String>> classB = new HashMap<>();

		classB.put("m1", new HashSet<>(Arrays.asList("p4", "p5", "p6")));
		classB.put("m2", new HashSet<>(Arrays.asList("p4", "p7")));
		classB.put("m3", new HashSet<>(Arrays.asList("p5", "p7")));
		
		System.out.println("\n ClasseY \n");
		print(classB);
		
		System.out.println("\n Example B - m4 na ClasseY \n");
		
		classA.clear();
		classA.put("m1", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		classA.put("m2", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		
		System.out.println("\n ClasseX \n");
		print(classA);

		classB.clear();
		classB.put("m1", new HashSet<>(Arrays.asList("p4", "p5", "p6")));
		classB.put("m2", new HashSet<>(Arrays.asList("p4", "p7")));
		classB.put("m3", new HashSet<>(Arrays.asList("p5", "p7")));
		classB.put("m4", new HashSet<>(Arrays.asList("p1")));
		
		System.out.println("\n ClasseY \n");
		print(classB);
	}
	
//	@Test
	public void testExampleAMethod() {
		Map<String, Set<String>> classA = new HashMap<>();
		
		classA.put("m1", new HashSet<>(Arrays.asList("p1")));
		classA.put("m2", new HashSet<>(Arrays.asList("p2")));
		
		System.out.println("\n Example A - m3 na ClasseX \n");
		print(new HashSet<>(Arrays.asList("p3")), classA);
		
		Map<String, Set<String>> classB = new HashMap<>();

		classB.put("m1", new HashSet<>(Arrays.asList("p3", "p4", "p5")));
		classB.put("m2", new HashSet<>(Arrays.asList("p3", "p4", "p5")));
		
		System.out.println("\n Example A - m3 na ClasseY \n");
		print(new HashSet<>(Arrays.asList("p3")), classB);
	}
	
//	@Test
	public void testExampleBMethod() {
		Map<String, Set<String>> classA = new HashMap<>();
		
		classA.put("m1", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		classA.put("m2", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		
		System.out.println("\n Example B - m4 na ClasseX \n");
		print(new HashSet<>(Arrays.asList("p1")), classA);
		
		Map<String, Set<String>> classB = new HashMap<>();

		classB.put("m1", new HashSet<>(Arrays.asList("p4", "p5", "p6")));
		classB.put("m2", new HashSet<>(Arrays.asList("p4", "p7")));
		classB.put("m3", new HashSet<>(Arrays.asList("p5", "p7")));

		System.out.println("\n Example B - m4 na ClasseY \n");
		print(new HashSet<>(Arrays.asList("p1")), classB);
	}

}
