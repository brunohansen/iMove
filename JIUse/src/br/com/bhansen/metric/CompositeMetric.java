package br.com.bhansen.metric;

import org.eclipse.jdt.core.IType;

import br.com.bhansen.metric.iuc.IUC;

public class CompositeMetric implements Metric {
	
	private Metric iuc;
	private Metric dec;
	
	public CompositeMetric(IUC iuc, DeclarationMetric dec) throws Exception {
		this.iuc = iuc;
		this.dec = dec;
	}
	
	@Override
	public double getMetric() throws Exception {
		return this.getMetric(false);
	}

	@Override
	public double getMetric(boolean skipIUC) throws Exception {
		if(skipIUC)
			return dec.getMetric();
		else
			return iuc.getMetric() + dec.getMetric();
	}

	@Override
	public String getName() {
		return iuc.getName();
	}

	@Override
	public boolean isPublicMethod() {
		return iuc.isPublicMethod();
	}

	@Override
	public boolean hasNoCaller() {
		return iuc.hasNoCaller();
	}

	@Override
	public boolean isCalledOnlyBy(IType type) {
		return iuc.isCalledOnlyBy(type);
	}

}
