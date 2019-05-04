package br.com.bhansen.metric;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class LatticesGroupTest extends MetricTest {
	
	@Test
	public void testLatices() {
		Map<String, Set<String>> partition = new HashMap<>();

		partition.put("m1", new HashSet<>(Arrays.asList("p1")));
		partition.put("m2", new HashSet<>(Arrays.asList("p2")));
		partition.put("m3", new HashSet<>(Arrays.asList("p2", "p3")));
		partition.put("m4", new HashSet<>(Arrays.asList("p4")));
		partition.put("m5", new HashSet<>(Arrays.asList("p4")));
		
		Map<String, Set<String>> umbrella = new HashMap<>();

		umbrella.put("m1", new HashSet<>(Arrays.asList("p1", "p2", "p3", "p4", "p5", "p6")));
		umbrella.put("m2", new HashSet<>(Arrays.asList("p2")));
		umbrella.put("m3", new HashSet<>(Arrays.asList("p3")));
		umbrella.put("m4", new HashSet<>(Arrays.asList("p4")));
		umbrella.put("m5", new HashSet<>(Arrays.asList("p5")));
		umbrella.put("m6", new HashSet<>(Arrays.asList("p6")));
		
		System.out.println("\n Partition to Umbrella \n");
		test(partition, umbrella, lessThanOrEqualTo());
		
		Map<String, Set<String>> mRange1 = new HashMap<>();

		mRange1.put("m1", new HashSet<>(Arrays.asList("p1")));
		mRange1.put("m2", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		mRange1.put("m3", new HashSet<>(Arrays.asList("p2", "p3", "p4")));
		mRange1.put("m4", new HashSet<>(Arrays.asList("p4")));
		
		System.out.println("\n Umbrella to Montain Range 1 \n");
		test(umbrella, mRange1, lessThanOrEqualTo());
		
		Map<String, Set<String>> mRange2 = new HashMap<>();

		mRange2.put("m1", new HashSet<>(Arrays.asList("p1", "p2")));
		mRange2.put("m2", new HashSet<>(Arrays.asList("p2", "p3")));
		mRange2.put("m3", new HashSet<>(Arrays.asList("p3", "p4")));
		mRange2.put("m4", new HashSet<>(Arrays.asList("p1", "p4")));
		
		System.out.println("\n Montain Range 1 to Montain Range 2 \n");
		test(mRange1, mRange2, lessThanOrEqualTo());
		
		Map<String, Set<String>> mRange3 = new HashMap<>();

		mRange3.put("m1", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		mRange3.put("m2", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		mRange3.put("m3", new HashSet<>(Arrays.asList("p1", "p2", "p3", "p4")));
		mRange3.put("m4", new HashSet<>(Arrays.asList("p4", "p5", "p6")));
		mRange3.put("m5", new HashSet<>(Arrays.asList("p4", "p5", "p6")));
		mRange3.put("m6", new HashSet<>(Arrays.asList("p4", "p5", "p6")));
		
		System.out.println("\n Montain Range 2 to Montain Range 3 \n");
		test(mRange2, mRange3, lessThanOrEqualTo());
		
		Map<String, Set<String>> iUmbrella = new HashMap<>();

		iUmbrella.put("m1", new HashSet<>(Arrays.asList("p1", "p2")));
		iUmbrella.put("m2", new HashSet<>(Arrays.asList("p2")));
		iUmbrella.put("m3", new HashSet<>(Arrays.asList("p2", "p3")));
		
		System.out.println("\n Montain Range 3 to Inverted Umbrella \n");
		test(mRange3, iUmbrella, lessThanOrEqualTo());
		
		Map<String, Set<String>> diamond = new HashMap<>();

		diamond.put("m1", new HashSet<>(Arrays.asList("p1", "p2", "p3", "p4", "p5")));
		diamond.put("m2", new HashSet<>(Arrays.asList("p1", "p4", "p5")));
		diamond.put("m3", new HashSet<>(Arrays.asList("p1", "p3", "p5")));
		diamond.put("m4", new HashSet<>(Arrays.asList("p1")));
		diamond.put("m5", new HashSet<>(Arrays.asList("p1","p3", "p4", "p5")));
		
		System.out.println("\n Inverted Umbrella to Diamond \n");
		test(iUmbrella, diamond, lessThanOrEqualTo());
		
		Map<String, Set<String>> chain1 = new HashMap<>();

		chain1.put("m1", new HashSet<>(Arrays.asList("p1")));
		chain1.put("m2", new HashSet<>(Arrays.asList("p1", "p2")));
		chain1.put("m3", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		
		System.out.println("\n Diamond to Chain 1 \n");
		test(diamond, chain1, lessThanOrEqualTo());
		
		Map<String, Set<String>> chain2 = new HashMap<>();

		chain2.put("m1", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		chain2.put("m2", new HashSet<>(Arrays.asList("p1", "p2")));
		
		System.out.println("\n Chain 1 to Chain 2 \n");
		test(chain1, chain2, lessThanOrEqualTo());
		
		Map<String, Set<String>> dot = new HashMap<>();

		dot.put("m1", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		dot.put("m2", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		
		System.out.println("\n Chain 2 to Dot \n");
		test(chain2, dot, lessThanOrEqualTo());
	}

}
