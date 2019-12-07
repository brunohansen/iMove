package br.com.bhansen.metric.ic;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiFunction;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.metric.DeclarationMetricClass;
import br.com.bhansen.utils.Jaccard;

public class ICClass extends DeclarationMetricClass {
	
	public ICClass(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}

	public interface MMWeight extends BiFunction<Set<String>, Set<String>, Double> {
		
		@Override
		default Double apply(Set<String> t, Set<String> u) {
			return 1.0;
		}
		
	}
	
	public interface PPWeight extends BiFunction<Set<String>, Set<String>, Double> {
		
		@Override
		default Double apply(Set<String> t, Set<String> u) {
			return 1.0;
		}
		
	}
	
	@Override
	final public double getMetric() throws Exception {
		return icClass(getMethods(), createMMWeight(), createPPWeight());
	}
	
	protected MMWeight createMMWeight() {
		return new MMWeight(){};
	}
	
	protected PPWeight createPPWeight() {
		return new PPWeight(){};
	}
	
	public static double icClass(Map<String, Set<String>> methods, MMWeight mmWeight, PPWeight ppWeight) {
		double r = 0;
		
		if (methods.size() < 2)
			return 0;

		for (Entry<String, Set<String>> method : methods.entrySet()) {
			Map<String, Set<String>> mtdsCopy = new HashMap<>(methods);
			mtdsCopy.remove(method.getKey());
			
			r = r + icMethod(method.getValue(), mtdsCopy, mmWeight, ppWeight);
		}

		return r / methods.size();
	}
	
	protected static double icMethod(Set<String> method, Map<String, Set<String>> methods, MMWeight mmWeight, PPWeight ppWeight) {
		double mm = mm(method, methods, mmWeight);
		
		if(mm == 0) {
			return 0;
		}
		
		double pp = pp(method, methods, ppWeight);
		
		return ((mm + pp) / 2);
	}
	
	public static double mm(Map<String, Set<String>> methods, MMWeight mmWeight) {
		double r = 0;
		
		if (methods.size() < 2)
			return 0;

		for (Entry<String, Set<String>> method : methods.entrySet()) {
			Map<String, Set<String>> mtdsCopy = new HashMap<>(methods);
			mtdsCopy.remove(method.getKey());
			
			r = r + mm(method.getValue(), mtdsCopy, mmWeight);
		}

		return r / methods.size();
	}
	
	private static double mm(Set<String> method, Map<String, Set<String>> methods, MMWeight mmWeight) {
		return Jaccard.similarity(method, methods.values(), mmWeight);
	}
	
	public static double pp(Map<String, Set<String>> methods, PPWeight ppWeight) {
		double r = 0;
		
		if (methods.size() < 2)
			return 0;

		for (Entry<String, Set<String>> method : methods.entrySet()) {
			Map<String, Set<String>> mtdsCopy = new HashMap<>(methods);
			mtdsCopy.remove(method.getKey());
			
			r = r + pp(method.getValue(), mtdsCopy, ppWeight);
		}

		return r / methods.size();
	}

	private static double pp(Set<String> method, Map<String, Set<String>> methods, PPWeight ppWeight) {
		
		if(method.size() == 0)
			return 0;
		
		double pp = 0;		
		
		Map<String, Set<String>> mP = AbsMetric.transpose(methods);
		
		for (String k : method) {
			Map<String, Set<String>> mPC = new HashMap<>(mP);
			Set<String> p = mPC.remove(k);

			if (p != null) {
				pp += Jaccard.similarity(p, mPC.values(), ppWeight);
			}

		}
		
		return pp / method.size();
	}

}
