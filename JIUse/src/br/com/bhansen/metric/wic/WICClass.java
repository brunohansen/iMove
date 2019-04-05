package br.com.bhansen.metric.wic;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.ic.ICClass;

public class WICClass extends ICClass {
	
	protected WICClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}
	
	@Override
	protected BiFunction<Set<String>, Set<String>, Double> createWeight() {
		return createWeight(getValues().size());
	}
	
	protected static BiFunction<Set<String>, Set<String>, Double> createWeight(double values) {
		return new BiFunction<Set<String>, Set<String>, Double>() {
			
			@Override
			public Double apply(Set<String> m1, Set<String> m2) {
				Set<String> union = new HashSet<>(m1);
				union.addAll(m2);
				
				return union.size() / values;
			}
		};
	}	

}
