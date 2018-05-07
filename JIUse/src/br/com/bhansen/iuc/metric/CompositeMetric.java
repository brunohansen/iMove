package br.com.bhansen.iuc.metric;

import org.eclipse.jdt.core.IType;

public class CompositeMetric implements Metric {
	
	private CAMCJClass one;
	private IUCClass two;
	
	public CompositeMetric(IType type, String fakeDelegate, String fakeParameter) throws Exception {
		one = new CAMCJClass(type, true, fakeDelegate, fakeParameter);
		two = new IUCClass(type, fakeDelegate);
	}

	@Override
	public double getMetric() throws Exception {
		return one.getMetric() + two.getMetric();
	}

}
