package br.com.bhansen.metric;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

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

public class MetricTest {
	
	public interface TestPredicate<T> extends Predicate<T> {
		
		public String getText(T t);
	}
	
	@Test
	public void test100() {
		System.out.println("\nTest 100%\n");
		
		Map<String, Set<String>> mtds = new HashMap<>();
		
		mtds.put("m1", new HashSet<>(Arrays.asList("p1", "p2")));
		mtds.put("m2", new HashSet<>(Arrays.asList("p1", "p2")));
		
		testEquals(mtds, AbsMetric.uniqueValues(mtds), 1);
		
		mtds = new HashMap<>();

		mtds.put("m1", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		mtds.put("m2", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		mtds.put("m3", new HashSet<>(Arrays.asList("p1", "p2", "p3")));
		
		testEquals(mtds, AbsMetric.uniqueValues(mtds), 1);
	}
	
	@Test
	public void testMonotonocity() {
		System.out.println("\nTest Monotonocity\n");
		
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
		
		testEqualsOrGreater(mtds1, AbsMetric.uniqueValues(mtds1), mtds2, AbsMetric.uniqueValues(mtds2));
	}
	
	public void testEquals(Map<String, Set<String>> methods, Set<String> values, double result) {
		checkThat("CAMC", result, equalTo(CAMCClass.getMetric(methods, values.size())));
		checkThat("CCi", result, equalTo(CCiClass.cci(methods)));
		checkThat("IC", result, equalTo(ICClass.icClass(methods, new MMWeight(){}, new PPWeight(){})));
		checkThat("ISCOMi", result, equalTo(ISCOMiClass.iscomClass(methods, values.size())));
		checkThat("NHD", result, equalTo(NHDClass.nhdClass(methods, values, NHDClass.NHD)));
		checkThat("NHDM", result, equalTo(NHDMClass.nhdClass(methods, values, NHDMClass.NHDM)));
		checkThat("WIC", result, equalTo(WICClass.icClass(methods, WICClass.createMMWeight(values.size()), WICClass.createPPWeight(methods.size()))));
	}
	
	public void testEqualsOrGreater(Map<String, Set<String>> methods1, Set<String> values1, Map<String, Set<String>> methods2, Set<String> values2) {
		checkThat("CAMC", CAMCClass.getMetric(methods1, values1.size()), lessThanOrEqualTo(CAMCClass.getMetric(methods2, values2.size())));
		checkThat("CCi", CCiClass.cci(methods1), lessThanOrEqualTo(CCiClass.cci(methods2)));
		checkThat("IC", ICClass.icClass(methods1, new MMWeight(){}, new PPWeight(){}), lessThanOrEqualTo(ICClass.icClass(methods2, new MMWeight(){}, new PPWeight(){})));
		checkThat("ISCOMi", ISCOMiClass.iscomClass(methods1, values1.size()), lessThanOrEqualTo(ISCOMiClass.iscomClass(methods2, values2.size())));
		checkThat("NHD", NHDClass.nhdClass(methods1, values1, NHDClass.NHD), lessThanOrEqualTo(NHDClass.nhdClass(methods2, values2, NHDClass.NHD)));
		checkThat("NHDM", NHDMClass.nhdClass(methods1, values1, NHDMClass.NHDM), lessThanOrEqualTo(NHDMClass.nhdClass(methods2, values2, NHDMClass.NHDM)));
		checkThat("WIC", WICClass.icClass(methods1, WICClass.createMMWeight(values1.size()), WICClass.createPPWeight(methods1.size())), lessThanOrEqualTo(WICClass.icClass(methods2, WICClass.createMMWeight(values2.size()), WICClass.createPPWeight(methods2.size()))));
	}
	
	public <T> void checkThat(String text, T value, TestPredicate<T> predicate) {
		if(predicate.test(value)) {
			System.out.println("PASS " + text + " " + predicate.getText(value));
		} else {
			System.out.println("FAIL " + text + " " + predicate.getText(value));
		}
	}
	
	public <T> TestPredicate<T> equalTo(T t2) {
		return new TestPredicate<T>(){

			@Override
			public boolean test(T t) {
				return t.equals(t2);
			}

			@Override
			public String getText(T t) {
				return t + " == " + t2;
			}
			
		};
	}
	
	public <T extends Number> TestPredicate<T> lessThanOrEqualTo(T t2) {
		return new TestPredicate<T>(){

			@Override
			public boolean test(T t) {
				return t.doubleValue() <= t2.doubleValue();
			}

			@Override
			public String getText(T t) {
				return t + " <= " + t2;
			}
			
		};
	}

}
