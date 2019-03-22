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

	@Override
	public double getMetric() throws Exception {
		return getMetric(this.getMethod(), this.getMethods());
	}

	public static double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		
		if (method.size() == 0)
			return 0;

		if (methods.size() == 0)
			return 0;
		
		return Jaccard.similarity(method, methods.values());
//		return Jaccard.biSimilarity(method, methods);

	}

}
