package br.com.bhansen.iuc.metric;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.corext.callhierarchy.CallHierarchy;
import org.eclipse.jdt.internal.corext.callhierarchy.MethodWrapper;

@SuppressWarnings("restriction")
public class IUCClass extends MetricClass {
	
	public IUCClass(IType type, String fakeDelegate) throws Exception {
		super(type);
		
		IMethod[] methods = type.getMethods();
				
		for (IMethod method : methods) {
			
			if((! Flags.isPrivate(method.getFlags())) && (! isFakeDelegate(method, fakeDelegate))) {
				if(getMethods().put(getSignature(method), getCallerClasses(method)) != null) {
					System.out.println("Method " + getSignature(method) + " colision!");
				};
			}

		}
		
	}
		
	public double getMetric() throws Exception {
		
		Map<String, Integer> callers = getCallerClasses();
		
		//callers.remove(getName());
		
		double iuc = 0;
		double numMethods = getMethods().size();
		
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
	
	private Set<String> getCallerClasses(IMethod method) {
		Set<String> callerMethods = new HashSet<>();
		
		CallHierarchy callHierarchy = CallHierarchy.getDefault();

		IMember[] members = { method };

		MethodWrapper[] methodWrappers = callHierarchy.getCallerRoots(members);
		for (MethodWrapper mw : methodWrappers) {
			MethodWrapper[] mw2 = mw.getCalls(new NullProgressMonitor());
			for (MethodWrapper m : mw2) {
				IMethod im = getIMethodFromMethodWrapper(m);
				if (im != null) {
					callerMethods.add(getClassName(im.getDeclaringType()));
				}
			}
		}
		
		return callerMethods;
	}
	
	@Override
	public String toString() {
		try {
			StringBuilder txt = new StringBuilder();
			
			txt.append(getName());
			
			txt.append("\n\n\tIUC: ").append(getMetric());		
			txt.append("\n\n\tNum. of methods: ").append(getMethods().size()).append("\n");
			
			for (Entry<String, Set<String>> method : getMethods().entrySet()) {
				txt.append("\n\t").append(method.getValue().size()).append(" -> ").append(method.getKey());
				
				for (String caller : method.getValue()) {
					txt.append("\n\t\t").append(caller);
				}			
			}
			
			Map<String, Integer> callers = getCallerClasses();
			
			//callers.remove(getName());
			
			txt.append("\n\n\tNum. of callers: ").append(callers.size()).append("\n");
			
			for (Entry<String, Integer> caller : callers.entrySet()) {
				txt.append("\n\t").append(caller.getKey()).append(" -> ").append(caller.getValue());
			}
			
			return txt.toString();
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
}
