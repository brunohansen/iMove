package br.com.bhansen.metric.camc;

import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.metric.DeclarationMetric;
import br.com.bhansen.utils.Method;
import br.com.bhansen.utils.MethodWithParameters;

public class CAMCJMethod extends DeclarationMetric {

	Set<String> method;

	public CAMCJMethod(IType type, String method, String parameter) throws Exception {
		super(type);

		IMethod[] iMethods = type.getMethods();

		for (IMethod iMethod : iMethods) {
			
			Method m = new Method(iMethod);

			if (m.isMethod(method) || m.isMovedMethod( method)) {
				this.method = m.getMethodWithParameters(parameter).getParameters();
			} else {
				
				// Dont add constructor
				if (m.isConstructor())
					continue;
				
				// Dont add private
				if (m.isPrivate())
					continue;
				
				MethodWithParameters mp = m.getMethodWithParameters(parameter);
				
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
