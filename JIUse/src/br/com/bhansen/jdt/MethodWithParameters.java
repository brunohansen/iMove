package br.com.bhansen.jdt;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.JavaModelException;

import br.com.bhansen.config.Config;

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
		//String strParams = getSignature().replaceAll(".*\\(", "").replaceAll("\\).*", "");
		String strParams = getParametersAndReturn();
		
		// Remove arrays
		//strParams = strParams.replaceAll("\\[\\]", "");
		
		if(without != null) {
			strParams = strParams.replaceFirst(without + "(, )*", "");
		}
		
		strParams = ParameterHelper.explodGenerics(strParams);
	
		Set<String> params = new HashSet<>();
	
		if (! strParams.isEmpty()) {
			for (String param : strParams.split(", ")) {
				params.add(param);
			}
		}
		
		if(Config.isMetricTight()) {
			ParameterHelper.removePrimitives(params);
		}
		
		//ParameterHelper.removeCollections(params);		
		ParameterHelper.removeSelfParameter(params, Type.getName(getIMethod().getDeclaringType()));
		
		return params;
	}
		
	private String getParametersAndReturn() throws IllegalArgumentException, JavaModelException {
		String strParams = getSignature().replaceAll(".*\\(", "").replaceAll("\\).*", "");
	
		String ret = getSignature().split(":", 2)[1];
	
		if (! ret.equals("void")) {
			if (! strParams.isEmpty()) {
				strParams = strParams + ", " + ret;
			} else {
				strParams = ret;
			}
		}
	
		return strParams;
	}

}
