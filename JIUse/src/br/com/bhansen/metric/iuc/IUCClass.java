package br.com.bhansen.metric.iuc;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.metric.UsageMetricClass;
import br.com.bhansen.metric.camc.CAMCClass;
import br.com.bhansen.utils.Type;

public class IUCClass extends UsageMetricClass {
	
	public IUCClass(Type type, String method, IProgressMonitor monitor) throws Exception {
		super(type, method, monitor);				
	}
		
	public double getMetric() throws Exception {
		return getMetric(getMethods());
	}
	
	public static double getMetric(Map<String, Set<String>> methods) {
		return CAMCClass.getMetric(methods, uniqueValues(methods).size());
	}
	
	public Map<String, Set<String>> getMethodsWithoutThis() {
		return IUCClass.removeCaller(getMethods(), getName());
	}
	
	@Override
	public String toDetailedString() {
		try {
			return IUCClass.toString(getName(), getMetric(), getMethods());
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	public static Map<Set<String>, Set<String>> groupMethodsByCallers(Map<String, Set<String>> methods) {
		Map<Set<String>, Set<String>> groupedMethods = new HashMap<>();
	
		for (Entry<String, Set<String>> method : methods.entrySet()) {
			if (!method.getValue().isEmpty()) {
				if (!groupedMethods.containsKey(method.getValue())) {
					Set<String> ms = new HashSet<>();
					ms.add(method.getKey());
	
					groupedMethods.put(method.getValue(), ms);
				} else {
					groupedMethods.get(method.getValue()).add(method.getKey());
				}
			}
		}
	
		return groupedMethods;
	}

	public static Map<String, Set<String>> removeCaller(Map<String, Set<String>> methods, String caller) {
		Map<String, Set<String>> result = new HashMap<>(methods);
	
		for (Entry<String, Set<String>> method : result.entrySet()) {
			method.getValue().remove(caller);
		}
	
		return result;
	}

	public static Map<String, Map<String, Set<String>>> getMethodsByCaller(Map<String, Set<String>> methods) {
		Map<String, Map<String, Set<String>>> methodsByCaller = new HashMap<>();
	
		for (String caller : uniqueValues(methods)) {
			methodsByCaller.put(caller, new HashMap<>());
		}
	
		for (Entry<String, Set<String>> method : methods.entrySet()) {
	
			for (String caller : method.getValue()) {
				methodsByCaller.get(caller).put(method.getKey(), method.getValue());
			}
		}
	
		return methodsByCaller;
	}

	protected static Map<String, Integer> getCallerCount(Map<String, Set<String>> methods) {
		Map<String, Integer> callerClasses = new HashMap<>();
	
		for (String caller : uniqueValues(methods)) {
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
	
	public static <I extends Iterable<?>> String toString(I iterable, String tabs) {
		StringBuilder txt = new StringBuilder();
		
		for (Object o : iterable) {
			txt.append("\n").append(tabs).append(o);
		}
	
		return txt.toString();
	}

	public static <K, C extends Collection<?>> String toString(Map<K, C> map, String tabs) {
		StringBuilder txt = new StringBuilder();
	
		txt.append("\n");
	
		for (Entry<K, C> e : map.entrySet()) {
			txt.append("\n").append(tabs).append(e.getValue().size()).append(" -> ").append(e.getKey());
	
			txt.append(IUCClass.toString(e.getValue(), tabs + "\t"));
		}
	
		txt.append("\n");
	
		return txt.toString();
	}

	public static String toStringMap(Map<String, Set<String>> map) {
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

	public static String toString(String className, double metricValue, Map<String, Set<String>> methods) {
		StringBuilder txt = new StringBuilder();
	
		txt.append("\n");
	
		txt.append(className);
	
		txt.append("\n\n\tIUC: ").append(metricValue);
		txt.append("\n\n\tNum. of methods: ").append(methods.size());
	
		txt.append(IUCClass.toString(methods, "\t"));
	
		Map<String, Integer> callers = getCallerCount(methods);
	
		// callers.remove(getName());
	
		txt.append("\n\n\tNum. of callers: ").append(callers.size()).append("\n");
	
		for (Entry<String, Integer> caller : callers.entrySet()) {
			txt.append("\n\t").append(caller.getKey()).append(" -> ").append(caller.getValue());
		}
	
		txt.append("\n");
	
		txt.append(IUCClass.toStringMap(invert(methods)));
	
		txt.append("\n");
	
		return txt.toString();
	}
			
}
