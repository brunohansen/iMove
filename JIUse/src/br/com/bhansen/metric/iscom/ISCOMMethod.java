package br.com.bhansen.metric.iscom;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.DeclarationMetricMethod;

public class ISCOMMethod extends DeclarationMetricMethod {

	public ISCOMMethod(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}

	@Override
	public double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		return ISCOMClass.iscomMethod(method, methods, getValues().size());
	}

}
