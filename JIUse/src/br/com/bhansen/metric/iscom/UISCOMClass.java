package br.com.bhansen.metric.iscom;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.UsageMetricClass;

public class UISCOMClass extends UsageMetricClass {
	
	protected UISCOMClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}
	
	@Override
	public double getMetric() throws Exception {
		return ISCOMClass.iscomClass(getMethods(), getValues().size());
	}

}
