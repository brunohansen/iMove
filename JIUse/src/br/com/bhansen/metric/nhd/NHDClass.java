package br.com.bhansen.metric.nhd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.config.Config;
import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.DeclarationMetricClass;
import br.com.bhansen.metric.Metric;
import br.com.bhansen.utils.OccMtrx;

public class NHDClass extends DeclarationMetricClass {
	
	public static final BiPredicate<Boolean, Boolean> NHD = new BiPredicate<Boolean, Boolean>() {
		
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
		return nhdClass(this, getMethods(), getValues(), getPredicate());
	}
	
	protected BiPredicate<Boolean, Boolean> getPredicate() {
		return NHD;
	}
	
	public static double nhdClass(Metric instance, Map<String, Set<String>> methods, Set<String> values, BiPredicate<Boolean, Boolean> predicate) {
		return nhdClass(instance, OccMtrx.createOccMtrx(methods, values), predicate);
	}
	
	protected static double nhdClass(Metric instance, boolean[][] poMtrx, BiPredicate<Boolean, Boolean> predicate) {
		double metric = 0;
		
		List<boolean []> poList = new ArrayList<>(Arrays.asList(poMtrx));
				
		for (int i = 0; i < poMtrx.length; i++) {
			List<boolean []> po = new ArrayList<>(poList);
			po.remove(i);
			metric += nhdMethod(instance, poMtrx[i], po.toArray(new boolean[0][0]), predicate);
		}
		
		return metric / poMtrx.length;
	}
	
	protected static double nhdMethod(Metric instance, Set<String> method, Map<String, Set<String>> methods, Set<String> values, BiPredicate<Boolean, Boolean> predicate) {
		Set<String> valuesCopy = new HashSet<>(values);
		valuesCopy.addAll(method);
		
		return nhdMethod(instance, OccMtrx.createOccArray(method, valuesCopy), OccMtrx.createOccMtrx(methods, valuesCopy), predicate);
	}
	
	protected static double nhdMethod(Metric instance, boolean[] method, boolean[][] poMtrx, BiPredicate<Boolean, Boolean> predicate) {
		double metric = 0;
		
		for (int i = 0; i < poMtrx.length; i++) {
			double agree = 0;
			double zeros = 0;

			for (int j = 0; j < method.length; j++) {
				if(! (poMtrx[i][j] || method[j])) {
					zeros++;
				}
				
				if (predicate.test(poMtrx[i][j], method[j])) {
					agree++;
				}
			}
			
			//Metódos sem parametros possuem 100% coesão 
			if(Config.isMetricTight(instance) && zeros == method.length) {
				metric += 1;
			} else {
				metric += agree / method.length;
			}
		}
		
		return metric / poMtrx.length;
	}
		
}
