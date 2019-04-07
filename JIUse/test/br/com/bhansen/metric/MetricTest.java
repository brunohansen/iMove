package br.com.bhansen.metric;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

	public static void main(String[] args) {
		Map<String, Set<String>> mtds = new HashMap<>();

		mtds.put("m1", new HashSet<>(Arrays.asList("p1")));
		mtds.put("m2", new HashSet<>(Arrays.asList("p1")));
		mtds.put("m3", new HashSet<>(Arrays.asList("p1")));
		mtds.put("m4", new HashSet<>(Arrays.asList("p2", "p3", "p4")));

		testClassMetric(mtds, AbsMetric.uniqueValues(mtds));
	}
	
	public static void testClassMetric(Map<String, Set<String>> methods, Set<String> values) {System.out.println("CAMC -> " + CAMCClass.getMetric(methods, values.size()));
		System.out.println("CCi -> " + CCiClass.cci(methods));
		System.out.println("IC -> " + ICClass.icClass(methods, new MMWeight(){}, new PPWeight(){}));
		System.out.println("ISCOMi -> " + ISCOMiClass.iscomClass(methods, values.size()));
		System.out.println("NHD -> " + NHDClass.nhdClass(methods, values, NHDClass.NHD));
		System.out.println("NHDM -> " + NHDMClass.nhdClass(methods, values, NHDMClass.NHDM));
		System.out.println("WIC -> " + WICClass.icClass(methods, WICClass.createMMWeight(values.size()), WICClass.createPPWeight(methods.size())));
	}

}
