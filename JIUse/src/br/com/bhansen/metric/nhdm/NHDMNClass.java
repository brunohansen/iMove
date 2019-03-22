package br.com.bhansen.metric.nhdm;

import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;

import java.math.BigInteger;
import java.util.Set;

import br.com.bhansen.metric.DeclarationMetricClass;
import br.com.bhansen.utils.Type;

public class NHDMNClass extends DeclarationMetricClass {
		
	public NHDMNClass(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}
	
	@Override
	public double getMetric() throws Exception {
		
		@SuppressWarnings("unchecked")
		Entry<String, Set<String>> [] mxs = getMethods().entrySet().toArray(new Entry [0]);
		String params [] = getParams().toArray(new String[0]);
		
		if((mxs.length < 2) || (params.length == 0))
			return 0f;
		
		boolean pMtrx[][] = NHDMClass.createOccMtrx(mxs, params);
		
		int comb = comb(mxs.length);
		int result [][] = new int[comb][params.length];
		int l = 0;
		
		for (int x = 0; x < mxs.length - 1; x++) {
			for (int y = x + 1; y < mxs.length; y++) {
				for (int i = 0; i < params.length; i++) {
					
					if(pMtrx[x][i] && pMtrx[y][i]) {
						result[l][i] = 1;
					} else if(pMtrx[x][i] ^ pMtrx[y][i]) {
						result[l][i] = 0;
					} else {
						result[l][i] = 2;
					}
				}
				
				l++;
				
			}
		}
				
		double metric = 0;
		
		for (int x = 0; x < comb; x++) {
			for (int i = 0; i < params.length; i++) {
				metric += (result[x][i] == 0)? 1 : 0;
			}
		}

		return 1 - (metric / (comb * params.length));
	}
	
	private static int comb(int n) {
		if (n == 0)
			return 0;

		if (n <= 2)
			return 1;

		return fat(n).divide(fat(2).multiply(fat(n - 2))).intValue();
	}
	
	private static BigInteger fat(int n) {
		BigInteger f = BigInteger.valueOf(n);

		while (n > 1) {
			f = f.multiply(BigInteger.valueOf(n - 1));
			n--;
		}

		return f;
	}

}
