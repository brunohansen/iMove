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
	
	public static double ic(boolean[][] m) {
		double r = ((cci(m) + pp(m)) / 2) * 0.5;
		double i = ((im(m) + ip(m)) / 2) * 0.5;
		return r + i;
	}

	public static double cci(boolean[][] m) {
		if(m.length == 0)
			return 0;
		
		if(m[0].length == 0)
			return 0;
		
		double k = m.length;
		double l = m[0].length;
		double n = 0;
		
		for (int i = 0; i < k - 1; i++) {
			for (int i2 = i + 1; i2 < k; i2++) {
				double a = 0;
				double d = 0;
				
				for (int j = 0; j < l; j++) {
					if(m[i][j] && m[i2][j])		
						a = a + 1;
						
					if(m[i][j] ^ m[i2][j])
						d = d + 1;
				}
				
				n = n + (a / (a + d));
			}			
		}
		
		return n / ((k * (k - 1)) / 2);
	}
	
	public static double pp(boolean[][] m) {
		if(m.length == 0)
			return 0;
		
		if(m[0].length == 0)
			return 0;
		
		double k = m.length;
		double l = m[0].length;
		double n = 0;
		
		for (int j = 0; j < l - 1; j++) {
			for (int j2 = j + 1; j2 < l; j2++) {
				double a = 0;
				double d = 0;
				
				for (int i = 0; i < k; i++) {
					if(m[i][j] && m[i][j2])		
						a = a + 1;
						
					if(m[i][j] ^ m[i][j2])
						d = d + 1;
				}
				
				n = n + (a / (a + d));
			}			
		}
		
		return n / ((l * (l - 1)) / 2);
	}
	
	public static double im(boolean[][] m) {
		if(m.length == 0)
			return 0;
		
		if(m[0].length == 0)
			return 0;
		
		double k = m.length;
		double l = m[0].length;
		double n = 0;
		
		for (int i = 0; i < k - 1; i++) {
			for (int i2 = i + 1; i2 < k; i2++) {
				for (int j = 0; j < l; j++) {
					if(m[i][j] && m[i2][j]) {
						n = n + 1;
						break;
					}
				}
			}			
		}
		
		return n / ((k * (k - 1)) / 2);
	}
	
	public static double ip(boolean[][] m) {
		if(m.length == 0)
			return 0;
		
		if(m[0].length == 0)
			return 0;
		
		double k = m.length;
		double l = m[0].length;
		double n = 0;
		
		for (int j = 0; j < l - 1; j++) {
			for (int j2 = j + 1; j2 < l; j2++) {
				for (int i = 0; i < k; i++) {
					if(m[i][j] && m[i][j2]) {
						n = n + 1;
						break;
					}
				}
			}			
		}
		
		return n / ((l * (l - 1)) / 2);
	}
	
	public static void main(String[] args) {
		//boolean[][] poMtrx = {{true, true, true, false},{false, true, true, true},{true, true, true, false}};
		boolean[][] poMtrx = {{true, false, true}, {true, true, false}, {false, true, true}, {true, false, false}, {true, false, false}};
		
		System.out.println(pp(poMtrx));
		
	}
}
