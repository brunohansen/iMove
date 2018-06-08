package br.com.bhansen.metric;

import org.eclipse.jdt.core.IType;

public abstract class MetricFactory {
	
	public Metric create(IType type) throws Exception {
		return create(type, null, null);
	}
	
	public abstract Metric create(IType type, String method, String fakeParameter) throws Exception;

}
