package br.com.bhansen.metric;

import java.util.Map;
import java.util.Set;

import br.com.bhansen.config.MoveMethodConfig;

public class CompositeMetric implements Metric {
		
	private String name;
	private double usa;
	private double dec;
	
	public CompositeMetric(UsageMetric usa, DeclarationMetric dec) throws Exception {
		this.name = usa.getName();
		
		this.usa = usa.getMetric() * MoveMethodConfig.getMucWeight();
		this.dec = dec.getMetric() * MoveMethodConfig.getMdcWeight();
	}
	
	@Override
	public double getMetric() throws Exception {
		return getUsageMetric() + getDeclarationMetric();
	}
	
	public double getUsageMetric() throws Exception {
		return usa;
	}
	
	public double getDeclarationMetric() throws Exception {
		return dec;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		try {
			return "Usage Metric: \n\n" + getUsageMetric() + "\n\n Data Metric: \n\n" + getDeclarationMetric();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
