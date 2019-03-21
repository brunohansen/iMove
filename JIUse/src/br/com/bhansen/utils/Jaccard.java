package br.com.bhansen.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import br.com.bhansen.metric.AbsMetric;

public class Jaccard {

	public static double similarity(Set<String> s1, Set<String> s2) {
		Set<String> intersection = new HashSet<>(s1);
		intersection.retainAll(s2);
	
		Set<String> union = new HashSet<>(s1);
		union.addAll(s2);
	
		if (union.size() == 0) {
			return 1;
		} else {
			return (double) intersection.size() / (double) union.size();
		}
	}
	
	public static double similarity(Set<String> s1, Collection<Set<String>> s2s) {
		double similarity = 0;
		
		if(s2s.size() == 0) {
			return 0;
		}
		
		for (Set<String> s2 : s2s) {
			similarity += similarity(s1, s2);
		}
		
		return similarity / s2s.size();
	}

	public static double biSimilarity(Set<String> s1, Map<String, Set<String>> s2s) {
		
		double mm = Jaccard.similarity(s1, s2s.values());

		if (mm == 0) {
			return 0;
		}
		
		double mp = 0;		
		
		Map<String, Set<String>> mP = AbsMetric.invert(s2s);
		
		for (String k : s1) {
			Map<String, Set<String>> mPC = new HashMap<>(mP);
			Set<String> p = mPC.remove(k);

			if (p != null) {
				mp += Jaccard.similarity(p, mPC.values());
			}

		}
		
		mp = mp / s1.size();
		
		return (mm + mp) / 2;
	}
	
	
}
