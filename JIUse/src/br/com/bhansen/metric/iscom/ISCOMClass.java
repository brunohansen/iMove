package br.com.bhansen.metric.iscom;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.DeclarationMetricClass;

public class ISCOMClass extends DeclarationMetricClass {
	
	protected ISCOMClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}
	
	@Override
	public double getMetric() throws Exception {
		return iscomClass(getMethods(), getValues().size());
	}
	
	public static double iscomClass(Map<String, Set<String>> methods, int weight) {
		double r = 0;
		
		if (methods.size() < 2)
			return 0;

		for (Entry<String, Set<String>> method : methods.entrySet()) {
			Map<String, Set<String>> mtdsCopy = new HashMap<>(methods);
			mtdsCopy.remove(method.getKey());
			r = r + iscomMethod(method.getValue(), mtdsCopy, weight);
		}

		return r / methods.size();
	}

	public static double iscomMethod(Set<String> mi, Map<String, Set<String>> methods, int a) {
		double r = 0;
				
		for (Entry<String, Set<String>> mj : methods.entrySet()) {
			r = r + (c(mi, mj.getValue()) * w(mi, mj.getValue(), a));
		}
		
		return r / methods.size();
	}
	
	public static double c(Set<String> i, Set<String> j) {
		Set<String> intersection = new HashSet<>(i);
		intersection.retainAll(j);
		
		return (2 * intersection.size()) / (i.size() + j.size());
	}
	
	public static double w(Set<String> i, Set<String> j, int a) {
		Set<String> union = new HashSet<>(i);
		union.addAll(j);
		
		return union.size() / a;
	}

}
