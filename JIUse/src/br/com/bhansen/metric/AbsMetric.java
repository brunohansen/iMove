package br.com.bhansen.metric;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import br.com.bhansen.jdt.Type;

public abstract class AbsMetric implements Metric {
	
	private String name;
	private Map<String, Set<String>> methods;
	
	public AbsMetric(Type type) {
		super();
		this.name = type.getName();
		this.methods = new HashMap<>();
	}
	
	@Override
	public final double getMetric() throws Exception {
		return getMetricValue();
	}
	
	public static BigDecimal round(double value) {
		return new BigDecimal(value).setScale(6, RoundingMode.HALF_UP);
	}
	
	protected abstract double getMetricValue() throws Exception;
	
	public String getName() {
		return name;
	}
		
	public Map<String, Set<String>> getMethods() {
		return methods;
	}
	
	public String toDetailedString() { 
		return toString();
	}
	
	@Override
	public String toString() {
		try {
			return getName() + ": " + getMetric();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <T> Set<T> uniqueValues(Map<?, ? extends Collection<T>> m) {
		Set<T> values = new HashSet<>();
		
		for (Collection<T> mValues : m.values()) {
			values.addAll(mValues);
		}
		
		return values;
	}
	
	protected static <T> Set<T> uniqueValues(Set<T> s, Map<?, ? extends Collection<T>> m) {
		Set<T> values = new HashSet<>(s);
		
		values.addAll(uniqueValues(m));
		
		return values;
	}
	
	public static <T> Set<T> uniqueValues(Map<?, ? extends Collection<T>> m1, Map<?, ? extends Collection<T>> m2) {
		return uniqueValues(uniqueValues(m1), m2);
	}
	
	public static <T> Map<T, Set<T>> transpose(Map<T, Set<T>> m) {
		Map<T, Set<T>> m2 = new HashMap<>();

		for (T v : uniqueValues(m)) {
			m2.put(v, new HashSet<>());
		}

		for (Entry<T, Set<T>> e : m.entrySet()) {

			for (T v : e.getValue()) {
				m2.get(v).add(e.getKey());
			}
		}

		return m2;
	}

}
