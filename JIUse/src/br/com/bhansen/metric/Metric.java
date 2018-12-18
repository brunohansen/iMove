package br.com.bhansen.metric;

import org.eclipse.jdt.core.IType;

public interface Metric {
	
	public String getName();
	public double getMetric() throws Exception;
	public double getMetric(boolean skipIUC) throws Exception;
	
	public boolean isPublicMethod();
	public boolean hasNoCaller();
	public boolean isCalledOnlyBy(IType type);

}
