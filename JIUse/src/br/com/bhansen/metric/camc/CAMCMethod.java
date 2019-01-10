package br.com.bhansen.metric.camc;

import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import br.com.bhansen.metric.DeclarationMetric;
import br.com.bhansen.utils.MethodHelper;

public class CAMCMethod extends DeclarationMetric {
	
	Set<String> method;
		
	public CAMCMethod(IType type, boolean zeroParams, String method, String parameter) throws Exception {
		super(type);

		IMethod[] iMethods = type.getMethods();

		for (IMethod iMethod : iMethods) {

//			if ((Flags.isPrivate(iMethod.getFlags())) || (isFakeDelegate(iMethod, method)))
//				continue;

			Set<String> params = createParametersSet(iMethod);
			
//			if (isMovedMethod(iMethod, method))
//				removeFakeParameter(params, parameter);
			
			if((MethodHelper.isMethod(iMethod, method)) || (MethodHelper.isMovedMethod(iMethod, method)))
				this.method = params;

			if (zeroParams) {
				getMethods().put(MethodHelper.getSignature(iMethod), params);
			} else if (params.size() > 0) {
				getMethods().put(MethodHelper.getSignature(iMethod), params);
			}

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
