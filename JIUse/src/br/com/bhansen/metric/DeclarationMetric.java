package br.com.bhansen.metric;

import java.util.HashSet;
import java.util.Set;

import br.com.bhansen.utils.Type;

public abstract class DeclarationMetric extends AbsMetric {

	public DeclarationMetric(Type type) {
		super(type);
	}
	
	protected final Set<String> getParams() {
		Set<String> params = new HashSet<>();

		for (Set<String> ps : getMethods().values()) {
			params.addAll(ps);
		}

		return params;
	}
	
}
