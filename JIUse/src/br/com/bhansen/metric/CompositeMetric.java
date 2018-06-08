package br.com.bhansen.metric;

import org.eclipse.jdt.core.IType;

import br.com.bhansen.metric.camc.CAMCClass;
import br.com.bhansen.metric.iuc.IUCClass;

public class CompositeMetric implements Metric {
	
	private CAMCClass one;
	private IUCClass two;
	
	public CompositeMetric(IType type, String method, String parameter) throws Exception {
		one = new CAMCClass(type, true, method, parameter);
		two = new IUCClass(type, method);
	}

	@Override
	public double getMetric() throws Exception {
		return one.getMetric() + two.getMetric();
	}

}
