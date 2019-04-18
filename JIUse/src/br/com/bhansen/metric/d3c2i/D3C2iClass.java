package br.com.bhansen.metric.d3c2i;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.metric.DeclarationMetricClass;
import br.com.bhansen.metric.nhd.NHDClass;

public class D3C2iClass extends DeclarationMetricClass {

	protected D3C2iClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}

	@Override
	public double getMetric() throws Exception {
		return getMetric(getMethods());
	}
	
	public static double getMetric(Map<String, Set<String>> methods) {
		double k = methods.size();
		double l = AbsMetric.uniqueValues(methods).size();
		
		double n = ((k * (k - 1) * mmac(methods)) + (l * (l - 1) * aac(methods)) + (2 * k * l * amc(methods)));
		double d = ((k * (k - 1)) + (l * (l - 1)) + (2 * k * l));
				
		return n / d;
	}
	
	public static double mmac(Map<String, Set<String>> methods) {
		return NHDClass.nhdClass(methods, AbsMetric.uniqueValues(methods), NHDClass.NHD);
	}
	
	public static double aac(Map<String, Set<String>> methods) {
		Map<String, Set<String>> parameters = AbsMetric.transpose(methods);
		
		return NHDClass.nhdClass(parameters, AbsMetric.uniqueValues(parameters), NHDClass.NHD);
	}
	
	public static double amc(Map<String, Set<String>> methods) {
		double k = methods.size();
		double l = AbsMetric.uniqueValues(methods).size();
		double p = 0;
		
		for (Entry<String, Set<String>> method : methods.entrySet()) {
			p = p + method.getValue().size();
		}
		
		return p / (k * l);
	}

}
