package br.com.bhansen.jdt;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.JavaModelException;

import br.com.bhansen.config.DataMetricConfig;

public class MethodWithParameters extends Method {
	
	private Set<String> parameters;
	
	public MethodWithParameters(Method method) throws IllegalArgumentException, JavaModelException {
		this(method, null);
	}
	
	public MethodWithParameters(Method method, String without) throws IllegalArgumentException, JavaModelException {
		super(method);
		
		parameters = createParametersSet(without);
	}
	
	public Set<String> getParameters() {
		return parameters;
	}
	
	public boolean hasParameter() {
		return parameters.size() > 0;
	}
		
	private Set<String> createParametersSet(String without) throws IllegalArgumentException, JavaModelException {
		String strParams = getParametersAndReturn();
		
		// Remove arrays
		if(! DataMetricConfig.useArraysAndCollections()) {
			strParams = strParams.replaceAll("\\[\\]", "");
		}
		
		if(without != null) {
			strParams = strParams.replaceFirst(without + "(, )*", "");
		}
		
		if(DataMetricConfig.extractGenerics()) {
			strParams = ParameterHelper.explodGenerics(strParams);
		}
	
		Set<String> params = new HashSet<>();
	
		if (! strParams.isEmpty()) {
			for (String param : strParams.split(", ")) {
				params.add(param);
			}
		}
		
		if(! DataMetricConfig.usePrimitives()) {
			ParameterHelper.removePrimitives(params);
		}
		
		if(! DataMetricConfig.useArraysAndCollections()) {
			ParameterHelper.removeCollections(params);
		}
		
		if(! DataMetricConfig.useThis()) {
			ParameterHelper.removeSelfParameter(params, Type.getName(getIMethod().getDeclaringType()));
		} 
//		else {
//			//Add self na porra toda
//			params.add(Signature.normalizeInnerSignature(Type.getName(getIMethod().getDeclaringType())));
//		}
		
		return params;
	}
		
	private String getParametersAndReturn() throws IllegalArgumentException, JavaModelException {
		String strParams = getSignature().replaceAll(".*\\(", "").replaceAll("\\).*", "");
	
		String ret = getSignature().split(":", 2)[1];
	
		if (! ret.equals("void") && DataMetricConfig.useReturn()) {
			if (! strParams.isEmpty()) {
				strParams = strParams + ", " + ret;
			} else {
				strParams = ret;
			}
		}
	
		return strParams;
	}

}
