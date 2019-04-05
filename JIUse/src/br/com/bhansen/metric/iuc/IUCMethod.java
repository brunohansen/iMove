package br.com.bhansen.metric.iuc;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.UsageMetricMethod;
import br.com.bhansen.metric.camc.CAMCMethod;

public class IUCMethod extends UsageMetricMethod {

	public IUCMethod(Type type, String method, IProgressMonitor monitor) throws Exception {
		super(type, method, monitor);
	}

	@Override
	public double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		return CAMCMethod.getMetric(method, methods, getValues().size());
	}
			
}
