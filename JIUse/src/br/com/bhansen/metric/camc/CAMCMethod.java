package br.com.bhansen.metric.camc;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.DeclarationMetricMethod;

public class CAMCMethod extends DeclarationMetricMethod {
		
	public CAMCMethod(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}

	@Override
	public double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		return camcMethod(method, methods);
	}
	
	public static double camcMethod(Set<String> method, Map<String, Set<String>> methods) {		
		Set<String> values = uniqueValues(methods);
		values.addAll(method);
		
		if(values.size() == 0)
			return (double) 0.0;
		
		return (double) method.size() / (double) values.size();
	}

}
