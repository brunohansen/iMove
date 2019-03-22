package br.com.bhansen.metric.nhdm;

import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.metric.UsageMetricMethod;
import br.com.bhansen.utils.Type;

public class UNHDMMethod extends UsageMetricMethod {

	public UNHDMMethod(Type type, String method, IProgressMonitor monitor) throws Exception {
		super(type, method, monitor);
	}

	@Override
	public double getMetric() throws Exception {

		if (getMethods().size() == 0)
			return 0;

		if (getMethods().size() == 0)
			return 0;

		@SuppressWarnings("unchecked")
		Entry<String, Set<String>>[] methods = getMethods().entrySet().toArray(new Entry[0]);
		String clients[] = getClients().toArray(new String[0]);
		boolean method[] = new boolean[clients.length];

		for (int i = 0; i < clients.length; i++) {
			method[i] = getMethod().contains(clients[i]);
		}

		boolean[][] poMtrx = NHDMClass.createOccMtrx(methods, clients);

		return NHDMMethod.nhdmMethod(method, poMtrx);
	}
}
