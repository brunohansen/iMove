package br.com.bhansen.metric;

import org.eclipse.jdt.core.IType;

import br.com.bhansen.metric.camc.CAMCJMethod;
import br.com.bhansen.metric.iuc.IUCJMethod;

public class CompositeMetric implements Metric {
	
	private Metric one;
	private Metric two;
	
	public CompositeMetric(IType type, String method, String parameter) throws Exception {
		one = new CAMCJMethod(type, true, method, parameter);
		two = new IUCJMethod(type, method);
	}

	@Override
	public double getMetric() throws Exception {
		return one.getMetric() + two.getMetric();
	}

}
