package br.com.bhansen.metric.iscomi;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Method;
import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.UsageMetricMethod;

public class UISCOMiMethod extends UsageMetricMethod {


	public UISCOMiMethod(Type type, String method, IProgressMonitor monitor) throws Exception {
		super(type, method, monitor);
	}
	
	public UISCOMiMethod(Type type, Method method, IProgressMonitor monitor) throws Exception {
		super(type, method, monitor);
	}

	@Override
	public double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		return ISCOMiMethod.iscomMethod(method, methods);
	}

}
