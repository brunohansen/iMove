package br.com.bhansen.utils;

import java.util.Map.Entry;
import java.util.Map;
import java.util.Set;

public class OccMtrx {
	
	public static boolean[] createOccArray(Set<String> method, Set<String> values) {
		String vls[] = values.toArray(new String[0]);
		
		boolean occArray[] = new boolean[vls.length];

		for (int i = 0; i < vls.length; i++) {
			occArray[i] = method.contains(vls[i]);
		}
		
		return occArray;
	}

	public static boolean[][] createOccMtrx(Map<String, Set<String>> methods, Set<String> values) {
		String vls[] = values.toArray(new String[0]);
		
		@SuppressWarnings("unchecked")
		Entry<String, Set<String>>[] mtds = methods.entrySet().toArray(new Entry[0]);
		boolean occMtrx[][] = new boolean[mtds.length][vls.length];
		
		for (int m = 0; m < mtds.length; m++) {
			Entry<String, Set<String>> method = mtds[m];
			
			for (int v = 0; v < vls.length; v++) {
				String value = vls[v];
				
				occMtrx[m][v] = method.getValue().contains(value);
			}
		}
		return occMtrx;
	}

}
