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
import br.com.bhansen.metric.Metric;
import br.com.bhansen.utils.Jaccard;

public class ICClass extends DeclarationMetricClass {
	
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
	
	public ICClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}
	
	@Override
	final public double getMetric() throws Exception {
		return icClass(this, getMethods(), createMMWeight(), createPPWeight());
	}
	
	protected MMWeight createMMWeight() {
		return new MMWeight(){};
	}
	
	protected PPWeight createPPWeight() {
		return new PPWeight(){};
	}
	
	public static double icClass(Metric instance, Map<String, Set<String>> methods, MMWeight mmWeight, PPWeight ppWeight) {
		double r = 0;
		
		if (methods.size() < 2)
			return 0;

		for (Entry<String, Set<String>> method : methods.entrySet()) {
			Map<String, Set<String>> mtdsCopy = new HashMap<>(methods);
			mtdsCopy.remove(method.getKey());
			
			r = r + icMethod(instance, method.getValue(), mtdsCopy, mmWeight, ppWeight);
		}

		return r / methods.size();
	}
	
	public static double icMethod(Metric instance, Set<String> method, Map<String, Set<String>> methods, MMWeight mmWeight, PPWeight ppWeight) {
		double mm = mm(instance, method, methods, mmWeight);
		
		if(mm == 0) {
			return 0;
		}
		
		double pp = pp(instance, method, methods, ppWeight);
		
		return ((mm + pp) / 2);
	}
	
	public static double mm(Metric instance, Map<String, Set<String>> methods, MMWeight mmWeight) {
		double r = 0;
		
		if (methods.size() < 2)
			return 0;

		for (Entry<String, Set<String>> method : methods.entrySet()) {
			Map<String, Set<String>> mtdsCopy = new HashMap<>(methods);
			mtdsCopy.remove(method.getKey());
			
			r = r + mm(instance, method.getValue(), mtdsCopy, mmWeight);
		}

		return r / methods.size();
	}
	
	public static double mm(Metric instance, Set<String> method, Map<String, Set<String>> methods, MMWeight mmWeight) {
		return Jaccard.similarity(instance, method, methods.values(), mmWeight);
	}
	
	public static double pp(Metric instance, Map<String, Set<String>> methods, PPWeight ppWeight) {
		double r = 0;
		
		if (methods.size() < 2)
			return 0;

		for (Entry<String, Set<String>> method : methods.entrySet()) {
			Map<String, Set<String>> mtdsCopy = new HashMap<>(methods);
			mtdsCopy.remove(method.getKey());
			
			r = r + pp(instance, method.getValue(), mtdsCopy, ppWeight);
		}

		return r / methods.size();
	}

	public static double pp(Metric instance, Set<String> method, Map<String, Set<String>> methods, PPWeight ppWeight) {
		
		if(method.size() == 0)
			return 0;
		
		double pp = 0;		
		
		Map<String, Set<String>> mP = AbsMetric.transpose(methods);
		
		for (String k : method) {
			Map<String, Set<String>> mPC = new HashMap<>(mP);
			Set<String> p = mPC.remove(k);

			if (p != null) {
				pp += Jaccard.similarity(instance, p, mPC.values(), ppWeight);
			}

		}
		
		return pp / method.size();
	}

}
