package br.com.bhansen.metric.cci;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.DeclarationMetricMethod;
import br.com.bhansen.utils.Jaccard;

public class CCiMethod extends DeclarationMetricMethod {

	public CCiMethod(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}
	
	@Override
	public double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		return Jaccard.similarity(getMethod(), getMethods().values());
	}

}
