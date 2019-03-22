package br.com.bhansen.metric;

import java.util.Set;

import br.com.bhansen.utils.Type;

public abstract class DeclarationMetric extends AbsMetric {

	public DeclarationMetric(Type type) {
		super(type);
	}
	
	protected final Set<String> getParams() {
		return uniqueValues(getMethods());
	}
	
}
