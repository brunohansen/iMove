package br.com.bhansen.metric.iuc;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.metric.UsageMetricMethod;
import br.com.bhansen.utils.Jaccard;
import br.com.bhansen.utils.Type;

public class IUCJMethod extends UsageMetricMethod {

	public IUCJMethod(Type type, String method, IProgressMonitor monitor) throws Exception {
		super(type, method, monitor);
	}

	public double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		
//		return Jaccard.similarity(method, methods.values());
		return Jaccard.biSimilarity(method, methods);

	}

}
