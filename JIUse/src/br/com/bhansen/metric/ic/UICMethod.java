package br.com.bhansen.metric.ic;

import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.UsageMetricMethod;
import br.com.bhansen.utils.Jaccard;

public class UICMethod extends UsageMetricMethod {

	public UICMethod(Type type, String method, IProgressMonitor monitor) throws Exception {
		super(type, method, monitor);
	}

	@Override
	final public double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		return ICClass.icMethod(getMethod(), getMethods(), createWeight());
	}
	
	protected BiFunction<Set<String>, Set<String>, Double> createWeight() {
		return Jaccard.NO_WEIGHT;
	}

}
