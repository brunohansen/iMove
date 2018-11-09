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

		txt.append(className);

		txt.append("\n\n\tIUC: ").append(metricValue);
		txt.append("\n\n\tNum. of methods: ").append(methods.size()).append("\n");

		for (Entry<String, Set<String>> method : methods.entrySet()) {
			txt.append("\n\t").append(method.getValue().size()).append(" -> ").append(method.getKey());

			for (String caller : method.getValue()) {
				txt.append("\n\t\t").append(caller);
			}
		}

		Map<String, Integer> callers = getCallerClasses(methods);

		// callers.remove(getName());

		txt.append("\n\n\tNum. of callers: ").append(callers.size()).append("\n");

		for (Entry<String, Integer> caller : callers.entrySet()) {
			txt.append("\n\t").append(caller.getKey()).append(" -> ").append(caller.getValue());
		}

		return txt.toString();
	}	
	
}
