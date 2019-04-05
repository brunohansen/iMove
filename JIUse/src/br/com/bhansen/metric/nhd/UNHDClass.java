package br.com.bhansen.metric.nhd;

import java.util.function.BiPredicate;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.metric.UsageMetricClass;
import br.com.bhansen.utils.Type;

public class UNHDClass extends UsageMetricClass {

	public UNHDClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}

	@Override
	final public double getMetric() throws Exception {
		return NHDClass.nhdClass(getMethods(), getValues(), getPredicate());
	}
	
	protected BiPredicate<Boolean, Boolean> getPredicate() {
		return NHDClass.NHD;
	}
		
}
