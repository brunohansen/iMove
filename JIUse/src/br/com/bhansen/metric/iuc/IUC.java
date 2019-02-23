package br.com.bhansen.metric.iuc;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import br.com.bhansen.metric.AbsMetric;
import br.com.bhansen.utils.Type;

public abstract class IUC extends AbsMetric {

	public IUC(Type type) {
		super(type);
	}

	protected static Map<String, Integer> getCallerCount(Map<String, Set<String>> methods) {
		Map<String, Integer> callerClasses = new HashMap<>();

		for (String caller : getCallers(methods)) {
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

		for (String caller : getCallers(methods)) {
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
		return removeCaller(getMethods(), getName());
	}
	
	public static Map<String, Set<String>> removeCaller(Map<String, Set<String>> methods, String caller) {
		Map<String, Set<String>> result = new HashMap<>(methods);

		for (Entry<String, Set<String>> method : result.entrySet()) {
			method.getValue().remove(caller);
		}

		return result;
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

	@Override
	public String toDetailedString() {
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

		txt.append(toString(methods, "\t"));

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

	public static <K, C extends Collection<?>> String toString(Map<K, C> map, String tabs) {
		StringBuilder txt = new StringBuilder();

		txt.append("\n");

		for (Entry<K, C> e : map.entrySet()) {
			txt.append("\n").append(tabs).append(e.getValue().size()).append(" -> ").append(e.getKey());

			txt.append(toString(e.getValue(), tabs + "\t"));
		}

		txt.append("\n");

		return txt.toString();
	}

	public static <I extends Iterable<?>> String toString(I iterable, String tabs) {
		StringBuilder txt = new StringBuilder();
		
		for (Object o : iterable) {
			txt.append("\n").append(tabs).append(o);
		}

		return txt.toString();
	}

}
