package br.com.bhansen.iuc.metric;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public abstract class DeclarationMetricClass extends MetricClass {
	
	public DeclarationMetricClass() throws Exception {
		this(null, true, null, null);
		
		getMethods().put("m1", new HashSet<>());
		getMethods().get("m1").add("X");
		
		getMethods().put("m2", new HashSet<>());
		getMethods().get("m2").add("y");
		
		getMethods().put("m3", new HashSet<>());
		getMethods().get("m3").add("X");
//		getMethods().get("m3").add("Z");
//		getMethods().get("m3").add("W");
	}
		
	public DeclarationMetricClass(IType type, boolean zeroParams, String fakeDelegate, String fakeParameter) throws Exception {
		super(type);
		
		// TODO Gatilho
		if(type == null) return;

		IMethod[] methods = type.getMethods();

		for (IMethod method : methods) {
			
			if((! Flags.isPublic(method.getFlags())) || (isFakeDelegate(method, fakeDelegate)))
				continue;
			
			Set<String> params = getParameters(method);
			//Set<String> params = getParametersAndReturn(method);
			
			if(isMethod(method, fakeDelegate))
				removeFakeParameter(params, fakeParameter);
			
			if(zeroParams) {
				getMethods().put(getSignature(method), params);
			} else if(params.size() > 0) {
				getMethods().put(getSignature(method), params);
			}
				
		}
	}
	
	private Set<String> getParameters(String signature) throws IllegalArgumentException, JavaModelException {
		Set<String> parameters = new HashSet<>();

		String strParameters = signature.replaceAll(".*\\(", "").replaceAll("\\).*", "");

		if(! strParameters.isEmpty()) {
			for (String param : strParameters.split(", ")) {
				parameters.add(param);
			}
		}

		return parameters;
	}

	private Set<String> getParameters(IMethod method) throws IllegalArgumentException, JavaModelException {
		return getParameters(getSignature(method));
	}
	
	private Set<String> getParametersAndReturn(IMethod method) throws IllegalArgumentException, JavaModelException {
		String signature = getSignature(method);
		Set<String> parameters = getParameters(signature);
		
		String ret = signature.split(":", 2)[1];
		
		if(! ret.equals("void"))
			parameters.add(ret);

		return parameters;
	}
	
	public void removeFakeParameter(Set<String> params, String fakeParameter) throws JavaModelException {
		
		if(fakeParameter == null)
			return;
		
		String fType = generateInnerSignature(fakeParameter);
		
		for (IField iField : getType().getFields()) {
			if(iField.toString().split(" ", 2)[0].equals(fType)) {
				params.remove(fType);
				return;
			}				
		}
		
		fType = generateSignature(fakeParameter);
		
		for (IField iField : getType().getFields()) {
			if(iField.toString().split(" ", 2)[0].equals(fType)) {
				params.remove(fType);
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
	
	public static int comb(int n) {
		if(n == 0)
			return 0;
		
		if(n <= 2)
			return 1;
		
		return fat(n).divide(fat(2).multiply(fat(n - 2))).intValue();
	}
	
	public static BigInteger fat(int n) {
		BigInteger f = BigInteger.valueOf(n);
		
		while (n > 1) {
			f = f.multiply(BigInteger.valueOf(n - 1));
			n--;
		}
		
		return f;
	}
}
