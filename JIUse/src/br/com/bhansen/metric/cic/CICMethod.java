package br.com.bhansen.metric.cic;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.metric.DeclarationMetricMethod;
import br.com.bhansen.utils.Jaccard;
import br.com.bhansen.utils.Type;

public class CICMethod extends DeclarationMetricMethod {

	public CICMethod(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}
	
	@Override
	public double getMetric(Set<String> method, Map<String, Set<String>> methods) {
//		return Jaccard.similarity(getMethod(), getMethods().values());
		return Jaccard.biSimilarity(getMethod(), getMethods());
	}

}
