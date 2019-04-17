package br.com.bhansen.metric.nhd;

import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.UsageMetricMethod;

public class UNHDMethod extends UsageMetricMethod {

	public UNHDMethod(Type type, String method, IProgressMonitor monitor) throws Exception {
		super(type, method, monitor);
	}

	@Override
	final public double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		return NHDClass.nhdMethod(this, getMethod(), getMethods(), getValues(), getPredicate());
	}
	
	protected BiPredicate<Boolean, Boolean> getPredicate() {
		return NHDClass.NHD;
	}
	
}
