package br.com.bhansen.iuc.metric;

import org.eclipse.jdt.core.IType;

public interface MetricFactory {
	
	Metric create(IType type) throws Exception;

}
