package br.com.bhansen.metric.cci;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.metric.DeclarationMetricClass;
import br.com.bhansen.utils.Jaccard;
import br.com.bhansen.utils.Type;

public class CCiClass extends DeclarationMetricClass {

	public CCiClass(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}

	@Override
	public double getMetric() throws Exception {

		if (getMethods().size() < 2)
			return 0;

		return cci(getMethods());
	}

	public static double cci(Map<String, Set<String>> mtds) {
		double r = 0;

		for (Entry<String, Set<String>> s1 : mtds.entrySet()) {
			Map<String, Set<String>> mtdsCopy = new HashMap<>(mtds);
			mtdsCopy.remove(s1.getKey());
			r = r + Jaccard.similarity(s1.getValue(), mtdsCopy.values());
		}

		return r / mtds.size();
	}

	public static void main(String[] args) {
		Map<String, Set<String>> mtds = new HashMap<>();

		mtds.put("m1", new HashSet<>(Arrays.asList("p1")));
		mtds.put("m2", new HashSet<>(Arrays.asList("p1")));
		mtds.put("m3", new HashSet<>(Arrays.asList("p1")));
		mtds.put("m4", new HashSet<>(Arrays.asList("p2", "p3", "p4")));

		System.out.println(cci(mtds));
	}

}
