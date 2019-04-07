package br.com.bhansen.metric.ic;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.UsageMetricMethod;
import br.com.bhansen.metric.ic.ICClass.MMWeight;
import br.com.bhansen.metric.ic.ICClass.PPWeight;

public class UICMethod extends UsageMetricMethod {

	public UICMethod(Type type, String method, IProgressMonitor monitor) throws Exception {
		super(type, method, monitor);
	}

	@Override
	final public double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		return ICClass.icMethod(getMethod(), getMethods(), createMMWeight(), createPPWeight());
	}
	
	protected MMWeight createMMWeight() {
		return new MMWeight(){};
	}
	
	protected PPWeight createPPWeight() {
		return new PPWeight(){};
	}

}
