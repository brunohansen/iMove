package br.com.bhansen.metric;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.utils.Type;

public abstract class MetricFactory {
	
	private boolean skipIUC = false;
	
	public boolean skipIUC() {
		return skipIUC;
	}

	public Metric create(Type type, IProgressMonitor monitor) throws Exception {
		return create(type, null, monitor);
	}
		
	public Metric create(Type type, String method, IProgressMonitor monitor) throws Exception {
		return create(type, method, this.skipIUC, monitor);
	}
	
	public Metric create(Type type, String method, boolean skipIUC, IProgressMonitor monitor) throws Exception {
		this.skipIUC = skipIUC;
		return create(type, method, null, skipIUC, monitor);
	}
	
	public Metric create(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		return create(type, method, parameter, this.skipIUC, monitor);
	}
	
	public abstract Metric create(Type type, String method, String parameter, boolean skipIUC, IProgressMonitor monitor) throws Exception;

}
