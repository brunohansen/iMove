package br.com.bhansen.metric.nhdm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.metric.DeclarationMetricMethod;
import br.com.bhansen.utils.Type;

public class NHDMMethod extends DeclarationMetricMethod {

	public NHDMMethod(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}

	@Override
	public double getMetric() throws Exception {

		if (getMethod().size() == 0)
			return 1;

		if (getMethods().size() == 0)
			return 0;

		@SuppressWarnings("unchecked")
		Entry<String, Set<String>>[] methods = getMethods().entrySet().toArray(new Entry[0]);
		String params[] = getParams().toArray(new String[0]);
		boolean method[] = new boolean[params.length];

		for (int i = 0; i < params.length; i++) {
			method[i] = getMethod().contains(params[i]);
		}

		boolean[][] poMtrx = NHDMClass.createOccMtrx(methods, params);

		return nhdmMethod(method, poMtrx);
	}
	
	private static double nhdmClass(boolean[][] poMtrx) {
		double metric = 0;
		
		List<boolean []> poList = new ArrayList<>(Arrays.asList(poMtrx));
				
		for (int i = 0; i < poMtrx.length; i++) {
			List<boolean []> po = new ArrayList<>(poList);
			po.remove(i);
			metric += nhdmMethod(poMtrx[i], po.toArray(new boolean[0][0]));
		}
		
		return metric / poMtrx.length;
	}

	public static double nhdmMethod(boolean[] method, boolean[][] poMtrx) {
		double metric = 0;
		
		for (int i = 0; i < poMtrx.length; i++) {
			double agree = 0;

			for (int j = 0; j < method.length; j++) {
				if ((poMtrx[i][j] && method[j])) {
				//if ((poMtrx[i][j] && method[j]) || (! poMtrx[i][j] && ! method[j])) {
					agree++;
				}
			}

			metric += agree / method.length;
		}
		
		return metric / poMtrx.length;
	}
	
	public static void main(String[] args) {
		//boolean[][] poMtrx = {{true, true, true, false},{false, true, true, true},{true, true, true, false}};
		boolean[][] poMtrx = {{true, false, true}, {true, true, false}, {false, true, true}, {true, false, false}, {true, false, false}};
		
		System.out.println(nhdmClass(poMtrx));
		
	}
}
