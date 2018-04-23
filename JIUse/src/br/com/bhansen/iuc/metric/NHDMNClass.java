package br.com.bhansen.iuc.metric;

import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.jdt.core.IType;

public class NHDMNClass extends DeclarationMetricClass {
	
	public NHDMNClass() throws Exception {
		super();
	}
	
	public NHDMNClass(IType type) throws Exception {
		super(type);
	}
	
	@Override
	public float getMetric(String fakeDelegate, String fakeParameter) throws Exception {
		super.getMetric(fakeDelegate, fakeParameter);
		
		if(getMethods().size() == 0)
			return 0f;
		
		Entry<String, Set<String>> [] mxs = getMethods().entrySet().toArray(new Entry [1]);
		String params [] = getParams().toArray(new String[1]);
		
		boolean pMtrx[][] = new boolean[mxs.length][params.length];
		
		for (int x = 0; x < mxs.length; x++) {
			Entry<String, Set<String>> method = mxs[x];
			
			for (int i = 0; i < params.length; i++) {
				String param = params[i];
				
				pMtrx[x][i] = method.getValue().contains(param);
			}
		}
		
		int comb = comb(mxs.length);
		int result [][] = new int[comb][params.length];
		
		for (int x = 0; x < mxs.length - 1; x++) {
			for (int y = x + 1; y < mxs.length; y++) {
				for (int i = 0; i < params.length; i++) {
					
					if(pMtrx[x][i] && pMtrx[y][i]) {
						result[x + y - 1][i] = 1;
					} else if(pMtrx[x][i] ^ pMtrx[y][i]) {
						result[x + y - 1][i] = 0;
					} else {
						result[x + y - 1][i] = 2;
					}
				}
			}
		}
				
		float metric = 0;
		
		for (int x = 0; x < comb; x++) {
			for (int i = 0; i < params.length; i++) {
				metric += (result[x][i] == 0)? 1 : 0;
			}
		}

		return 1 - (metric / (comb * params.length));
	}
	
	
	public static void main(String[] args) throws Exception {
		NHDMNClass cj = new NHDMNClass();
		
		System.out.println("NHDMN Resultado: " + cj.getMetric());
	}

}
