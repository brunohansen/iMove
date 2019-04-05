package br.com.bhansen.metric.cci;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.UsageMetricMethod;
import br.com.bhansen.utils.Jaccard;

public class UCCiMethod extends UsageMetricMethod {

	public UCCiMethod(Type type, String method, IProgressMonitor monitor) throws Exception {
		super(type, method, monitor);
	}

	public double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		return Jaccard.similarity(method, methods.values());
	}

}
