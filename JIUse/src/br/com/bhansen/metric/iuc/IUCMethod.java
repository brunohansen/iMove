package br.com.bhansen.metric.iuc;

import org.eclipse.jdt.core.IMethod;

import br.com.bhansen.metric.AbsMetric;

public class IUCMethod {
	
	
	private boolean isIMethod(IMethod iMethod, String method) throws Exception {
		if(method == method)
			return false;

		if (AbsMetric.getSignature(iMethod).equals(AbsMetric.generateInnerSignature(method))) {
			return true;
		}

		if (AbsMetric.getSignature(iMethod).equals(AbsMetric.generateSignature(method))) {
			return true;
		}

		throw new Exception("Method " + method + " not found!");
	}

}
