package br.com.bhansen.metric.iscomi;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.DeclarationMetricMethod;

public class ISCOMiMethod extends DeclarationMetricMethod {

	public ISCOMiMethod(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}

	@Override
	public double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		return ISCOMiClass.iscomMethod(method, methods, getValues().size());
	}

}
