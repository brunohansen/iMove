package br.com.bhansen.metric;

import br.com.bhansen.metric.iuc.IUC;

public class CompositeMetric implements Metric {
	
	private static final double DEC_W = 0.5;
	private static final double IUC_W = 1.0 - DEC_W;
	
	private Metric iuc;
	private Metric dec;
	
	public CompositeMetric(IUC iuc, DeclarationMetric dec) throws Exception {
		this.iuc = iuc;
		this.dec = dec;
	}
	
	@Override
	public double getMetric() throws Exception {
		double iucM = iuc.getMetric();
		double decM = dec.getMetric();
		
		return (iucM * IUC_W) + (decM * DEC_W);
	}

	@Override
	public String getName() {
		return iuc.getName();
	}
	
	@Override
	public String toString() {
		return "Usage Metric: \n\n" + iuc.toString() + "\n\n Data Metric: \n\n" + dec.toString();
	}

	@Override
	public String toDetailedString() {
		return toString();
	}

}
