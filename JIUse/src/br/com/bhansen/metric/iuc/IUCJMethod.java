package br.com.bhansen.metric.iuc;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.IMethod;

import br.com.bhansen.config.Config;
import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.utils.Method;
import br.com.bhansen.utils.MethodWithCallers;
import br.com.bhansen.utils.Type;
import br.com.bhansen.view.Console;

public class IUCJMethod extends IUC {

	private Set<String> method;

	public IUCJMethod(Type type, String method, IProgressMonitor monitor) throws Exception {
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

	@Override
	public double getMetric() throws Exception {
		return getMetric(this.method, this.getMethods());
	}

	public static double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		double metric = 0;
		
		if (method.size() == 0)
			return 0;

		if (methods.size() == 0)
			return 0;

		for (Entry<String, Set<String>> entry : methods.entrySet()) {

			metric += AbsMetric.howMuchIntersect(method, entry.getValue());

		}

		metric = metric / methods.size();

		return metric;
	}

}
