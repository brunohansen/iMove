package br.com.bhansen.metric;

import java.util.Map.Entry;
import java.util.Set;

import br.com.bhansen.jdt.MethodWithCallers;
import br.com.bhansen.jdt.Type;

public abstract class UsageMetric extends AbsMetric {

	public UsageMetric(Type type) {
		super(type);
	}
	
	public boolean doesntShareCallers(MethodWithCallers method, Type exception) {
		Set<String> callers = method.getCallers();
		callers.remove(exception.getName());
		
		for (String caller : callers) {
			for (Entry<String, Set<String>> m : getMethods().entrySet()) {
				if(m.getValue().contains(caller)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
}
