package br.com.bhansen.metric;

import java.util.Map;
import java.util.Set;

public class MockMetric  implements Metric {

	@Override
	public double getMetric() throws Exception {
		return 0;
	}

	@Override
	public String getName() {
		return "Just check";
	}

	@Override
	public String toDetailedString() {
		return "Just check";
	}

	@Override
	public Map<String, Set<String>> getMethods() {
		return null;
	}
	
}
