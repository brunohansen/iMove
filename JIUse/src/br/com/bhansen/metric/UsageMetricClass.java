package br.com.bhansen.metric;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.IMethod;

import br.com.bhansen.config.Config;
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
				
			// Add only public
//			if(! m.isPublic())
//				continue;
			
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
//			mc.removeCaller(type);
			
			if (getMethods().put(mc.getSignature(), mc.getCallers()) != null) {
				Console.println("Method " + mc.getSignature() + " colision!");
			}
		}
	}
}
