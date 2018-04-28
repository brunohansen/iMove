package br.com.bhansen.iuc.metric;

import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.IType;

public class NHDMClass extends DeclarationMetricClass {
	
	public NHDMClass() throws Exception {
		super();
	}
	
	public NHDMClass(IType type, boolean zeroParams, String fakeDelegate, String fakeParameter) throws Exception {
		super(type, zeroParams, fakeDelegate, fakeParameter);
	}
	
	@Override
	public double getMetric() throws Exception {
		
		@SuppressWarnings("unchecked")
		Entry<String, Set<String>> [] methods = getMethods().entrySet().toArray(new Entry [0]);
		String params [] = getParams().toArray(new String[0]);
		
		if((methods.length == 0) || (params.length == 0))
			return 0f;
		
		boolean poMtrx[][] = new boolean[methods.length][params.length];
		
		for (int m = 0; m < methods.length; m++) {
			Entry<String, Set<String>> method = methods[m];
			
			for (int p = 0; p < params.length; p++) {
				String param = params[p];
				
				poMtrx[m][p] = method.getValue().contains(param);
			}
		}
						
		double metric = 0;
		
		for (int p = 0; p < params.length; p++) {
			double ones = 0;
			double zeros = 0;
			
			for (int m = 0; m < methods.length; m++) {
				if(poMtrx[m][p]) {
					ones++;
				} else {
					zeros++;
				}
			}
			
			metric += ones * (methods.length - ones) + 0.5 * zeros * (zeros - 1);
		}
		
		double y1 = (2f / (params.length * methods.length * (methods.length - 1)));
		return 1 - y1 * metric;
	}
	
	
	public static void main(String[] args) throws Exception {
		NHDMClass cj = new NHDMClass();
		
		System.out.println("NHDMN Resultado: " + cj.getMetric());
	}

}
