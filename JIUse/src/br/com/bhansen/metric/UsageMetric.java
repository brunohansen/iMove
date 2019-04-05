package br.com.bhansen.metric;

import java.util.Set;

import br.com.bhansen.utils.Type;

public abstract class UsageMetric extends AbsMetric {

	public UsageMetric(Type type) {
		super(type);
	}
	
	protected final Set<String> getClients() {
		return uniqueValues(getMethods());
	}

}
