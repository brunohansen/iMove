package br.com.bhansen.metric.camc;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.IType;

import br.com.bhansen.metric.DeclarationMetricClass;

public class CAMCJClass extends DeclarationMetricClass {
	
	public CAMCJClass() throws Exception {
		super();
	}
	
	public CAMCJClass(IType type, boolean zeroParams, String method, String fakeParameter) throws Exception {
		super(type, zeroParams, method, fakeParameter);
	}
		
	@Override
	@SuppressWarnings("unchecked")
	public double getMetric() throws Exception {
		
		if(getMethods().size() < 2)
			return 0;
		
		double metric = 0;
		
		Entry<String, Set<String>> [] mxs = getMethods().entrySet().toArray(new Entry [1]);
		
		for (int x = 0; x < mxs.length - 1; x++) {
			Entry<String, Set<String>> mx = mxs[x];
						
			Entry<String, Set<String>> [] mys = getMethods().entrySet().toArray(new Entry [1]);
			
			for (int y = x + 1; y < mys.length; y++) {
				Entry<String, Set<String>> my = mys[y];
								
				Set<String> intersection = new HashSet<>(mx.getValue());
				intersection.retainAll(my.getValue());
				
				Set<String> union = new HashSet<>(mx.getValue());
				union.addAll(my.getValue());
				
				if(union.size() == 0) {
					metric += 0;
				} else {
					metric += (double) intersection.size() / (double) union.size();
				}
			}
		}
				
		metric = metric / comb(getMethods().size());

		return metric;
	}
	
	public static void main(String[] args) throws Exception {
		CAMCJClass cj = new CAMCJClass();
		
		System.out.println("CAMCJ Resultado: " + cj.getMetric());
	}
	
}
