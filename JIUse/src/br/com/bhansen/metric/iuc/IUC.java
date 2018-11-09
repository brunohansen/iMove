package br.com.bhansen.metric.iuc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.corext.callhierarchy.CallHierarchy;
import org.eclipse.jdt.internal.corext.callhierarchy.MethodWrapper;

import br.com.bhansen.metric.AbsMetric;

@SuppressWarnings("restriction")
public abstract class IUC extends AbsMetric {
	
	public IUC(IType type) {
		super(type);
	}
	
	protected static Map<String, Integer> getCallerClasses(Map<String, Set<String>> methods) {
		Map<String, Integer> callerClasses = new HashMap<>();
		
		for (Entry<String, Set<String>> method : methods.entrySet()) {
			
			for (String caller : method.getValue()) {
				Integer count = callerClasses.get(caller);
				count = (count == null)? 1 : count + 1;
				callerClasses.put(caller, count);
			}
		}
		
		return callerClasses;
	}
	
	public static Map<String, Set<String>> getCallerMethods(Map<String, Set<String>> methods) {
		Map<String, Set<String>> callerMethds = new HashMap<>();
		
		for (Entry<String, Set<String>> method : methods.entrySet()) {
			
			for (String caller : method.getValue()) {
				if(! callerMethds.containsKey(caller)) {
					Set<String> cL = new HashSet<>();
					cL.add(method.getKey());
					
					callerMethds.put(caller, cL);				
				} else {
					callerMethds.get(caller).add(method.getKey());
				}
			}
		}
		
		return callerMethds;
	}
	
	protected Set<String> getCallerClasses(IMethod method) {
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
			return toString(getName(), getMetric(), getMethods());
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	public static String toString(String className, double metricValue, Map<String, Set<String>> methods) {
		StringBuilder txt = new StringBuilder();
		
		txt.append("\n");
		
		txt.append(className);

		txt.append("\n\n\tIUC: ").append(metricValue);
		txt.append("\n\n\tNum. of methods: ").append(methods.size());

		printMap(methods, txt);

		Map<String, Integer> callers = getCallerClasses(methods);

		// callers.remove(getName());

		txt.append("\n\n\tNum. of callers: ").append(callers.size()).append("\n");

		for (Entry<String, Integer> caller : callers.entrySet()) {
			txt.append("\n\t").append(caller.getKey()).append(" -> ").append(caller.getValue());
		}
		
		txt.append("\n");
		
		printMap(getCallerMethods(methods), txt);
		
		txt.append("\n");

		return txt.toString();
	}

	private static void printMap(Map<String, Set<String>> map, StringBuilder txt) {
		txt.append("\n");
		
		for (Entry<String, Set<String>> parent : map.entrySet()) {
			txt.append("\n\t").append(parent.getValue().size()).append(" -> ").append(parent.getKey());

			for (String child : parent.getValue()) {
				txt.append("\n\t\t").append(child);
			}
		}
		
		txt.append("\n");
	}	
	
}
