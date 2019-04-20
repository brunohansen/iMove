package br.com.bhansen.metric;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import br.com.bhansen.metric.camc.CAMCClass;
import br.com.bhansen.metric.camc.CAMCMethod;
import br.com.bhansen.metric.cci.CCiClass;
import br.com.bhansen.metric.cci.CCiMethod;
import br.com.bhansen.metric.d3c2i.D3C2iClass;
import br.com.bhansen.metric.ic.ICClass;
import br.com.bhansen.metric.ic.ICClass.MMWeight;
import br.com.bhansen.metric.ic.ICClass.PPWeight;
import br.com.bhansen.metric.ic.ICMethod;
import br.com.bhansen.metric.iscomi.ISCOMiClass;
import br.com.bhansen.metric.iscomi.ISCOMiMethod;
import br.com.bhansen.metric.nhd.NHDClass;
import br.com.bhansen.metric.nhd.NHDMethod;
import br.com.bhansen.metric.nhdm.NHDMClass;
import br.com.bhansen.metric.nhdm.NHDMMethod;
import br.com.bhansen.metric.wic.WICClass;
import br.com.bhansen.metric.wic.WICMethod;
import br.com.bhansen.test.TestCase;

public class MetricTest extends TestCase {

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
	
//	@Test
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
	
//	@Test
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
	
	@Test
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
	
	public void test(Map<String, Set<String>> methods, Double expected, TestPredicateFactory<Double> factory) {
		checkThat("CAMC", expected, CAMCClass.camcClass(methods), factory);
		checkThat("CCi", expected, CCiClass.cciClass(methods), factory);
		checkThat("D3C2i", expected, D3C2iClass.d3c2iClass(methods), factory);
		checkThat("IC", expected, ICClass.icClass(methods, new MMWeight(){}, new PPWeight(){}), factory);
		checkThat("ISCOMi", expected, ISCOMiClass.iscomClass(methods), factory);
		checkThat("NHD", expected, NHDClass.nhdClass(methods, NHDClass.NHD), factory);
		checkThat("NHDM", expected, NHDMClass.nhdClass(methods, NHDMClass.NHDM), factory);
		checkThat("WIC", expected, WICClass.icClass(methods, WICClass.createMMWeight(methods), WICClass.createPPWeight(methods)), factory);
	}
	
	public void test(Map<String, Set<String>> methods1, Map<String, Set<String>> methods2, TestPredicateFactory<Double> factory) {
		checkThat("CAMC", CAMCClass.camcClass(methods1), CAMCClass.camcClass(methods2), factory);
		checkThat("CCi", CCiClass.cciClass(methods1), CCiClass.cciClass(methods2), factory);
		checkThat("D3C2i", D3C2iClass.d3c2iClass(methods1), D3C2iClass.d3c2iClass(methods2), factory);
		checkThat("IC", ICClass.icClass(methods1, new MMWeight(){}, new PPWeight(){}), ICClass.icClass(methods2, new MMWeight(){}, new PPWeight(){}), factory);
		checkThat("ISCOMi", ISCOMiClass.iscomClass(methods1), ISCOMiClass.iscomClass(methods2), factory);
		checkThat("NHD", NHDClass.nhdClass(methods1, NHDClass.NHD), NHDClass.nhdClass(methods2, NHDClass.NHD), factory);
		checkThat("NHDM", NHDMClass.nhdClass(methods1, NHDMClass.NHDM), NHDMClass.nhdClass(methods2, NHDMClass.NHDM), factory);
		checkThat("WIC", WICClass.icClass(methods1, WICClass.createMMWeight(methods1), WICClass.createPPWeight(methods1)), WICClass.icClass(methods2, WICClass.createMMWeight(methods2), WICClass.createPPWeight(methods2)), factory);
	}
	
	public void print(Map<String, Set<String>> methods) {
		System.out.println("CAMC -> " + CAMCClass.camcClass(methods));
		System.out.println("CCi -> " + CCiClass.cciClass(methods));
		System.out.println("D3C2i -> " + D3C2iClass.d3c2iClass(methods));
		System.out.println("IC -> " + ICClass.icClass(methods, new MMWeight(){}, new PPWeight(){}));
		System.out.println("ISCOMi -> " + ISCOMiClass.iscomClass(methods));
		System.out.println("NHD -> " + NHDClass.nhdClass(methods, NHDClass.NHD));
		System.out.println("NHDM -> " + NHDMClass.nhdClass(methods, NHDMClass.NHDM));
		System.out.println("WIC -> " + WICClass.icClass(methods, WICClass.createMMWeight(methods), WICClass.createPPWeight(methods)));
	}
	
	public void print(Set<String> method, Map<String, Set<String>> methods) {
		System.out.println("CAMC -> " + CAMCMethod.camcMethod(method, methods));
		System.out.println("CCi -> " + CCiMethod.cciMethod(method, methods));
		//System.out.println("D3C2i -> " + D3C2iClass.getMetric(methods));
		System.out.println("IC -> " + ICMethod.icMethod(method, methods, new MMWeight(){}, new PPWeight(){}));
		System.out.println("ISCOMi -> " + ISCOMiMethod.iscomMethod(method, methods));
		System.out.println("NHD -> " + NHDMethod.nhdMethod(method, methods, NHDClass.NHD));
		System.out.println("NHDM -> " + NHDMMethod.nhdMethod(method, methods, NHDMClass.NHDM));
		System.out.println("WIC -> " + WICMethod.icMethod(method, methods, WICMethod.createMMWeight(method, methods), WICMethod.createPPWeight(method, methods)));
	}
	
}
