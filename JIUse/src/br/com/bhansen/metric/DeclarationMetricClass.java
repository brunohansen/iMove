package br.com.bhansen.metric;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.IMethod;

import br.com.bhansen.utils.Method;
import br.com.bhansen.utils.MethodWithParameters;
import br.com.bhansen.utils.Type;

public abstract class DeclarationMetricClass extends DeclarationMetric {

	protected DeclarationMetricClass(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type);

		IMethod[] iMethods = type.getIType().getMethods();
		
		SubMonitor subMonitor = SubMonitor.convert(monitor, iMethods.length);

		for (IMethod iMethod : iMethods) {
			subMonitor.split(1).done();
			
			Method m = new Method(iMethod);
			
			MethodWithParameters mp = null;
					
			if (m.isMethod(method)) {
				mp = m.getMethodWithParameters(parameter);
			} else {
				mp = m.getMethodWithParameters();
			}

//			if ((m.isPrivate() || (m.isFakeDelegate(method)))
//				continue;
			
			// Dont add zero parameters
//			if(! mp.hasParameter())
//				continue;

			getMethods().put(mp.getSignature(), mp.getParameters());

		}
	}

}
