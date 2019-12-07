package br.com.bhansen.metric.wic;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.ic.ICClass.MMWeight;
import br.com.bhansen.metric.ic.ICClass.PPWeight;
import br.com.bhansen.metric.ic.ICMethod;

public class WICMethod extends ICMethod {

	public WICMethod(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}

	@Override
	protected MMWeight createMMWeight() {
		return createMMWeight(getMethod(), getMethods());
	}
	
	@Override
	protected PPWeight createPPWeight() {
		return createPPWeight(getMethod(), getMethods());
	}
	
	public static PPWeight createPPWeight(Set<String> method, Map<String, Set<String>> methods) {
		return WICClass.createPPWeight(methods.size() + 1);
	}
	
	public static MMWeight createMMWeight(Set<String> method, Map<String, Set<String>> methods) {
		Set<String> vls = new HashSet<>(uniqueValues(methods));
		vls.addAll(method);
		
		return WICClass.createMMWeight(vls.size());
	}

}
