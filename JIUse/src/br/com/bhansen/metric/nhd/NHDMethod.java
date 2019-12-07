package br.com.bhansen.metric.nhd;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.DeclarationMetricMethod;
import br.com.bhansen.utils.OccMtrx;

public class NHDMethod extends DeclarationMetricMethod {

	public NHDMethod(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}

	@Override
	final public double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		return nhdMethod(method, methods, getPredicate());
	}
	
	public static double nhdMethod(Set<String> method, Map<String, Set<String>> methods, BiPredicate<Boolean, Boolean> predicate) {
		Set<String> valuesCopy = new HashSet<>(uniqueValues(methods));
		valuesCopy.addAll(method);
		
		return NHDClass.nhdMethod(OccMtrx.createOccArray(method, valuesCopy), OccMtrx.createOccMtrx(methods, valuesCopy), predicate);
	}
	
	protected BiPredicate<Boolean, Boolean> getPredicate() {
		return NHDClass.NHD;
	}
	
}
