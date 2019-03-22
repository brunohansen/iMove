package br.com.bhansen.metric.camc;

import org.eclipse.core.runtime.IProgressMonitor;

import br.com.bhansen.metric.DeclarationMetricMethod;
import br.com.bhansen.utils.Jaccard;
import br.com.bhansen.utils.Type;

public class CAMCJMethod extends DeclarationMetricMethod {

	public CAMCJMethod(Type type, String method, String parameter, IProgressMonitor monitor) throws Exception {
		super(type, method, parameter, monitor);
	}
	
	@Override
	public double getMetric() throws Exception {
		if (getMethod().size() == 0)
			return 1;

		if (getMethods().size() == 0)
			return 0;
		
		return Jaccard.similarity(getMethod(), getMethods().values());
//		return Jaccard.biSimilarity(getMethod(), getMethods());

	}

}
