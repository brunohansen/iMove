package br.com.bhansen.metric.iuc;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import br.com.bhansen.metric.AbsMetric;

public class IUCMethod extends IUC {
	
	private String method;
	
	public IUCMethod(IType type, String method) throws Exception {
		super(type);
				
		IMethod[] iMethods = type.getMethods();
		
		if(isMovedMethod(method)) {
			
		} else {
			
		}
				
		for (IMethod iMethod : iMethods) {
			
			if((! Flags.isPrivate(iMethod.getFlags())) && (! isFakeDelegate(iMethod, method))) {
				if(getMethods().put(getSignature(iMethod), getCallerClasses(iMethod)) != null) {
					System.out.println("Method " + getSignature(iMethod) + " colision!");
				};
			}

		}
		
	}
	
	private boolean isIMethod(IMethod iMethod, String method) throws Exception {
		if(method == null)
			return false;

		if (AbsMetric.getSignature(iMethod).equals(AbsMetric.generateInnerSignature(method))) {
			return true;
		}

		if (AbsMetric.getSignature(iMethod).equals(AbsMetric.generateSignature(method))) {
			return true;
		}

		return false;
	}

	@Override
	public double getMetric() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
