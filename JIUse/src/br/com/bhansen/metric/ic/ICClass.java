package br.com.bhansen.metric.ic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiFunction;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.metric.DeclarationMetricClass;
import br.com.bhansen.utils.Jaccard;

public class ICClass extends DeclarationMetricClass {
	
	protected ICClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}
	
	@Override
	public double getMetric() throws Exception {
		return icClass(getMethods(), Jaccard.NO_WEIGHT);
	}
	
	public static double icClass(Map<String, Set<String>> methods, BiFunction<Set<String>, Set<String>, Double> weight) {
		double r = 0;
		
		if (methods.size() < 2)
			return 0;

		for (Entry<String, Set<String>> method : methods.entrySet()) {
			Map<String, Set<String>> mtdsCopy = new HashMap<>(methods);
			mtdsCopy.remove(method.getKey());
			r = r + icMethod(method.getValue(), mtdsCopy, weight);
		}

		return r / methods.size();
	}

	public static double icMethod(Set<String> method, Map<String, Set<String>> methods, BiFunction<Set<String>, Set<String>, Double> weight) {
		
		double mm = Jaccard.similarity(method, methods.values());

		if (mm == 0) {
			return 0;
		}
		
		double mp = 0;		
		
		Map<String, Set<String>> mP = AbsMetric.invert(methods);
		
		for (String k : method) {
			Map<String, Set<String>> mPC = new HashMap<>(mP);
			Set<String> p = mPC.remove(k);

			if (p != null) {
				mp += Jaccard.similarity(p, mPC.values(), weight);
			}

		}
		
		mp = mp / method.size();
		
		return (mm + mp) / 2;
	}

	protected static BiFunction<Set<String>, Set<String>, Double> getWeight(double values) {
		return new BiFunction<Set<String>, Set<String>, Double>() {
			
			@Override
			public Double apply(Set<String> m1, Set<String> m2) {
				Set<String> union = new HashSet<>(m1);
				union.addAll(m2);
				
				return union.size() / values;
			}
		};
	}	

}
