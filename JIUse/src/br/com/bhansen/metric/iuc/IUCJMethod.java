package br.com.bhansen.metric.iuc;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;

import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.utils.Method;
import br.com.bhansen.utils.MethodWithCallers;
import br.com.bhansen.utils.Type;

public class IUCJMethod extends IUC {

	private Set<String> method;

	public IUCJMethod(Type type, String method) throws Exception {
		super(type);

		IMethod[] iMethods = type.getIType().getMethods();

		for (IMethod iMethod : iMethods) {
			
			Method m = new Method(iMethod);

			if (m.isMethod(method) || m.isMovedMethod(method)) {
				MethodWithCallers mc = m.getMethodWithCallers();
				
				//Remove fake public
//				mc.removeCaller(type);
				
				this.method = mc.getCallers();

			} else {
				
				// Dont add constructor
				if (m.isConstructor())
					continue;
				
				// Dont add private
				if (m.isPrivate())
					continue;
				
				MethodWithCallers mc = m.getMethodWithCallers();

				// Dont add not called
//				if (! mc.hasCaller())
//					continue;
				
				// Dont add fake public
//				if(mc.isCalledOnlyBy(type))
//					continue;
				
				//Remove fake public
//				mc.removeCaller(type);
				
				if (getMethods().put(mc.getSignature(), mc.getCallers()) != null) {
					System.out.println("Method " + mc.getSignature() + " colision!");
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
