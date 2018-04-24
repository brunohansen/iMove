package br.com.bhansen.iuc.metric;

public interface Metric {
	
	public double getMetric() throws Exception;
	public double getMetric(String fakeDelegate, String fakeParameter) throws Exception;

}
