package br.com.bhansen.metric.iuc;

import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import br.com.bhansen.util.SetUtils;

public class IUCJMethod extends IUC {
	
	private Set<String> method;
	
	public IUCJMethod(IType type, String method) throws Exception {
		super(type);
		
		IMethod[] iMethods = type.getMethods();
		
		if(isMovedMethod(method)) {
			IMethod movedMethod = null;
			
			for (IMethod iMethod : iMethods) {
				
				if(isMovedMethod(iMethod, method)) {
					movedMethod = iMethod;
				} else {
					if(getMethods().put(getSignature(iMethod), getCallers(iMethod)) != null) {
						System.out.println("Method " + getSignature(iMethod) + " colision!");
					};	
				}
				
			}
			
//			String original = getOriginalMethod(movedMethod);
//			
//			//Does not make sense compare itself
//			if(original != null) {
//				this.method = getMethods().remove(original);
//			} else {
				this.method = getCallers(movedMethod);
//			}
			
		} else {
			for (IMethod iMethod : iMethods) {
				
				if(isMethod(iMethod, method)) {
					this.method = getCallers(iMethod);
				} else {
					if(getMethods().put(getSignature(iMethod), getCallers(iMethod)) != null) {
						System.out.println("Method " + getSignature(iMethod) + " colision!");
					};
				}

			}
		}
		
	}
	
	@Override
	public double getMetric() throws Exception {
		return getMetric(this.method, this.getMethods());
	}
	
	public static double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		double metric = 0;
		
		if(methods.size() == 0)
			return 0;
		
		for (Entry<String, Set<String>> entry : methods.entrySet()) {
			
			metric += SetUtils.howMuchIntersect(method, entry.getValue());
						
		}
		
		metric = metric / methods.size();

		return metric;
	}
	


}
