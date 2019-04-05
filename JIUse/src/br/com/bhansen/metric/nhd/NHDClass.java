package br.com.bhansen.metric.nhd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.metric.DeclarationMetricClass;
import br.com.bhansen.utils.OccMtrx;
import br.com.bhansen.utils.Type;

public class NHDClass extends DeclarationMetricClass {
	
	protected static final BiPredicate<Boolean, Boolean> NHD = new BiPredicate<Boolean, Boolean>() {
		
		@Override
		public boolean test(Boolean v1, Boolean v2) {
			return (v1 && v2) || (! v1 && ! v2);
		}
	};
	
	public NHDClass(Type type, IProgressMonitor monitor) throws Exception {
		super(type, monitor);
	}
	
	@Override
	final public double getMetric() throws Exception {
		return nhdClass(getMethods(), getValues(), getPredicate());
	}
	
	protected BiPredicate<Boolean, Boolean> getPredicate() {
		return NHD;
	}
	
	protected static double nhdClass(Map<String, Set<String>> methods, Set<String> values, BiPredicate<Boolean, Boolean> predicate) {
		return nhdClass(OccMtrx.createOccMtrx(methods, values), predicate);
	}
	
	protected static double nhdClass(boolean[][] poMtrx, BiPredicate<Boolean, Boolean> predicate) {
		double metric = 0;
		
		List<boolean []> poList = new ArrayList<>(Arrays.asList(poMtrx));
				
		for (int i = 0; i < poMtrx.length; i++) {
			List<boolean []> po = new ArrayList<>(poList);
			po.remove(i);
			metric += nhdMethod(poMtrx[i], po.toArray(new boolean[0][0]), predicate);
		}
		
		return metric / poMtrx.length;
	}
	
	protected static double nhdMethod(Set<String> method, Map<String, Set<String>> methods, Set<String> values, BiPredicate<Boolean, Boolean> predicate) {
		return nhdMethod(OccMtrx.createOccArray(method, values), OccMtrx.createOccMtrx(methods, values), predicate);
	}
	
	protected static double nhdMethod(boolean[] method, boolean[][] poMtrx, BiPredicate<Boolean, Boolean> predicate) {
		double metric = 0;
		
		for (int i = 0; i < poMtrx.length; i++) {
			double agree = 0;

			for (int j = 0; j < method.length; j++) {
				if (predicate.test(poMtrx[i][j], method[j])) {
					agree++;
				}
			}

			metric += agree / method.length;
		}
		
		return metric / poMtrx.length;
	}
		
}
