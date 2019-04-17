package br.com.bhansen.metric.iscomi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.config.Config;
import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.DeclarationMetricClass;
import br.com.bhansen.metric.Metric;

public class ISCOMiClass extends DeclarationMetricClass {
	
	public ISCOMiClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}
	
	@Override
	public double getMetric() throws Exception {
		return iscomClass(this, getMethods(), getValues().size());
	}
	
	public static double iscomClass(Metric instance, Map<String, Set<String>> methods, double numValues) {
		double r = 0;
		
		if (methods.size() < 2)
			return 0;

		for (Entry<String, Set<String>> method : methods.entrySet()) {
			Map<String, Set<String>> mtdsCopy = new HashMap<>(methods);
			mtdsCopy.remove(method.getKey());
			r = r + iscomMethod(instance, method.getValue(), mtdsCopy, numValues);
		}

		return r / methods.size();
	}

	public static double iscomMethod(Metric instance, Set<String> mi, Map<String, Set<String>> methods, double a) {
		double r = 0;
				
		for (Entry<String, Set<String>> mj : methods.entrySet()) {
			//Metódos sem parametros possuem 100% coesão 
			if(Config.isMetricTight(instance) && mi.size() == 0 && mj.getValue().size() == 0) {
				r = r + 1;
			} else {
				r = r + (c(mi, mj.getValue()) * w(mi, mj.getValue(), a));
			}
		}
		
		return r / (double) methods.size();
	}
	
	public static double c(Set<String> i, Set<String> j) {
		Set<String> intersection = new HashSet<>(i);
		intersection.retainAll(j);
		
		if(i.size() + j.size() == 0) {
			return 0;
		}
		
		return (2.0 * (double) intersection.size()) / ((double) i.size() + (double) j.size());
	}
	
	public static double w(Set<String> i, Set<String> j, double a) {
		Set<String> union = new HashSet<>(i);
		union.addAll(j);
		
		return (double) union.size() / a;
	}

}
