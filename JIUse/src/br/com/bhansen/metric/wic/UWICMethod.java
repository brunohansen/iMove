package br.com.bhansen.metric.wic;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.ic.ICClass.MMWeight;
import br.com.bhansen.metric.ic.ICClass.PPWeight;
import br.com.bhansen.metric.ic.UICMethod;

public class UWICMethod extends UICMethod {

	public UWICMethod(Type type, String method, IProgressMonitor monitor) throws Exception {
		super(type, method, monitor);
	}
	
	@Override
	protected MMWeight createMMWeight() {
		return WICMethod.createMMWeight(getMethod(), getMethods());
	}
	
	@Override
	protected PPWeight createPPWeight() {
		return WICMethod.createPPWeight(getMethod(), getMethods());
	}

}
