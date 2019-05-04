package br.com.bhansen.metric;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class NHDMinMaxTest extends MetricTest {
	
	@Test
	public void testNHDMinMax() {
		System.out.println("\nA5b - Test NHD min\n");
		
		HashMap<String, Set<String>> min = new HashMap<>();
		
		min.put("m1", new HashSet<>(Arrays.asList("p1", "p2", "p3", "p4", "p5", "p6")));
		min.put("m2", new HashSet<>(Arrays.asList("p1", "p2")));
		min.put("m3", new HashSet<>());
		min.put("m4", new HashSet<>());
		min.put("m5", new HashSet<>());
		min.put("m6", new HashSet<>());
		min.put("m7", new HashSet<>());
		
//		System.out.println("MM " +	
//		WICClass.mm(min, WICClass.createMMWeight(AbsMetric.uniqueValues(min).size())) +
//		" -> " +
//		WICClass.mm(min, WICClass.createMMWeight(AbsMetric.uniqueValues(min).size()))); 
//		
//		System.out.println("PP " +	
//		WICClass.pp(min, WICClass.createPPWeight(min.size())) +
//		" -> " +
//		WICClass.pp(min, WICClass.createPPWeight(min.size())));
		
		print(min);
		
		System.out.println("\nA5b - Test NHD min 2\n");
		
		HashMap<String, Set<String>> min2 = new HashMap<>();
		
		min2.put("m1", new HashSet<>(Arrays.asList("p1")));
		min2.put("m2", new HashSet<>(Arrays.asList("p2")));
		min2.put("m3", new HashSet<>(Arrays.asList("p3")));
		min2.put("m4", new HashSet<>(Arrays.asList("p4")));
		min2.put("m5", new HashSet<>(Arrays.asList("p5")));
		min2.put("m6", new HashSet<>(Arrays.asList("p1", "p6")));
		min2.put("m7", new HashSet<>(Arrays.asList("p4")));
		
//		System.out.println("MM " +	
//		WICClass.mm(min2, WICClass.createMMWeight(AbsMetric.uniqueValues(min2).size())) +
//		" -> " +
//		WICClass.mm(min2, WICClass.createMMWeight(AbsMetric.uniqueValues(min2).size()))); 
//		
//		System.out.println("PP " +	
//		WICClass.pp(min2, WICClass.createPPWeight(min2.size())) +
//		" -> " +
//		WICClass.pp(min2, WICClass.createPPWeight(min2.size())));
		
		print(min2);
		
		System.out.println("\nA5b - Test NHD max 1\n");
		
		HashMap<String, Set<String>> max1 = new HashMap<>();
	
		max1.put("m1", new HashSet<>(Arrays.asList("p1", "p2", "p3", "p4", "p5", "p6")));
		max1.put("m2", new HashSet<>(Arrays.asList("p1")));
		max1.put("m3", new HashSet<>(Arrays.asList("p1")));
		max1.put("m4", new HashSet<>());
		max1.put("m5", new HashSet<>());
		max1.put("m6", new HashSet<>());
		max1.put("m7", new HashSet<>());
		
		print(max1);
		
		System.out.println("\nA5b - Test NHD max 2\n");
		
		HashMap<String, Set<String>> max2 = new HashMap<>();
		
		max2.put("m1", new HashSet<>());
		max2.put("m2", new HashSet<>(Arrays.asList("p2", "p3", "p4", "p5")));
		max2.put("m3", new HashSet<>(Arrays.asList("p1", "p2")));
		max2.put("m4", new HashSet<>());
		max2.put("m5", new HashSet<>(Arrays.asList("p6")));
		max2.put("m6", new HashSet<>(Arrays.asList("p2")));
		max2.put("m7", new HashSet<>());
		
		print(max2);
		
	}

}
