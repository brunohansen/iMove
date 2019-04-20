package br.com.bhansen.metric;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnomalyTest extends MetricTest {
	
//	@Test
	public void test100() {
		System.out.println("\nTest 100%\n");
		
		Map<String, Set<String>> mtds = new HashMap<>();
		
		mtds.put("m1", new HashSet<>(Arrays.asList("p1", "p2")));
		mtds.put("m2", new HashSet<>(Arrays.asList("p1", "p2")));
		
		test(mtds, 1.0, equalTo());
		
		mtds = new HashMap<>();

		mtds.put("m1", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		mtds.put("m2", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		mtds.put("m3", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		
		test(mtds, 1.0, equalTo());
	}
	
//	@Test
	public void testZero() {
		System.out.println("\nA1 e A3 - Test Zero\n");
		
		Map<String, Set<String>> mtds1 = new HashMap<>();

		mtds1.put("m1", new HashSet<>(Arrays.asList("p1")));
		mtds1.put("m2", new HashSet<>(Arrays.asList("p2")));
		
		test(mtds1, 0.0, equalTo());
		
		System.out.println("\nA1 e A3 - Test Zero\n");
		
		Map<String, Set<String>> mtds2 = new HashMap<>();

		mtds2.put("m1", new HashSet<>(Arrays.asList("p1")));
		mtds2.put("m2", new HashSet<>(Arrays.asList("p2")));
		mtds2.put("m3", new HashSet<>(Arrays.asList("p3")));
		
		test(mtds2, 0.0, equalTo());
		
//		Map<String, Set<String>> mtds3 = new HashMap<>();
//
//		mtds3.put("m1", new HashSet<>(Arrays.asList("p1")));
//		mtds3.put("m2", new HashSet<>(Arrays.asList("p2", "p3")));
//		
//		System.out.println("MMAC " + D3C2iClass.mmac(mtds3));
//		System.out.println("AAC " + D3C2iClass.aac(mtds3));
//		System.out.println("AMC " + D3C2iClass.amc(mtds3));
//		System.out.println("WPPC " + WICClass.pp(mtds2, WICClass.createPPWeight(mtds3.size())));
//		
//		test(mtds3, AbsMetric.uniqueValues(mtds3), 0.0, equalTo());
	}
	
//	@Test
	public void testPattern() {
		System.out.println("\nA2 - Test Pattern\n");
		
		Map<String, Set<String>> mtds1 = new HashMap<>();

		mtds1.put("m1", new HashSet<>(Arrays.asList("p1")));
		mtds1.put("m2", new HashSet<>(Arrays.asList("p1", "p2", "p3", "p4")));
		mtds1.put("m3", new HashSet<>(Arrays.asList("p1", "p2", "p3", "p4")));
		mtds1.put("m4", new HashSet<>(Arrays.asList("p3")));
		mtds1.put("m5", new HashSet<>(Arrays.asList("p4")));
		
		Map<String, Set<String>> mtds2 = new HashMap<>();

		mtds2.put("m1", new HashSet<>(Arrays.asList("p3", "p4")));
		mtds2.put("m2", new HashSet<>(Arrays.asList("p1", "p2")));
		mtds2.put("m3", new HashSet<>(Arrays.asList("p2", "p3")));
		mtds2.put("m4", new HashSet<>(Arrays.asList("p1", "p4")));
		mtds2.put("m5", new HashSet<>(Arrays.asList("p2", "p3", "p4")));
		
		test(mtds1, mtds2, notEqualTo());
	}
	
//	@Test
	public void testManyToZero() {
		System.out.println("\nA4 - Test Many To Zero\n");
		
		Map<String, Set<String>> mtds1 = new HashMap<>();

		mtds1.put("m1", new HashSet<>(Arrays.asList("p1", "p2", "p3", "p4")));
		mtds1.put("m2", new HashSet<>(Arrays.asList("p1")));
		mtds1.put("m3", new HashSet<>(Arrays.asList("p1", "p2", "p3", "p4")));
		mtds1.put("m4", new HashSet<>(Arrays.asList("p2", "p3", "p4")));
		
		Map<String, Set<String>> mtds2 = new HashMap<>();

		mtds2.put("m1", new HashSet<>(Arrays.asList("p3")));
		mtds2.put("m2", new HashSet<>(Arrays.asList("p1")));
		mtds2.put("m3", new HashSet<>(Arrays.asList("p4")));
		mtds2.put("m4", new HashSet<>(Arrays.asList("p2")));
		
		test(mtds1, mtds2, notEqualTo());
	}
	
//	@Test
	public void testMonotonocity() {
		System.out.println("\nVM - Test Monotonocity\n");
		
		HashMap<String, Set<String>> mtds1 = new HashMap<>();

		mtds1.put("m1", new HashSet<>(Arrays.asList("p1")));
		mtds1.put("m2", new HashSet<>(Arrays.asList("p1")));
		mtds1.put("m3", new HashSet<>(Arrays.asList("p1", "p2")));
		mtds1.put("m4", new HashSet<>(Arrays.asList("p1", "p2", "p3", "p4")));
		
		HashMap<String, Set<String>> mtds2 = new HashMap<>();

		mtds2.put("m1", new HashSet<>(Arrays.asList("p1")));
		mtds2.put("m2", new HashSet<>(Arrays.asList("p1", "p4")));
		mtds2.put("m3", new HashSet<>(Arrays.asList("p1", "p2")));
		mtds2.put("m4", new HashSet<>(Arrays.asList("p1", "p2", "p3", "p4")));
		
		test(mtds1, mtds2, lessThanOrEqualTo());
		
		System.out.println("\nVM - Test Monotonocity\n");
		
		HashMap<String, Set<String>> mtds3 = new HashMap<>();

		mtds3.put("m1", new HashSet<>(Arrays.asList("p1", "p2")));
		mtds3.put("m2", new HashSet<>(Arrays.asList("p1", "p2")));
		mtds3.put("m3", new HashSet<>());
		mtds3.put("m4", new HashSet<>());
		mtds3.put("m5", new HashSet<>());
		mtds3.put("m6", new HashSet<>());
		mtds3.put("m7", new HashSet<>());
		
		HashMap<String, Set<String>> mtds4 = new HashMap<>();

		mtds4.put("m1", new HashSet<>(Arrays.asList("p1", "p2")));
		mtds4.put("m2", new HashSet<>(Arrays.asList("p1", "p2")));
		mtds4.put("m3", new HashSet<>(Arrays.asList("p1")));
		mtds4.put("m4", new HashSet<>());
		mtds4.put("m5", new HashSet<>());
		mtds4.put("m6", new HashSet<>());
		mtds4.put("m7", new HashSet<>());
		
		test(mtds3, mtds4, lessThanOrEqualTo());
	}
	
//	@Test
	public void testSparse() {
		System.out.println("\nA5a - Test Sparse\n");
		
		HashMap<String, Set<String>> mtds1 = new HashMap<>();

		mtds1.put("m1", new HashSet<>(Arrays.asList("p1", "p3")));
		mtds1.put("m2", new HashSet<>(Arrays.asList("p1", "p2")));
		mtds1.put("m3", new HashSet<>(Arrays.asList("p2", "p3")));
		mtds1.put("m4", new HashSet<>(Arrays.asList("p1")));
		
		HashMap<String, Set<String>> mtds2 = new HashMap<>();

		mtds2.put("m1", new HashSet<>(Arrays.asList("p1", "p3")));
		mtds2.put("m2", new HashSet<>(Arrays.asList("p1", "p2")));
		mtds2.put("m3", new HashSet<>(Arrays.asList("p2", "p3")));
		mtds2.put("m4", new HashSet<>(Arrays.asList("p1")));
		mtds2.put("m5", new HashSet<>(Arrays.asList("p1")));
		
		test(mtds1, mtds2, greaterThanOrEqualTo());
		
		System.out.println("\nA5b - Test Sparse\n");
		
		mtds1 = new HashMap<>();

		mtds1.put("m1", new HashSet<>(Arrays.asList("p1", "p3")));
		mtds1.put("m2", new HashSet<>(Arrays.asList("p1", "p2")));
		mtds1.put("m3", new HashSet<>(Arrays.asList("p2", "p3")));
		mtds1.put("m4", new HashSet<>(Arrays.asList("p1")));
		mtds1.put("m5", new HashSet<>(Arrays.asList("p1")));
		mtds1.put("m6", new HashSet<>(Arrays.asList("p1")));
		
		mtds2 = new HashMap<>();

		mtds2.put("m1", new HashSet<>(Arrays.asList("p1", "p3")));
		mtds2.put("m2", new HashSet<>(Arrays.asList("p1", "p2")));
		mtds2.put("m3", new HashSet<>(Arrays.asList("p2", "p3")));
		mtds2.put("m4", new HashSet<>(Arrays.asList("p1")));
		mtds2.put("m5", new HashSet<>(Arrays.asList("p1")));
		mtds2.put("m6", new HashSet<>(Arrays.asList("p1")));
		mtds2.put("m7", new HashSet<>(Arrays.asList("p1")));
		
		test(mtds1, mtds2, greaterThanOrEqualTo());
	}
}
