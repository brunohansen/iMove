package br.com.bhansen.metric.cci;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.metric.UsageMetricClass;
import br.com.bhansen.utils.Type;

public class UCCiClass extends UsageMetricClass {

	public UCCiClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}

	@Override
	public double getMetric() throws Exception {
		return CCiClass.cci(getMethods());
	}



}
