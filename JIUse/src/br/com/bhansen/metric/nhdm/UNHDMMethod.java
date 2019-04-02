package br.com.bhansen.metric.nhdm;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.metric.UsageMetricMethod;
import br.com.bhansen.utils.OccMtrx;
import br.com.bhansen.utils.Type;

public class UNHDMMethod extends UsageMetricMethod {

	public UNHDMMethod(Type type, String method, IProgressMonitor monitor) throws Exception {
		super(type, method, monitor);
	}

	@Override
	public double getMetric(Set<String> method, Map<String, Set<String>> methods) {
		String clients[] = getClients().toArray(new String[0]);

		boolean[][] poMtrx = OccMtrx.createOccMtrx(methods, clients);
		
		boolean m[] = new boolean[clients.length];

		for (int i = 0; i < clients.length; i++) {
			m[i] = getMethod().contains(clients[i]);
		}

		return NHDMMethod.nhdmMethod(m, poMtrx);
	}
}
