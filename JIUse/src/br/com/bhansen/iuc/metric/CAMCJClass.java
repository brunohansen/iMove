package br.com.bhansen.iuc.metric;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.IType;

public class CAMCJClass extends DeclarationMetricClass {
	
	public CAMCJClass(IType type) throws Exception {
		super(type);
	}
		
	@Override
	@SuppressWarnings("unchecked")
	public float getMetric(String fakeDelegate, String fakeParameter) throws Exception {
		super.getMetric(fakeDelegate, fakeParameter);
		
		if(getMethods().size() == 0)
			return 0f;
		
		float metric = 0;
		
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
					metric += (float) intersection.size() / (float) union.size();
				}
			}
		}
				
		//metric = metric / (getMethods().size() * getMethods().size());
		metric = metric / comb(getMethods().size());

		return metric;
	}
	
}
