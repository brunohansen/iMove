package br.com.bhansen.metric.nhd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.DeclarationMetricClass;
import br.com.bhansen.utils.OccMtrx;

public class NHDClass extends DeclarationMetricClass {
	
	public NHDClass(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}

	public static final BiPredicate<Boolean, Boolean> NHD = new BiPredicate<Boolean, Boolean>() {
		
		@Override
		public boolean test(Boolean v1, Boolean v2) {
			return (v1 && v2) || (! v1 && ! v2);
		}
	};
	
	@Override
	final public double getMetric() throws Exception {
		return nhdClass(getMethods(), getPredicate());
	}
	
	protected BiPredicate<Boolean, Boolean> getPredicate() {
		return NHD;
	}
	
	public static double nhdClass(Map<String, Set<String>> methods, BiPredicate<Boolean, Boolean> predicate) {
		return nhdClass(OccMtrx.createOccMtrx(methods, uniqueValues(methods)), predicate);
	}
	
	public static double nhdClass(boolean[][] poMtrx, BiPredicate<Boolean, Boolean> predicate) {
		double metric = 0;
		
		if(poMtrx.length == 0)
			return 0;
		
		List<boolean []> poList = new ArrayList<>(Arrays.asList(poMtrx));
				
		for (int i = 0; i < poMtrx.length; i++) {
			List<boolean []> po = new ArrayList<>(poList);
			po.remove(i);
			metric += nhdMethod(poMtrx[i], po.toArray(new boolean[0][0]), predicate);
		}
		
		return metric / poMtrx.length;
	}
	
	protected static double nhdMethod(boolean[] method, boolean[][] poMtrx, BiPredicate<Boolean, Boolean> predicate) {
		double metric = 0;
		
		if(method.length == 0 || poMtrx.length == 0)
			return 0;
		
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
