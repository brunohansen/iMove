package br.com.bhansen.metric.ic;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.DeclarationMetricMethod;
import br.com.bhansen.utils.Jaccard;

public class ICMethod extends DeclarationMetricMethod {

	public ICMethod(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
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
		if (m.length == 0)
			return 0;

		if (m[0].length == 0)
			return 0;

		double k = m.length;
		double n = 0;

		for (int i = 0; i < k; i++) {
			n = n + cci(m, i);
		}

		return n / k;
	}

	public static double cci(boolean[][] m, int i2) {
		if (m.length == 0)
			return 0;

		if (m[0].length == 0)
			return 0;

		double k = m.length;
		double l = m[0].length;
		double n = 0;

		for (int i = 0; i < k; i++) {

			if (i == i2)
				continue;

			double a = 0;
			double d = 0;

			for (int j = 0; j < l; j++) {
				if (m[i][j] && m[i2][j])
					a = a + 1;

				else if (m[i][j] ^ m[i2][j])
					d = d + 1;
			}

			n = n + (a / (a + d));
		}

		return n / (k - 1);
	}

	public static double pp(boolean[][] m) {
		if (m.length == 0)
			return 0;

		if (m[0].length == 0)
			return 0;

		double l = m[0].length;
		double n = 0;

		for (int j = 0; j < l; j++) {
			n = n + pp(m, j);
		}

		return n / l;
	}

	public static double pp(boolean[][] m, int j2) {
		if (m.length == 0)
			return 0;

		if (m[0].length == 0)
			return 0;

		double k = m.length;
		double l = m[0].length;
		double n = 0;

		for (int j = 0; j < l; j++) {

			if (j == j2)
				continue;

			double a = 0;
			double d = 0;

			for (int i = 0; i < k; i++) {
				if (m[i][j] && m[i][j2])
					a = a + 1;

				else if (m[i][j] ^ m[i][j2])
					d = d + 1;
			}

			n = n + (a / (a + d));
		}

		return n / (l - 1);
	}
	
	public static double im(boolean[][] m) {
		if (m.length == 0)
			return 0;

		if (m[0].length == 0)
			return 0;

		double k = m.length;
		double n = 0;

		for (int i = 0; i < k; i++) {
			n = n + im(m, i);
		}

		return n / k;
	}

	public static double im(boolean[][] m, int i2) {
		if (m.length == 0)
			return 0;

		if (m[0].length == 0)
			return 0;

		double k = m.length;
		double l = m[0].length;
		double n = 0;

		for (int i = 0; i < k; i++) {

			if (i == i2)
				continue;

			for (int j = 0; j < l; j++) {
				if (m[i][j] && m[i2][j]) {
					n = n + 1;
					break;
				}
			}
		}

		return n / (k - 1);
	}
	
	public static double ip(boolean[][] m) {
		if (m.length == 0)
			return 0;

		if (m[0].length == 0)
			return 0;

		double l = m[0].length;
		double n = 0;

		for (int j = 0; j < l; j++) {
			n = n + ip(m, j);
		}

		return n / l;
	}

	public static double ip(boolean[][] m, int j2) {
		if (m.length == 0)
			return 0;

		if (m[0].length == 0)
			return 0;

		double k = m.length;
		double l = m[0].length;
		double n = 0;

		for (int j = 0; j < l; j++) {

			if (j == j2)
				continue;

			for (int i = 0; i < k; i++) {
				if (m[i][j] && m[i][j2]) {
					n = n + 1;
					break;
				}
			}
		}

		return n / (l - 1);
	}

	public static void main(String[] args) {
//		 boolean[][] poMtrx = {
//				 {true, true, true, false},
//				 {false, true, true, true},
//				 {true, true, true, false}};
		
		boolean[][] poMtrx = { 
				{ true, false, true }, 
				{ true, true, false }, 
				{ false, true, true },
				{ true, false, false }, 
				{ true, false, false } };

		System.out.println(ic(poMtrx));

	}
}
