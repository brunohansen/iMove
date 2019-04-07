package br.com.bhansen.metric.wic;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.ic.ICClass.MMWeight;
import br.com.bhansen.metric.ic.ICClass.PPWeight;
import br.com.bhansen.metric.ic.UICClass;

public class UWICClass extends UICClass {

	public UWICClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}

	@Override
	protected MMWeight createMMWeight() {
		return WICClass.createMMWeight(getValues().size());
	}
	
	@Override
	protected PPWeight createPPWeight() {
		return WICClass.createPPWeight(getMethods().size());
	}

}
