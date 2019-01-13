package br.com.bhansen.metric;

import br.com.bhansen.utils.Type;

public abstract class MetricFactory {
	
	private boolean skipIUC = false;
	
	public boolean skipIUC() {
		return skipIUC;
	}

	public Metric create(Type type) throws Exception {
		return create(type, null);
	}
		
	public Metric create(Type type, String method) throws Exception {
		return create(type, method, this.skipIUC);
	}
	
	public Metric create(Type type, String method, boolean skipIUC) throws Exception {
		this.skipIUC = skipIUC;
		return create(type, method, null, skipIUC);
	}
	
	public Metric create(Type type, String method, String parameter) throws Exception {
		return create(type, method, parameter, this.skipIUC);
	}
	
	public abstract Metric create(Type type, String method, String parameter, boolean skipIUC) throws Exception;

}
