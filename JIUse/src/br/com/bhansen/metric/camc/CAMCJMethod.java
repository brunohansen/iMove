package br.com.bhansen.metric.camc;

import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.metric.DeclarationMetric;

public class CAMCJMethod extends DeclarationMetric {
	
	Set<String> method;
	private boolean publicMethod;
	
	public CAMCJMethod(IType type, boolean zeroParams, String method, String parameter) throws Exception {
		super(type);
		
		IMethod[] iMethods = type.getMethods();
		
		if(isMovedMethod(method)) {
			IMethod movedMethod = null;
			Set<String> params = null;
			
			for (IMethod iMethod : iMethods) {
				
				params = createParametersSet(iMethod);
				removeSelfParameter(params);
				
				if(isMovedMethod(iMethod, method)) {
					movedMethod = iMethod;
					//removeFakeParameter(params, parameter);
					this.method = params;
					this.publicMethod = Flags.isPublic(iMethod.getFlags());
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
//				this.method = params;
//			}
			
		} else {
			for (IMethod iMethod : iMethods) {
				
				Set<String> params = createParametersSet(iMethod);
				removeSelfParameter(params);
				
				if(isMethod(iMethod, method)) {
					this.method = params;	
					this.publicMethod = Flags.isPublic(iMethod.getFlags());
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
	public boolean isPublicMethod() {
		return this.publicMethod;
	}
		
	@Override
	public double getMetric() throws Exception {
		double metric = 0;
		
		if(getMethods().size() == 0)
			return 0;
		
		if(method.size() == 0)
			return 1;
		
		for (Entry<String, Set<String>> entry : getMethods().entrySet()) {
			
			metric += AbsMetric.howMuchIntersect(method, entry.getValue());
			
		}
		
		metric = metric / getMethods().size();

		return metric;
	}

}
