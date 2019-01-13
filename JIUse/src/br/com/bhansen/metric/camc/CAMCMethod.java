package br.com.bhansen.metric.camc;

import java.util.Set;

import org.eclipse.jdt.core.IMethod;

import br.com.bhansen.metric.DeclarationMetric;
import br.com.bhansen.utils.Method;
import br.com.bhansen.utils.MethodWithParameters;
import br.com.bhansen.utils.Type;

public class CAMCMethod extends DeclarationMetric {
	
	Set<String> method;
		
	public CAMCMethod(Type type, String method, String parameter) throws Exception {
		super(type);

		IMethod[] iMethods = type.getIType().getMethods();

		for (IMethod iMethod : iMethods) {
			
			MethodWithParameters mp = new Method(iMethod).getMethodWithParameters(parameter);
			
//			if (mp.isMovedMethod(method))
//				removeFakeParameter(mp.getParameters(), parameter);
			
			if((mp.isMethod(method)) || (mp.isMovedMethod(method)))
				this.method = mp.getParameters();

//			if (mp.isPrivate() || mp.isFakeDelegate(method))
//				continue;
			
			// Dont add zero parameters
//			if(! mp.hasParameter())
//				continue;

			getMethods().put(mp.getSignature(), mp.getParameters());

		}
	}

	
	@Override
	public double getMetric() throws Exception {
		double camc = 0;
		double numParams = getParams().size();
		double numMethods = getMethods().size();
		
		if((numMethods == 0) || (numParams == 0)) {
			return 0;
		}
		
		if(method.size() == 0) {
			return 1;
		}
		
		camc += method.size() / numParams;
		
		camc = camc / numMethods;
		
		return camc;
	}

}
