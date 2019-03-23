package br.com.bhansen.metric;

import java.util.Map;
import java.util.Set;

import br.com.bhansen.config.Config;

public class CompositeMetric implements Metric {
		
	private Metric usa;
	private Metric dec;
	
	public CompositeMetric(UsageMetric usa, DeclarationMetric dec) throws Exception {
		this.usa = usa;
		this.dec = dec;
	}
	
	@Override
	public double getMetric() throws Exception {
		double iucM = usa.getMetric();
		double decM = dec.getMetric();
		
		return (iucM * Config.getMucWeight()) + (decM * Config.getMdcWeight());
	}

	@Override
	public String getName() {
		return usa.getName();
	}
	
	@Override
	public String toString() {
		return "Usage Metric: \n\n" + usa.toString() + "\n\n Data Metric: \n\n" + dec.toString();
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
