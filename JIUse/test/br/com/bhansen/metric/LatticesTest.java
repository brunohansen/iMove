package br.com.bhansen.metric;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LatticesTest extends MetricTest {
	
//	@Test
	public void testLatices2() {
		Map<String, Set<String>> partition = new HashMap<>();

		partition.put("m2", new HashSet<>(Arrays.asList("p2", "p3", "p5", "p8")));
		partition.put("m3", new HashSet<>(Arrays.asList("p2", "p3", "p5")));
		partition.put("m4", new HashSet<>(Arrays.asList("p4", "p6")));
		partition.put("m5", new HashSet<>(Arrays.asList("p2", "p7")));
		partition.put("m6", new HashSet<>(Arrays.asList("p2")));
		partition.put("m7", new HashSet<>(Arrays.asList("p3")));
		partition.put("m8", new HashSet<>(Arrays.asList("p9")));
		
		Map<String, Set<String>> mRange1 = new HashMap<>();

		mRange1.put("m1", new HashSet<>(Arrays.asList("p3", "p5", "p6", "p7")));
		mRange1.put("m2", new HashSet<>(Arrays.asList("p1", "p2", "p4")));
		mRange1.put("m3", new HashSet<>(Arrays.asList("p1", "p2", "p5")));
		mRange1.put("m4", new HashSet<>(Arrays.asList("p1", "p4")));
		mRange1.put("m5", new HashSet<>(Arrays.asList("p1", "p2")));
		mRange1.put("m6", new HashSet<>(Arrays.asList("p3", "p6")));
		mRange1.put("m7", new HashSet<>(Arrays.asList("p7")));
		mRange1.put("m8", new HashSet<>(Arrays.asList("p3")));
		
		System.out.println("\n Partition to Montain Range \n");
		test(partition, mRange1, lessThanOrEqualTo());
		
		Map<String, Set<String>> umbrella = new HashMap<>();

		umbrella.put("m1", new HashSet<>(Arrays.asList("p2", "p3", "p4", "p5", "p6", "p7", "p8", "p9")));
		umbrella.put("m2", new HashSet<>(Arrays.asList("p2", "p3", "p5", "p8")));
		umbrella.put("m3", new HashSet<>(Arrays.asList("p2", "p3", "p5")));
		umbrella.put("m4", new HashSet<>(Arrays.asList("p4", "p6")));
		umbrella.put("m5", new HashSet<>(Arrays.asList("p2", "p7")));
		umbrella.put("m6", new HashSet<>(Arrays.asList("p2")));
		umbrella.put("m7", new HashSet<>(Arrays.asList("p3")));
		umbrella.put("m8", new HashSet<>(Arrays.asList("p9")));
		
		System.out.println("\n Montain Range to Umbrella \n");
		test(mRange1, umbrella, lessThanOrEqualTo());
		
		Map<String, Set<String>> iUmbrella = new HashMap<>();

		iUmbrella.put("m2", new HashSet<>(Arrays.asList("p1", "p2", "p3", "p5", "p8")));
		iUmbrella.put("m3", new HashSet<>(Arrays.asList("p1", "p2", "p3", "p5")));
		iUmbrella.put("m4", new HashSet<>(Arrays.asList("p1", "p4", "p6")));
		iUmbrella.put("m5", new HashSet<>(Arrays.asList("p1", "p2", "p7")));
		iUmbrella.put("m6", new HashSet<>(Arrays.asList("p1", "p2")));
		iUmbrella.put("m7", new HashSet<>(Arrays.asList("p1", "p3")));
		iUmbrella.put("m8", new HashSet<>(Arrays.asList("p1", "p9")));
		
		System.out.println("\n Umbrella to Inverted Umbrella \n");
		test(umbrella, iUmbrella, lessThanOrEqualTo());
		
		Map<String, Set<String>> diamond = new HashMap<>();

		diamond.put("m1", new HashSet<>(Arrays.asList("p1", "p2", "p3", "p4", "p5", "p6", "p7", "p8", "p9")));
		diamond.put("m2", new HashSet<>(Arrays.asList("p1", "p2", "p3", "p5", "p8")));
		diamond.put("m3", new HashSet<>(Arrays.asList("p1", "p2", "p3", "p5")));
		diamond.put("m4", new HashSet<>(Arrays.asList("p1", "p4", "p6")));
		diamond.put("m5", new HashSet<>(Arrays.asList("p1", "p2", "p7")));
		diamond.put("m6", new HashSet<>(Arrays.asList("p1", "p2")));
		diamond.put("m7", new HashSet<>(Arrays.asList("p1", "p3")));
		diamond.put("m8", new HashSet<>(Arrays.asList("p1", "p9")));
		
		System.out.println("\n Inverted Umbrella to Diamond \n");
		test(iUmbrella, diamond, lessThanOrEqualTo());
		
		Map<String, Set<String>> chain1 = new HashMap<>();

		chain1.put("m1", new HashSet<>(Arrays.asList("p1", "p2", "p3", "p4", "p5")));
		chain1.put("m2", new HashSet<>(Arrays.asList("p1", "p2", "p3", "p4")));
		chain1.put("m3", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		chain1.put("m4", new HashSet<>(Arrays.asList("p1", "p2")));
		chain1.put("m5", new HashSet<>(Arrays.asList("p1")));
		
		System.out.println("\n Diamond to Chain \n");
		test(diamond, chain1, lessThanOrEqualTo());
		
		Map<String, Set<String>> dot = new HashMap<>();

		dot.put("m1", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		dot.put("m2", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		dot.put("m3", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		
		System.out.println("\n Chain to Dot \n");
		test(chain1, dot, lessThanOrEqualTo());
	}
}
