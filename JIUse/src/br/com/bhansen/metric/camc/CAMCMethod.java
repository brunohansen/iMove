package br.com.bhansen.metric.camc;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import br.com.bhansen.metric.DeclarationMetric;

public class CAMCMethod extends DeclarationMetric {
	
	Set<String> method;
	
	public CAMCMethod(IType type, boolean zeroParams, String method, String parameter) throws Exception {
		super(type);
		
		IMethod[] iMethods = type.getMethods();
		
		if(isMovedMethod(method)) {
			IMethod movedMethod = null;
			Set<String> params = null;
			
			for (IMethod iMethod : iMethods) {
				
				params = createParametersSet(iMethod);
				
				if(isMovedMethod(iMethod, method)) {
					movedMethod = iMethod;
				} else {
					if (zeroParams) {
						getMethods().put(getSignature(iMethod), params);
					} else if (params.size() > 0) {
						getMethods().put(getSignature(iMethod), params);
					}
				}
				
			}
			
//			String original = getOriginalMethod(movedMethod);
//			
//			//Does not make sense compare itself
//			if(original != null) {
//				this.method = getMethods().remove(original);
//			} else {
//				removeFakeParameter(params, parameter);
				this.method = params;
//			}
			
		} else {
			for (IMethod iMethod : iMethods) {
				
				Set<String> params = createParametersSet(iMethod);
				
				if(isMethod(iMethod, method)) {
					this.method = params;					
				} else {
					if (zeroParams) {
						getMethods().put(getSignature(iMethod), params);
					} else if (params.size() > 0) {
						getMethods().put(getSignature(iMethod), params);
					}
				}

			}
		}
		
	}
	
	@Override
	public double getMetric() throws Exception {
		double camc = 0;
		Set<String> params = getParams();
		double numParams = params.size();
		
		if(numParams == 0) {
			return 0.0f;
		}
		
		Set<String> intersection = new HashSet<>(method);
		intersection.retainAll(params);
		
		camc = intersection.size() / numParams;
		
		return camc;
	}

}
