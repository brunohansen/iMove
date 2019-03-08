package br.com.bhansen.metric;

import java.util.Map;
import java.util.Set;

public interface Metric {
	
	public String getName();
	public double getMetric() throws Exception;
	
	public Map<String, Set<String>> getMethods();
	
	public String toDetailedString();

}
