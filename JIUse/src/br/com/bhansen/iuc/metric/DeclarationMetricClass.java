package br.com.bhansen.iuc.metric;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class DeclarationMetricClass extends MetricClass {
		
	public DeclarationMetricClass(IType type) throws Exception {
		super(type);
		
		// TODO Gatilho
		if(type == null) return;

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
	public void removeFakeParameter(String fakeSignature, String fakeParameter) throws JavaModelException {
		super.removeFakeParameter(fakeSignature, fakeParameter);
		
		String fType = generateInnerSignature(fakeParameter);
		
		for (IField iField : getType().getFields()) {
			if(iField.toString().split(" ", 2)[0].equals(fType)) {
				getMethods().get(fakeSignature).remove(fType);
				return;
			}				
		}
		
		fType = generateSignature(fakeParameter);
		
		for (IField iField : getType().getFields()) {
			if(iField.toString().split(" ", 2)[0].equals(fType)) {
				getMethods().get(fakeSignature).remove(fType);
				return;
			}				
		}
		
		
	}
	
	protected Set<String> getParams() {
		Set<String> params = new HashSet<>();
		
		for (Set<String> ps : getMethods().values()) {
			params.addAll(ps);
		}
		
		return params;
	}
}
