package br.com.bhansen.iuc.metric;

public interface Metric {
	
	public float getMetric() throws Exception;
	public float getMetric(String fakeDelegate, String fakeParameter) throws Exception;

}
