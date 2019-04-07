package br.com.bhansen.metric.wic;

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
		return WICClass.createMMWeight(getMethod(), getValues());
	}
	
	@Override
	protected PPWeight createPPWeight() {
		return WICClass.createPPWeight(getMethods().size() + 1);
	}

}
