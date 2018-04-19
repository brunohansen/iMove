package br.com.bhansen.iuc.metric;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import org.eclipse.jdt.core.IType;

public class LCOMSClass extends DeclarationMetricClass {

	public LCOMSClass(IType type) throws Exception {
		super(type);
	}
	
	@Override
	public float getMetric(String fakeDelegate) {
		super.getMetric(fakeDelegate);
		
		Map<String, Set<String>> mtds1 = getMethods();
		
		float x = 0;
		
		mtds1.forEach(new BiConsumer<String, Set<String>>() {

			@Override
			public void accept(String m1, Set<String> p1) {
				Map<String, Set<String>> mtds2 = getMethods();
				
				mtds2.forEach(new BiConsumer<String, Set<String>>() {

					@Override
					public void accept(String m2, Set<String> p2) {
						Set<String> intersection = new HashSet<>(p1);
						intersection.retainAll(p2);
						
						Set<String> union = new HashSet<>(p1);
						union.addAll(p2);
						
						x += (float) intersection.size() / (float) union.size(); 
						
					}
				});
				
			}
		});
		
		
		
	}
}
