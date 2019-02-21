package br.com.bhansen.metric.camc;

import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.metric.DeclarationMetric;
import br.com.bhansen.utils.Method;
import br.com.bhansen.utils.MethodWithParameters;
import br.com.bhansen.utils.Type;

public class CAMCJMethod extends DeclarationMetric {

	private Set<String> method;

	public CAMCJMethod(Type type, String method, String parameter) throws Exception {
		super(type);

		dontUseVisibilityLevels(type, method, parameter);
	}
	
	private void dontUseVisibilityLevels(Type type, String method, String parameter) throws JavaModelException, Exception {
		IMethod[] iMethods = type.getIType().getMethods();

		for (IMethod iMethod : iMethods) {
			
			Method m = new Method(iMethod);

			if (m.isMethod(method)) {
				this.method = m.getMethodWithParameters(parameter).getParameters();
			} else {
				
				// Add only public
//				if(! m.isPublic())
//					continue;
				
				// Dont add private
//				if (m.isPrivate())
//					continue;
				
				// Dont add constructor
//				if (m.isConstructor())
//					continue;				
				
				MethodWithParameters mp = m.getMethodWithParameters();
				
				// Dont add zero parameters
//				if(! mp.hasParameter())
//					continue;

				getMethods().put(mp.getSignature(), mp.getParameters());
			}

		}
	}
	
	private void useVisibilityLevels(Type type, String method, String parameter) throws JavaModelException, Exception {
		
		Method tMethod = type.getMethod(method);
		
		this.method = tMethod.getMethodWithParameters(parameter).getParameters();
		
		IMethod[] iMethods = type.getIType().getMethods();

		for (IMethod iMethod : iMethods) {
			
			Method m = new Method(iMethod);

			if (! m.isMethod(method)) {
								
				// Dont add constructor
				if (m.isConstructor())
					continue;
								
				// Add only same visibility
				if(! tMethod.hasSameVisibility(m))
					continue;
				
				// Add only same o higher visibility
//				if(! tMethod.hasVisibility(m))
//					continue;
				
				MethodWithParameters mp = m.getMethodWithParameters();
				
				// Dont add zero parameters
//				if(! mp.hasParameter())
//					continue;

				getMethods().put(mp.getSignature(), mp.getParameters());
			}

		}
	}

	@Override
	public double getMetric() throws Exception {
		double metric = 0;
		
		if (method.size() == 0)
			return 1;

		if (getMethods().size() == 0)
			return 0;

		for (Entry<String, Set<String>> entry : getMethods().entrySet()) {

			metric += AbsMetric.howMuchIntersect(method, entry.getValue());

		}

		metric = metric / getMethods().size();

		return metric;
	}

}
