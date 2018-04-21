package br.com.bhansen.iuc.metric;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

public class IUCClass extends MetricClass {
	
	public IUCClass(IType type) throws Exception {
		super(type);
		
		IMethod[] methods = type.getMethods();
				
		for (IMethod method : methods) {
			
			if(getMethods().put(getSignature(method), getCallerClasses(method)) != null) {
				throw new Exception("Method " + getSignature(method) + " colision!");
			};
		}
		
	}
		
	public float getMetric(String fakeDelegate) throws Exception {
		super.getMetric(fakeDelegate);
		
		Map<String, Integer> callers = getCallerClasses();
		
		return calcIUC(callers);
	}
		
	private Map<String, Integer> getCallerClasses() {
		Map<String, Integer> callerClasses = new HashMap<>();
		
		for (Entry<String, Set<String>> method : getMethods().entrySet()) {
			
			for (String caller : method.getValue()) {
				Integer count = callerClasses.get(caller);
				count = (count == null)? 1 : count + 1;
				callerClasses.put(caller, count);
			}
		}
		
		return callerClasses;
	}
	
	private float calcIUC(Map<String, Integer> callerClasses) {
		float iuc = 0.0f;
		float numMethods = getMethods().size();
		
		if((numMethods == 0) || (callerClasses.size() == 0)) {
			return 0.0f;
		}
		
		for (Entry<String, Integer> caller : callerClasses.entrySet()) {
			iuc += caller.getValue() / numMethods;			
		}
		
		iuc = iuc / callerClasses.size();
		
		return iuc;
	}
	
	@Override
	public String toString() {
		StringBuilder txt = new StringBuilder();
		
		txt.append(getName());
		
		Map<String, Integer> callers = getCallerClasses();
		
		txt.append("\n\n\tIUC: ").append(calcIUC(callers));		
		txt.append("\n\n\tNum. of methods: ").append(getMethods().size()).append("\n");
		
		for (Entry<String, Set<String>> method : getMethods().entrySet()) {
			txt.append("\n\t").append(method.getValue().size()).append(" -> ").append(method.getKey());
			
			for (String caller : method.getValue()) {
				txt.append("\n\t\t").append(caller);
			}			
		}
		
		txt.append("\n\n\tNum. of callers: ").append(callers.size()).append("\n");
		
		for (Entry<String, Integer> caller : callers.entrySet()) {
			txt.append("\n\t").append(caller.getKey()).append(" -> ").append(caller.getValue());
		}
		
		return txt.toString();
	}
	
}
