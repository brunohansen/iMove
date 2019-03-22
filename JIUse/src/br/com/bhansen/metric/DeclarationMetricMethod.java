package br.com.bhansen.metric;

import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import br.com.bhansen.config.Config;
import br.com.bhansen.utils.Method;
import br.com.bhansen.utils.MethodWithParameters;
import br.com.bhansen.utils.Type;

public abstract class DeclarationMetricMethod extends DeclarationMetric {
	
	private Set<String> method;

	public DeclarationMetricMethod(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type);

		dontUseVisibilityLevels(type, method, parameter, monitor);
	}
	
	public Set<String> getMethod() {
		return method;
	}
	
	private void dontUseVisibilityLevels(Type type, String method, String parameter, IProgressMonitor monitor) throws JavaModelException, Exception {
		IMethod[] iMethods = type.getIType().getMethods();
		
		SubMonitor subMonitor = SubMonitor.convert(monitor, iMethods.length);

		for (IMethod iMethod : iMethods) {
			subMonitor.split(1).done();
			
			Method m = new Method(iMethod);

			if (m.isMethod(method)) {
				this.method = m.getMethodWithParameters(parameter).getParameters();
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
	
}
