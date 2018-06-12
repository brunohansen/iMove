package br.com.bhansen.metric.camc;

import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import br.com.bhansen.metric.DeclarationMetric;

public class CAMCMethod extends DeclarationMetric {
	
	Set<String> method;
		
	public CAMCMethod(IType type, boolean zeroParams, String method, String parameter) throws Exception {
		super(type);

		IMethod[] iMethods = type.getMethods();

		for (IMethod iMethod : iMethods) {

//			if ((Flags.isPrivate(iMethod.getFlags())) || (isFakeDelegate(iMethod, method)))
//				continue;

			Set<String> params = createParametersSet(iMethod);
			
			if((isMethod(iMethod, method)) || (isMovedMethod(iMethod, method)))
				this.method = params;

//			if (isMovedMethod(iMethod, method))
//				removeFakeParameter(params, parameter);

			if (zeroParams) {
				getMethods().put(getSignature(iMethod), params);
			} else if (params.size() > 0) {
				getMethods().put(getSignature(iMethod), params);
			}

		}
	}

	
	@Override
	public double getMetric() throws Exception {
		double camc = 0;
		double numParams = getParams().size();
		double numMethods = getMethods().size();
		
		if((numMethods == 0) || (numParams == 0)) {
			return 0.0f;
		}
		
		camc += method.size() / numParams;
		
		camc = camc / numMethods;
		
		return camc;
	}

}
