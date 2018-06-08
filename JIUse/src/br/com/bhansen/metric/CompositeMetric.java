package br.com.bhansen.metric;

import org.eclipse.jdt.core.IType;

import br.com.bhansen.metric.camc.CAMCClass;
import br.com.bhansen.metric.iuc.IUCClass;

public class CompositeMetric implements Metric {
	
	private CAMCClass one;
	private IUCClass two;
	
	public CompositeMetric(IType type, String fakeDelegate, String fakeParameter) throws Exception {
		one = new CAMCClass(type, true, fakeDelegate, fakeParameter);
		two = new IUCClass(type, fakeDelegate);
	}

	@Override
	public double getMetric() throws Exception {
		return one.getMetric() + two.getMetric();
	}

}
