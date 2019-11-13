package br.com.bhansen.metric.ic;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Method;
import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.DeclarationMetricMethod;
import br.com.bhansen.metric.ic.ICClass.MMWeight;
import br.com.bhansen.metric.ic.ICClass.PPWeight;

public class ICMethod extends DeclarationMetricMethod {

	public ICMethod(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}
	
	public ICMethod(Type type, Method method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}

	@Override
	final public double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		return icMethod(getMethod(), getMethods(), createMMWeight(), createPPWeight());
	}
	
	public static double icMethod(Set<String> method, Map<String, Set<String>> methods, MMWeight mmWeight, PPWeight ppWeight) {
		return ICClass.icMethod(method, methods, mmWeight, ppWeight);
	}

	protected MMWeight createMMWeight() {
		return new MMWeight(){};
	}
	
	protected PPWeight createPPWeight() {
		return new PPWeight(){};
	}

}
