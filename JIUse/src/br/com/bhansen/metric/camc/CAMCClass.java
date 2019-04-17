package br.com.bhansen.metric.camc;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.config.Config;
import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.DeclarationMetricClass;
import br.com.bhansen.metric.Metric;

public class CAMCClass extends DeclarationMetricClass {
		
	public CAMCClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}

	@Override
	public double getMetric() throws Exception {
		return getMetric(this, getMethods(), getValues().size());
	}
	
	public static double getMetric(Metric instance, Map<String, Set<String>> methods, double numValues) {
		double camc = 0;
		double numMethods = methods.size();
		
		if(numMethods == 0) {
			return 0.0f;
		}
		
		if(numValues == 0) {
			//Metódos sem parametros possuem 100% coesão 
			if(Config.isMetricTight(instance)) {
				return 1;
			} else {
				return 0.0f;
			}
		}
		
		for (Set<String> ps : methods.values()) {
			//Metódos sem parametros possuem 100% coesão 
			if(Config.isMetricTight(instance) && ps.size() == 0) {
				camc += 1;
			} else {
				camc += ps.size() / numValues;
			}
		}
		
		camc = camc / numMethods;
		
		return camc;
	}

}
