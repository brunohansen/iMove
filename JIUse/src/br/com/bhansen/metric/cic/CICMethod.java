package br.com.bhansen.metric.cic;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.metric.DeclarationMetricMethod;
import br.com.bhansen.utils.Jaccard;
import br.com.bhansen.utils.Type;

public class CICMethod extends DeclarationMetricMethod {

	public CICMethod(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}
	
	@Override
	public double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		return Jaccard.biSimilarity(getMethod(), getMethods());
	}
	
	public static double cicMM(boolean[] method, boolean[][] occMtrx) {
		double metric = 0;
		
		for (int i = 0; i < occMtrx.length; i++) {
			double agree = 0;
			double disagree = 0;

			for (int j = 0; j < method.length; j++) {
				if ((occMtrx[i][j] && method[j])) { 
					agree++;
				}
				
				if ((occMtrx[i][j] ^ method[j])) { 
					disagree++;
				}
			}

			metric += agree / (agree + disagree);
		}
		
		return metric / occMtrx.length;
	}
	
	public static double cicPP(boolean[] method, boolean[][] occMtrx) {
		double metric = 0;
		
		for (int i = 0; i < occMtrx.length; i++) {
			double agree = 0;
			double disagree = 0;

			for (int j = 0; j < method.length; j++) {
				if ((occMtrx[i][j] && method[j])) { 
					agree++;
				}
				
				if ((occMtrx[i][j] ^ method[j])) { 
					disagree++;
				}
			}

			metric += agree / (agree + disagree);
		}
		
		return metric / occMtrx.length;
	}

}
