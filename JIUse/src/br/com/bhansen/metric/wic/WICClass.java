package br.com.bhansen.metric.wic;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.ic.ICClass;

public class WICClass extends ICClass {
	
	public WICClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}
	
	@Override
	protected MMWeight createMMWeight() {
		return createMMWeight(getValues().size());
	}
	
	@Override
	protected PPWeight createPPWeight() {
		return createPPWeight(getMethods().size());
	}
	
	public static MMWeight createMMWeight(Set<String> value, Set<String> values) {
		Set<String> vls = new HashSet<>(values);
		vls.addAll(value);
		
		return createMMWeight(vls.size());
	}
	
	public static MMWeight createMMWeight(double values) {
		return new MMWeight() {
			
			@Override
			public Double apply(Set<String> m1, Set<String> m2) {
				Set<String> union = new HashSet<>(m1);
				union.addAll(m2);
				
				return union.size() / values;
			}
		};
	}	
		
	public static PPWeight createPPWeight(double values) {
		return new PPWeight() {
			
			@Override
			public Double apply(Set<String> m1, Set<String> m2) {
				Set<String> union = new HashSet<>(m1);
				union.addAll(m2);
				
				return union.size() / values;
			}
		};
	}

}
