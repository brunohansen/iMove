package br.com.bhansen.metric.iscom;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.metric.UsageMetricMethod;
import br.com.bhansen.utils.Type;

public class UISCOMMethod extends UsageMetricMethod {

	public UISCOMMethod(Type type, String method, IProgressMonitor monitor) throws Exception {
		super(type, method, monitor);
	}

	@Override
	public double getMetric(Set<String> mi, Map<String, Set<String>> methods) {
		return ISCOMMethod.getMetric(mi, methods, getValues().size());
	}

}
