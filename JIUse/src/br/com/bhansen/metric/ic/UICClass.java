package br.com.bhansen.metric.ic;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.UsageMetricClass;
import br.com.bhansen.utils.Jaccard;

public class UICClass extends UsageMetricClass {

	public UICClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}

	@Override
	public double getMetric() throws Exception {
		return ICClass.icClass(getMethods(), Jaccard.NO_WEIGHT);
	}
	
	

}
