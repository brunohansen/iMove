package br.com.bhansen.metric;

import org.eclipse.jdt.core.IType;

public abstract class MetricFactory {
	
	private boolean skipIUC = false;
	
	public boolean skipIUC() {
		return skipIUC;
	}

	public Metric create(IType type) throws Exception {
		return create(type, this.skipIUC);
	}
	
	public Metric create(IType type, boolean skipIUC) throws Exception {
		return create(type, null, skipIUC);
	}
	
	public Metric create(IType type, String method, boolean skipIUC) throws Exception {
		this.skipIUC = skipIUC;
		return create(type, method, null, skipIUC);
	}
	
	public Metric create(IType type, String method, String parameter) throws Exception {
		return create(type, method, parameter, this.skipIUC);
	}
	
	public abstract Metric create(IType type, String method, String parameter, boolean skipIUC) throws Exception;

}
