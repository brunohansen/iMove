package br.com.bhansen.metric.iscom;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.DeclarationMetricMethod;

public class ISCOMMethod extends DeclarationMetricMethod {

	public ISCOMMethod(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
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

	@Override
	public double getMetric(Set<String> mi, Map<String, Set<String>> methods) {
		return getMetric(mi, methods, getValues().size());
	}
	
	public static double getMetric(Set<String> mi, Map<String, Set<String>> methods, int a) {
		double r = 0;
				
		for (Entry<String, Set<String>> mj : methods.entrySet()) {
			r = r + (c(mi, mj.getValue()) * w(mi, mj.getValue(), a));
		}
		
		return r / methods.size();
	}

}
