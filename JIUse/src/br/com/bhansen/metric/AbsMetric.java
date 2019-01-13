package br.com.bhansen.metric;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import br.com.bhansen.utils.Type;

public abstract class AbsMetric implements Metric {
	
	private String name;
	private Map<String, Set<String>> methods;
	
	public AbsMetric(Type type) {
		super();
		this.name = type.getName();
		this.methods = new HashMap<>();
	}
	
	public String getName() {
		return name;
	}
		
	public Map<String, Set<String>> getMethods() {
		return methods;
	}
	
	@Override
	public String toString() {
		try {
			return getName() + ": " + getMetric();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static double howMuchIntersect(Set<String> s1, Set<String> s2) {
		Set<String> intersection = new HashSet<>(s1);
		intersection.retainAll(s2);
	
		Set<String> union = new HashSet<>(s1);
		union.addAll(s2);
	
		if (union.size() == 0) {
			return 0;
		} else {
			return (double) intersection.size() / (double) union.size();
		}
	}

}
