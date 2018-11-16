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
	
	protected static Map<String, Integer> getCallerCount(Map<String, Set<String>> methods) {
		Map<String, Integer> callerClasses = new HashMap<>();
		
		for ( String caller : getCallers(methods)) {
			callerClasses.put(caller, 0);
		}
		
		for (Entry<String, Set<String>> method : methods.entrySet()) {
			
			for (String caller : method.getValue()) {
				int count = callerClasses.get(caller);
				callerClasses.put(caller, count + 1);
			}
		}
		
		return callerClasses;
	}
	
	public static Map<String, Map<String, Set<String>>> getMethodsByCaller(Map<String, Set<String>> methods) {
		Map<String, Map<String, Set<String>>> methodsByCaller = new HashMap<>();
		
		for ( String caller : getCallers(methods)) {
			methodsByCaller.put(caller, new HashMap<>());
		}
		
		for (Entry<String, Set<String>> method : methods.entrySet()) {
			
			for (String caller : method.getValue()) {
				methodsByCaller.get(caller).put(method.getKey(), method.getValue());
			}
		}
		
		return methodsByCaller;
	}
	
	public static Set<String> getCallers(Map<String, Set<String>> methods) {
		Set<String> callers = new HashSet<>();
		
		for (Entry<String, Set<String>> method : methods.entrySet()) {
			callers.addAll(method.getValue());
		}
		
		return callers;
	}	
	
	public Map<String, Set<String>> getMethodsWithoutThis() {
		Map<String, Set<String>> methods = new HashMap<>(getMethods());
		
		for (Entry<String, Set<String>> method : methods.entrySet()) {
			method.getValue().remove(getName());
		}
		
		return methods;
	}
	
	protected Set<String> getCallers(IMethod method) {
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

		txt.append(toString(methods));

		Map<String, Integer> callers = getCallerCount(methods);

		// callers.remove(getName());

		txt.append("\n\n\tNum. of callers: ").append(callers.size()).append("\n");

		for (Entry<String, Integer> caller : callers.entrySet()) {
			txt.append("\n\t").append(caller.getKey()).append(" -> ").append(caller.getValue());
		}
		
		txt.append("\n");
		
		txt.append(toStringMapMap(getMethodsByCaller(methods)));
		
		txt.append("\n");

		return txt.toString();
	}
	
	public static String toStringMapMap(Map<String, Map<String, Set<String>>> map) {
		StringBuilder txt = new StringBuilder();
		
		txt.append("\n");
		
		for (Entry<String, Map<String, Set<String>>> parent : map.entrySet()) {
			txt.append("\n\t").append(parent.getValue().size()).append(" -> ").append(parent.getKey());

			for (Entry<String, Set<String>> child : parent.getValue().entrySet()) {
				txt.append("\n\t\t").append(child.getKey());
			}
		}
		
		txt.append("\n");

		return txt.toString();
	}

	public static String toString(Map<String, Set<String>> map) {
		StringBuilder txt = new StringBuilder();
		
		txt.append("\n");
		
		for (Entry<String, Set<String>> parent : map.entrySet()) {
			txt.append("\n\t").append(parent.getValue().size()).append(" -> ").append(parent.getKey());

			for (String child : parent.getValue()) {
				txt.append("\n\t\t").append(child);
			}
		}
		
		txt.append("\n");

		return txt.toString();
	}	
	
}
