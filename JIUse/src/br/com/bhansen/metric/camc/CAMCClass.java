package br.com.bhansen.metric.camc;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.DeclarationMetricClass;

public class CAMCClass extends DeclarationMetricClass {
		
	public CAMCClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}

	@Override
	public double getMetric() throws Exception {
		return getMetric(getMethods(), getValues().size());
	}
	
	public static double getMetric(Map<String, Set<String>> methods, double numValues) {
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
