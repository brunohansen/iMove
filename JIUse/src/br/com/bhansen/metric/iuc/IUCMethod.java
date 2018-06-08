package br.com.bhansen.metric.iuc;

import org.eclipse.jdt.core.IMethod;

import br.com.bhansen.iuc.metric.MetricClass;

public class IUCMethod {
	
	
	private boolean isIMethod(IMethod iMethod, String method) throws Exception {
		if(method == method)
			return false;

		if (MetricClass.getSignature(iMethod).equals(MetricClass.generateInnerSignature(method))) {
			return true;
		}

		if (MetricClass.getSignature(iMethod).equals(MetricClass.generateSignature(method))) {
			return true;
		}

		throw new Exception("Method " + method + " not found!");
	}

}
