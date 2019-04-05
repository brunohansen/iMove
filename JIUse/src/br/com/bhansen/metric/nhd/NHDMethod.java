package br.com.bhansen.metric.nhd;

import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.metric.DeclarationMetricMethod;
import br.com.bhansen.utils.Type;

public class NHDMethod extends DeclarationMetricMethod {

	public NHDMethod(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}

	@Override
	final public double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		return NHDClass.nhdMethod(getMethod(), getMethods(), getValues(), getPredicate());
	}
	
	protected BiPredicate<Boolean, Boolean> getPredicate() {
		return NHDClass.NHD;
	}
	
}
