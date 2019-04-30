package br.com.bhansen.metric;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.IMethod;

import br.com.bhansen.config.MetricConfig;
import br.com.bhansen.config.UsageMetricConfig;
import br.com.bhansen.jdt.Method;
import br.com.bhansen.jdt.MethodWithCallers;
import br.com.bhansen.jdt.Type;
import br.com.bhansen.view.Console;

public abstract class UsageMetricClass extends UsageMetric {
	
	public UsageMetricClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type);
		
		IMethod[] iMethods = type.getIType().getMethods();
		
		SubMonitor subMonitor = SubMonitor.convert(monitor, iMethods.length);

		for (IMethod iMethod : iMethods) {
			subMonitor.split(1).done();
			
			Method m = new Method(iMethod);
				
			if(! MetricConfig.use(m))
				continue;
							
			MethodWithCallers mc = m.getMethodWithCallers();
			
			if(! UsageMetricConfig.use(mc, type))
				continue;
			
			//Remove fake public
			if(! UsageMetricConfig.useInternalCalls())
				mc.removeCaller(type);
			
			if (getMethods().put(mc.getSignature(), mc.getCallers()) != null) {
				Console.println("Method " + mc.getSignature() + " colision!");
			}
		}
	}
}
