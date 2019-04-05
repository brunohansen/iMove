package br.com.bhansen.metric;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.IMethod;

import br.com.bhansen.config.Config;
import br.com.bhansen.jdt.Method;
import br.com.bhansen.jdt.MethodWithCallers;
import br.com.bhansen.jdt.Type;
import br.com.bhansen.view.Console;

public abstract class UsageMetricMethod extends UsageMetric {
	
	private Set<String> method;

	public UsageMetricMethod(Type type, String method, IProgressMonitor monitor) throws Exception {
		super(type);

		IMethod[] iMethods = type.getIType().getMethods();
		
		SubMonitor subMonitor = SubMonitor.convert(monitor, iMethods.length);

		for (IMethod iMethod : iMethods) {
			subMonitor.split(1).done();
			
			Method m = new Method(iMethod);

			if (m.isMethod(method)) {
				MethodWithCallers mc = m.getMethodWithCallers();
				
				//Remove fake public
//				mc.removeCaller(type);
				
				this.method = mc.getCallers();

			} else {
				
				// Add only public
//				if(! m.isPublic())
//					continue;
				
				// Dont add private
				if (Config.isMetricTight() && m.isPrivate())
					continue;
				
				// Dont add constructor
				if (Config.isMetricTight() && m.isConstructor())
					continue;
								
				MethodWithCallers mc = m.getMethodWithCallers();

				// Dont add not called
				if (Config.isMetricTight() && ! mc.hasCaller())
					continue;
				
				// Dont add fake public
				if(Config.isMetricTight() && mc.isCalledOnlyBy(type))
					continue;
				
				//Remove fake public
//				mc.removeCaller(type);
				
				if (getMethods().put(mc.getSignature(), mc.getCallers()) != null) {
					Console.println("Method " + mc.getSignature() + " colision!");
				}
			}

		}
	}
	
	public Set<String> getMethod() {
		return method;
	}
	
	@Override
	public final double getMetric() throws Exception {
		
		if (this.getMethod().size() == 0)
			return 0;

		if (this.getMethods().size() == 0)
			return 0;
		
		if (this.getValues().size() == 0)
			return 0;
		
		return getMetric(this.getMethod(), this.getMethods());
	}
	
	public abstract double getMetric(Set<String> method, Map<String, Set<String>> methods);
}
