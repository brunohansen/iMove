package br.com.bhansen.metric;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SparserTest extends MetricTest {

//	@Test
	public void testGoSparser() {
		HashMap<String, Set<String>> mtds1 = new HashMap<>();

		mtds1.put("m1", new HashSet<>(Arrays.asList("p1", "p3")));
		mtds1.put("m2", new HashSet<>(Arrays.asList("p1", "p2")));
		mtds1.put("m3", new HashSet<>(Arrays.asList("p2", "p3")));
		
		HashMap<String, Set<String>> mtds2 = new HashMap<>();
		
		mtds2.put("m1", new HashSet<>(Arrays.asList("p1", "p3")));
		mtds2.put("m2", new HashSet<>(Arrays.asList("p1", "p2")));
		mtds2.put("m3", new HashSet<>(Arrays.asList("p2", "p3")));
		mtds2.put("m4", new HashSet<>(Arrays.asList("p1")));
		
		for (int i = 0; i < 500; i++) {
			mtds1.put("m" + (i + 4), new HashSet<>(Arrays.asList("p1")));
			mtds2.put("m" + (i + 5), new HashSet<>(Arrays.asList("p1")));
			System.out.println("\nA5+ - Test Go Sparser\n");
			test(mtds1, mtds2, greaterThanOrEqualTo());
		}
	}
	
//	@Test
	public void testGoSparser2() {
		HashMap<String, Set<String>> mtds1 = new HashMap<>();

		mtds1.put("m1", new HashSet<>(Arrays.asList("p1", "p2")));
		mtds1.put("m2", new HashSet<>(Arrays.asList("p1", "p2")));
		
		HashMap<String, Set<String>> mtds2 = new HashMap<>();
		
		mtds2.put("m1", new HashSet<>(Arrays.asList("p1", "p2")));
		mtds2.put("m2", new HashSet<>(Arrays.asList("p1", "p2")));
		mtds2.put("m3", new HashSet<>(Arrays.asList("p1")));
		
		for (int i = 0; i < 500; i++) {
			mtds1.put("m" + (i + 3), new HashSet<>(Arrays.asList("p1")));
			mtds2.put("m" + (i + 4), new HashSet<>(Arrays.asList("p1")));
			System.out.println("\nA5+ - Test Go Sparser\n");
			test(mtds1, mtds2, greaterThanOrEqualTo());
		}
	}
	
}
