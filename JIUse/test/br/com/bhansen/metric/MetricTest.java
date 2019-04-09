package br.com.bhansen.metric;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import br.com.bhansen.metric.camc.CAMCClass;
import br.com.bhansen.metric.cci.CCiClass;
import br.com.bhansen.metric.ic.ICClass;
import br.com.bhansen.metric.ic.ICClass.MMWeight;
import br.com.bhansen.metric.ic.ICClass.PPWeight;
import br.com.bhansen.metric.iscomi.ISCOMiClass;
import br.com.bhansen.metric.nhd.NHDClass;
import br.com.bhansen.metric.nhdm.NHDMClass;
import br.com.bhansen.metric.wic.WICClass;
import br.com.bhansen.test.TestCase;

public class MetricTest extends TestCase {

//	@Test
	public void test100() {
		System.out.println("\nTest 100%\n");
		
		Map<String, Set<String>> mtds = new HashMap<>();
		
		mtds.put("m1", new HashSet<>(Arrays.asList("p1", "p2")));
		mtds.put("m2", new HashSet<>(Arrays.asList("p1", "p2")));
		
		test(mtds, AbsMetric.uniqueValues(mtds), 1.0, equalTo());
		
		mtds = new HashMap<>();

		mtds.put("m1", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		mtds.put("m2", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		mtds.put("m3", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		
		test(mtds, AbsMetric.uniqueValues(mtds), 1.0, equalTo());
	}
	
//	@Test
	public void testZero() {
		System.out.println("\nA1 e A3 - Test Zero\n");
		
		Map<String, Set<String>> mtds1 = new HashMap<>();

		mtds1.put("m1", new HashSet<>(Arrays.asList("p1")));
		mtds1.put("m2", new HashSet<>(Arrays.asList("p2")));
		
		test(mtds1, AbsMetric.uniqueValues(mtds1), 0.0, equalTo());
		
		Map<String, Set<String>> mtds2 = new HashMap<>();

		mtds2.put("m1", new HashSet<>(Arrays.asList("p1")));
		mtds2.put("m2", new HashSet<>(Arrays.asList("p2")));
		mtds2.put("m3", new HashSet<>(Arrays.asList("p3")));
		
		test(mtds2, AbsMetric.uniqueValues(mtds2), 0.0, equalTo());
	}
	
	@Test
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
		
		test(mtds1, AbsMetric.uniqueValues(mtds1), mtds2, AbsMetric.uniqueValues(mtds2), notEqualTo());
	}
	
//	@Test
	public void testMonotonocity1() {
		System.out.println("\nVMA e VMB - Test Monotonocity\n");
		
		Map<String, Set<String>> mtds1 = new HashMap<>();

		mtds1.put("m1", new HashSet<>(Arrays.asList("p1")));
		mtds1.put("m2", new HashSet<>(Arrays.asList("p1")));
		mtds1.put("m3", new HashSet<>(Arrays.asList("p1")));
		mtds1.put("m4", new HashSet<>(Arrays.asList("p2", "p3", "p4")));
		
		Map<String, Set<String>> mtds2 = new HashMap<>();

		mtds2.put("m1", new HashSet<>(Arrays.asList("p1")));
		mtds2.put("m2", new HashSet<>(Arrays.asList("p1")));
		mtds2.put("m3", new HashSet<>(Arrays.asList("p1", "p2")));
		mtds2.put("m4", new HashSet<>(Arrays.asList("p2", "p3", "p4")));
		
		test(mtds1, AbsMetric.uniqueValues(mtds1), mtds2, AbsMetric.uniqueValues(mtds2), lessThanOrEqualTo());
	}
	
	public void test(Map<String, Set<String>> methods, Set<String> values, Double expected, TestPredicateFactory<Double> factory) {
		checkThat("CAMC", expected, CAMCClass.getMetric(methods, values.size()), factory);
		checkThat("CCi", expected, CCiClass.cci(methods), factory);
		checkThat("IC", expected, ICClass.icClass(methods, new MMWeight(){}, new PPWeight(){}), factory);
		checkThat("ISCOMi", expected, ISCOMiClass.iscomClass(methods, values.size()), factory);
		checkThat("NHD", expected, NHDClass.nhdClass(methods, values, NHDClass.NHD), factory);
		checkThat("NHDM", expected, NHDMClass.nhdClass(methods, values, NHDMClass.NHDM), factory);
		checkThat("WIC", expected, WICClass.icClass(methods, WICClass.createMMWeight(values.size()), WICClass.createPPWeight(methods.size())), factory);
	}
	
	public void test(Map<String, Set<String>> methods1, Set<String> values1, Map<String, Set<String>> methods2, Set<String> values2, TestPredicateFactory<Double> factory) {
		checkThat("CAMC", CAMCClass.getMetric(methods1, values1.size()), CAMCClass.getMetric(methods2, values2.size()), factory);
		checkThat("CCi", CCiClass.cci(methods1), CCiClass.cci(methods2), factory);
		checkThat("IC", ICClass.icClass(methods1, new MMWeight(){}, new PPWeight(){}), ICClass.icClass(methods2, new MMWeight(){}, new PPWeight(){}), factory);
		checkThat("ISCOMi", ISCOMiClass.iscomClass(methods1, values1.size()), ISCOMiClass.iscomClass(methods2, values2.size()), factory);
		checkThat("NHD", NHDClass.nhdClass(methods1, values1, NHDClass.NHD), NHDClass.nhdClass(methods2, values2, NHDClass.NHD), factory);
		checkThat("NHDM", NHDMClass.nhdClass(methods1, values1, NHDMClass.NHDM), NHDMClass.nhdClass(methods2, values2, NHDMClass.NHDM), factory);
		checkThat("WIC", WICClass.icClass(methods1, WICClass.createMMWeight(values1.size()), WICClass.createPPWeight(methods1.size())), WICClass.icClass(methods2, WICClass.createMMWeight(values2.size()), WICClass.createPPWeight(methods2.size())), factory);
	}
	
}
