package br.com.bhansen.utils;

import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;

public class OccMtrx {

	public static boolean[][] createOccMtrx(Map<String, Set<String>> methods, String[] values) {
		@SuppressWarnings("unchecked")
		Entry<String, Set<String>>[] mtds = methods.entrySet().toArray(new Entry[0]);
		boolean occMtrx[][] = new boolean[mtds.length][values.length];
		
		for (int m = 0; m < mtds.length; m++) {
			Entry<String, Set<String>> method = mtds[m];
			
			for (int v = 0; v < values.length; v++) {
				String value = values[v];
				
				occMtrx[m][v] = method.getValue().contains(value);
			}
		}
		return occMtrx;
	}

}
