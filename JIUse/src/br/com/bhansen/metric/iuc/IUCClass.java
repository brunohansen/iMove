package br.com.bhansen.metric.iuc;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

public class IUCClass extends IUC {
	
	public IUCClass(IType type) throws Exception {
		super(type);
		
		IMethod[] iMethods = type.getMethods();
				
		for (IMethod iMethod : iMethods) {
			
		//	if((! Flags.isPrivate(iMethod.getFlags())) && (! isFakeDelegate(iMethod, method))) {
				if(getMethods().put(getSignature(iMethod), getCallerClasses(iMethod)) != null) {
					System.out.println("Method " + getSignature(iMethod) + " colision!");
				};
		//	}

		}
				
	}
		
	public double getMetric() throws Exception {
		return getMetric(getMethods());
	}
	
	public static double getMetric(Map<String, Set<String>> methods) {
		
		Map<String, Integer> callers = getCallerClasses(methods);
		
		//callers.remove(getName());
		
		double iuc = 0;
		double numMethods = methods.size();
		
		if((numMethods == 0) || (callers.size() == 0)) {
			return 0;
		}
		
		//SIUC ainda na duvida
		double deduct = 0;//(numMethods > 1)? 1: 0;
				
		for (Entry<String, Integer> caller : callers.entrySet()) {
			iuc += (caller.getValue() - deduct) / numMethods;			
		}
		
		iuc = iuc / callers.size();
		
		return iuc;
	}
			
}
