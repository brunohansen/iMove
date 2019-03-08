package br.com.bhansen.metric;

import java.util.Map;
import java.util.Set;

import br.com.bhansen.config.Config;
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
		double iucM = iuc.getMetric();
		double decM = dec.getMetric();
		
		return (iucM * Config.getMucWeight()) + (decM * Config.getMdcWeight());
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

	@Override
	public Map<String, Set<String>> getMethods() {
		throw new UnsupportedOperationException();
	}

}
