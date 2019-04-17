package br.com.bhansen.metric.ic;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.UsageMetricClass;
import br.com.bhansen.metric.ic.ICClass.MMWeight;
import br.com.bhansen.metric.ic.ICClass.PPWeight;

public class UICClass extends UsageMetricClass {

	public UICClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}

	@Override
	final public double getMetric() throws Exception {
		return ICClass.icClass(this, getMethods(), createMMWeight(), createPPWeight());
	}
	
	protected MMWeight createMMWeight() {
		return new MMWeight(){};
	}
	
	protected PPWeight createPPWeight() {
		return new PPWeight(){};
	}

}
