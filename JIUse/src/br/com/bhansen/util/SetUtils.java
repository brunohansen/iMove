package br.com.bhansen.util;

import java.util.HashSet;
import java.util.Set;

public class SetUtils {
	
	public static double howMuchIntersect(Set<String> s1, Set<String> s2) {
		Set<String> intersection = new HashSet<>(s1);
		intersection.retainAll(s2);
		
		Set<String> union = new HashSet<>(s1);
		union.addAll(s2);
		
		if(union.size() == 0) {
			return 0;
		} else {
			return (double) intersection.size() / (double) union.size();
		}
	}

}
