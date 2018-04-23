package br.com.bhansen.iuc.metric;

import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.IType;

public class NHDMClass extends DeclarationMetricClass {
	
	public NHDMClass() throws Exception {
		super();
	}
	
	public NHDMClass(IType type) throws Exception {
		super(type);
	}
	
	@Override
	public float getMetric(String fakeDelegate, String fakeParameter) throws Exception {
		super.getMetric(fakeDelegate, fakeParameter);
		
		Entry<String, Set<String>> [] mxs = getMethods().entrySet().toArray(new Entry [0]);
		String params [] = getParams().toArray(new String[0]);
		
		if((mxs.length == 0) || (params.length == 0))
			return 0f;
		
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
		int l = 0;
		
		for (int x = 0; x < mxs.length - 1; x++) {
			for (int y = x + 1; y < mxs.length; y++) {
				for (int i = 0; i < params.length; i++) {
					
					if(pMtrx[x][i] && pMtrx[y][i]) {
						result[l][i] = 1;
					} else if(pMtrx[x][i] ^ pMtrx[y][i]) {
						result[l][i] = 0;
					} else {
						result[l][i] = 0;
					}
				}
				
				l++;
				
			}
		}
				
		double metric = 0;
		
		for (int i = 0; i < params.length; i++) {
			double ones = 0;
			double zeros = 0;
			
			for (int x = 0; x < comb; x++) {
				if(result[x][i] == 1) {
					ones++;
				} else {
					zeros++;
				}
			}
			
			metric += ones * (getMethods().size() - ones) + 0.5 * zeros * (zeros - 1);
		}
		double y1 = (2f / (params.length * getMethods().size() * (getMethods().size() - 1)));
		double r = y1 * metric;
		return new Float(r);
	}
	
	
	public static void main(String[] args) throws Exception {
		NHDMClass cj = new NHDMClass();
		
		System.out.println("NHDMN Resultado: " + cj.getMetric());
	}

}
