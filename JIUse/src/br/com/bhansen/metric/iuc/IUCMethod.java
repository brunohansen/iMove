package br.com.bhansen.metric.iuc;

import java.util.Set;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import br.com.bhansen.metric.AbsMetric;

public class IUCMethod extends IUC {
	
	private Set<String> method;
	
	public IUCMethod(IType type, String method) throws Exception {
		super(type);
				
		IMethod[] iMethods = type.getMethods();
		
		if(isMovedMethod(method)) {
			IMethod movedMethod = null;
			
			for (IMethod iMethod : iMethods) {
				
				if(isMovedMethod(iMethod, method)) {
					movedMethod = iMethod;
				} else {
					if(getMethods().put(getSignature(iMethod), getCallerClasses(iMethod)) != null) {
						System.out.println("Method " + getSignature(iMethod) + " colision!");
					};	
				}
				
			}
			
			String original = getOriginalMethod(movedMethod);
			
			if(original != null) {
				this.method = getMethods().remove(original);
			} else {
				this.method = getCallerClasses(movedMethod);
			}
			
		} else {
			for (IMethod iMethod : iMethods) {
				
				if(! isMethod(iMethod, method)) {
					if(getMethods().put(getSignature(iMethod), getCallerClasses(iMethod)) != null) {
						System.out.println("Method " + getSignature(iMethod) + " colision!");
					};
				} else {
					this.method = getCallerClasses(iMethod);
				}

			}
		}
				

		
	}
	
	private boolean isMethod(IMethod iMethod, String method) throws Exception {
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
