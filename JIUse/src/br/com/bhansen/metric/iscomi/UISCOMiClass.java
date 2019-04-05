package br.com.bhansen.metric.iscomi;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.UsageMetricClass;

public class UISCOMiClass extends UsageMetricClass {
	
	protected UISCOMiClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}
	
	@Override
	public double getMetric() throws Exception {
		return ISCOMiClass.iscomClass(getMethods(), getValues().size());
	}

}
