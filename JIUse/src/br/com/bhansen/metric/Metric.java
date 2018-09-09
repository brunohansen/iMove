package br.com.bhansen.metric;

public interface Metric {
	
	public String getName();
	public double getMetric() throws Exception;

}
