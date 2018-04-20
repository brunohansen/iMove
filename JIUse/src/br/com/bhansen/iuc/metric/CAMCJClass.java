package br.com.bhansen.iuc.metric;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.IType;

public class CAMCJClass extends DeclarationMetricClass {
	
	public CAMCJClass() {
		super("Teste");
		
		getMethods().put("m1", new HashSet<>());
//		getMethods().get("m1").add("X");
		
		getMethods().put("m2", new HashSet<>());
//		getMethods().get("m2").add("X");
		
		getMethods().put("m3", new HashSet<>());
//		getMethods().get("m3").add("X");
//		getMethods().get("m3").add("Z");
//		getMethods().get("m3").add("W");
	}

	public CAMCJClass(IType type) throws Exception {
		super(type);
	}
	
	@Override
	public float getMetric(String fakeDelegate) {
		super.getMetric(fakeDelegate);
		
		if(getMethods().size() == 0)
			return 1f;
		
		float metric = 0;
		
		Entry<String, Set<String>> [] mxs = getMethods().entrySet().toArray(new Entry [1]);
		
		for (int x = 0; x < mxs.length; x++) {
			Entry<String, Set<String>> mx = mxs[x];
						
			Entry<String, Set<String>> [] mys = getMethods().entrySet().toArray(new Entry [1]);
			
			for (int y = 0; y < mys.length; y++) {
				Entry<String, Set<String>> my = mys[y];
								
				Set<String> intersection = new HashSet<>(mx.getValue());
				intersection.retainAll(my.getValue());
				
				Set<String> union = new HashSet<>(mx.getValue());
				union.addAll(my.getValue());
				
				if(union.size() == 0) {
					metric += 1;
				} else {
					metric += (float) intersection.size() / (float) union.size();
				}
			}
		}
				
		metric = metric / (getMethods().size() * getMethods().size());

		return metric;
	}
	
	public static void main(String[] args) {
		CAMCJClass cj = new CAMCJClass();
		
		System.out.println("Resultado: " + cj.getMetric());
	}
}
