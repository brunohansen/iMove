package br.com.bhansen.iuc.metric;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class CAMCClass extends MetricClass {

	public CAMCClass(IType type) throws Exception {
		super(getClassName(type));

		IMethod[] methods = type.getMethods();

		for (IMethod method : methods) {
			getMethods().put(getSignature(method), getParameters(method));
		}
	}

	private Set<String> getParameters(IMethod method) throws IllegalArgumentException, JavaModelException {
		Set<String> parameters = new HashSet<>();

		String signature = getSignature(method);
		String strParameters = signature.replaceAll(".*\\(", "").replaceAll("\\).*", "");

		if(! strParameters.isEmpty()) {
			for (String param : strParameters.split(", ")) {
				parameters.add(param);
			}
		}

		return parameters;
	}
	
	@Override
	public float getMetric(String fakeDelegate) {
		super.getMetric(fakeDelegate);
		
		float camc = 0f;
		float numParams = numParams();
		float numMethods = getMethods().size();
		
		if((numMethods == 0) || (numParams == 0)) {
			return 0.0f;
		}
		
		for (Set<String> ps : getMethods().values()) {
			camc += ps.size() / numParams;
		}
		
		camc = camc / numMethods;
		
		return camc;
	}

	private int numParams() {
		Set<String> params = new HashSet<>();
		
		for (Set<String> ps : getMethods().values()) {
			params.addAll(ps);
		}
		
		return params.size();
	}

}
