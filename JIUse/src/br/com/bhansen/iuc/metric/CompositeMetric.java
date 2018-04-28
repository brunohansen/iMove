package br.com.bhansen.iuc.metric;

import org.eclipse.jdt.core.IType;

public class CompositeMetric implements Metric {
	
	private CAMCJClass camc;
	private IUCClass iuc;
	
	public CompositeMetric(IType type, String fakeDelegate, String fakeParameter) throws Exception {
		camc = new CAMCJClass(type, false, fakeDelegate, fakeParameter);
		iuc = new IUCClass(type, fakeDelegate);
	}

	@Override
	public double getMetric() throws Exception {
		return camc.getMetric() + iuc.getMetric();
	}

}
