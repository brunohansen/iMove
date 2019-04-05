package br.com.bhansen.metric.camc;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.metric.DeclarationMetricClass;
import br.com.bhansen.utils.Type;

public class CAMCClass extends DeclarationMetricClass {
		
	public CAMCClass(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}

	@Override
	public double getMetric() throws Exception {
		return getMetric(getMethods(), getParams().size());
	}
	
	public static double getMetric(Map<String, Set<String>> methods, int numValues) {
		double camc = 0;
		double numMethods = methods.size();
		
		if((numMethods == 0) || (numValues == 0)) {
			return 0.0f;
		}
		
		for (Set<String> ps : methods.values()) {
			camc += ps.size() / numValues;
		}
		
		camc = camc / numMethods;
		
		return camc;
	}

}
