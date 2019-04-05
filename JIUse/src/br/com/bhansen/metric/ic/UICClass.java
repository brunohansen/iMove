package br.com.bhansen.metric.ic;

import java.util.Set;
import java.util.function.BiFunction;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.UsageMetricClass;
import br.com.bhansen.utils.Jaccard;

public class UICClass extends UsageMetricClass {

	public UICClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}

	@Override
	final public double getMetric() throws Exception {
		return ICClass.icClass(getMethods(), createWeight());
	}
	
	protected BiFunction<Set<String>, Set<String>, Double> createWeight() {
		return Jaccard.NO_WEIGHT;
	}

}
