package br.com.bhansen.metric;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.IMethod;

import br.com.bhansen.config.DataMetricConfig;
import br.com.bhansen.config.MetricConfig;
import br.com.bhansen.jdt.Method;
import br.com.bhansen.jdt.MethodWithParameters;
import br.com.bhansen.jdt.Type;

public abstract class DeclarationMetricMethod extends DeclarationMetric {
	
	private Set<String> method;
	
	public DeclarationMetricMethod(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type);

		IMethod[] iMethods = type.getIType().getMethods();
		
		SubMonitor subMonitor = SubMonitor.convert(monitor, iMethods.length);

		for (IMethod iMethod : iMethods) {
			subMonitor.split(1).done();
			
			Method m = new Method(iMethod);

			if (m.isMethod(method)) {
				this.method = m.getMethodWithParameters(parameter).getParameters();
			} else {
				
				if(! MetricConfig.use(m))
					continue;

				MethodWithParameters mp = m.getMethodWithParameters();

				if(! DataMetricConfig.use(mp))
					continue;

				getMethods().put(mp.getSignature(), mp.getParameters());
			}

		}
	}
	
	public Set<String> getMethod() {
		return method;
	}
	
	public Set<String> getAllParams() {
		return uniqueValues(getMethod(), getMethods());
	}
	
	public Set<String> getMethodsParams() {
		return uniqueValues(getMethods());
	}
		
	@Override
	public final double getMetricValue() throws Exception {
		
		if (this.getMethod().size() == 0)
			return 1;

		if (this.getMethods().size() == 0)
			return 0;
		
		if(uniqueValues(getMethods()).size() == 0)
			return 0;
		
		return getMetric(this.getMethod(), this.getMethods());
	}
	
	public abstract double getMetric(Set<String> method, Map<String, Set<String>> methods);
	
}
