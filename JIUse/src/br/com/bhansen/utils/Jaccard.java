package br.com.bhansen.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

import br.com.bhansen.config.Config;
import br.com.bhansen.metric.Metric;

public class Jaccard {
	
	public static final BiFunction<Set<String>, Set<String>, Double> NO_WEIGHT = new BiFunction<Set<String>, Set<String>, Double>() {
		
		@Override
		public Double apply(Set<String> t, Set<String> u) {
			return 1.0;
		}
	};
	
	public static double similarity(Metric instance, Set<String> s1, Set<String> s2) {
		return similarity(instance, s1, s2, NO_WEIGHT);
	}

	public static double similarity(Metric instance, Set<String> s1, Set<String> s2, BiFunction<Set<String>, Set<String>, Double> weight) {
		Set<String> intersection = new HashSet<>(s1);
		intersection.retainAll(s2);
	
		Set<String> union = new HashSet<>(s1);
		union.addAll(s2);
	
		if (union.size() == 0) {
			//Metódos sem parametros possuem 100% coesão 
			if(Config.isMetricTight(instance))
				return 1;
			else
				return 0;
		} else {
			return ((double) intersection.size() / (double) union.size()) * weight.apply(s1, s2);
		}
	}
	
	public static double similarity(Metric instance, Set<String> s1, Collection<Set<String>> s2s) {
		return similarity(instance, s1, s2s, NO_WEIGHT);
	}
	
	public static double similarity(Metric instance, Set<String> s1, Collection<Set<String>> s2s, BiFunction<Set<String>, Set<String>, Double> weight) {
		double similarity = 0;
				
		if(s2s.size() == 0) {
			return 0;
		}
		
		for (Set<String> s2 : s2s) {
			similarity += similarity(instance, s1, s2, weight);
		}
		
		return similarity / s2s.size();
	}

}
