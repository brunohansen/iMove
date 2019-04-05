package br.com.bhansen.metric.camc;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.metric.DeclarationMetricMethod;
import br.com.bhansen.utils.Type;

public class CAMCMethod extends DeclarationMetricMethod {
		
	public CAMCMethod(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}

	@Override
	public double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		return getMetric(method, methods, getValues().size());
	}
	
	public static double getMetric(Set<String> method, Map<String, Set<String>> methods, int numValues) {
		double camc = 0;
		double numMethods = methods.size();
		
		camc += method.size() / numValues;
		
		camc = camc / numMethods;
		
		return camc;
	}

}
