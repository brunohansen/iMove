package br.com.bhansen.metric.cci;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.jdt.Type;
import br.com.bhansen.metric.DeclarationMetricClass;
import br.com.bhansen.utils.Jaccard;

public class CCiClass extends DeclarationMetricClass {

	public CCiClass(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}

	@Override
	public double getMetricValue() throws Exception {
		return cciClass(getMethods());
	}

	public static double cciClass(Map<String, Set<String>> mtds) {
		double r = 0;
		
		if (mtds.size() < 2)
			return 0;

		for (Entry<String, Set<String>> s1 : mtds.entrySet()) {
			Map<String, Set<String>> mtdsCopy = new HashMap<>(mtds);
			mtdsCopy.remove(s1.getKey());
			r = r + Jaccard.similarity(s1.getValue(), mtdsCopy.values());
		}

		return r / mtds.size();
	}

}
