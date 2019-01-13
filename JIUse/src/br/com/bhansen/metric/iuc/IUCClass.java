package br.com.bhansen.metric.iuc;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;

import br.com.bhansen.utils.Method;
import br.com.bhansen.utils.MethodWithCallers;
import br.com.bhansen.utils.Type;

public class IUCClass extends IUC {
	
	public IUCClass(Type type) throws Exception {
		super(type);
		
		IMethod[] iMethods = type.getIType().getMethods();
				
		for (IMethod iMethod : iMethods) {
			
			MethodWithCallers method = new Method(iMethod).getMethodWithCallers();
			
		//	if((! Flags.isPrivate(iMethod.getFlags())) && (! isFakeDelegate(iMethod, method))) {
				if(getMethods().put(method.getSignature(), method.getCallers()) != null) {
					System.out.println("Method " + method.getSignature() + " colision!");
				};
		//	}

		}
				
	}
		
	public double getMetric() throws Exception {
		return getMetric(getMethods());
	}
	
	public static double getMetric(Map<String, Set<String>> methods) {
		
		Map<String, Integer> callers = getCallerCount(methods);
		
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
