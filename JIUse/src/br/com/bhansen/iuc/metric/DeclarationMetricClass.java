package br.com.bhansen.iuc.metric;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class DeclarationMetricClass extends MetricClass {
	
	public DeclarationMetricClass() throws Exception {
		this(null);
		
		getMethods().put("m1", new HashSet<>());
//		getMethods().get("m1").add("X");
		
		getMethods().put("m2", new HashSet<>());
//		getMethods().get("m2").add("y");
		
		getMethods().put("m3", new HashSet<>());
//		getMethods().get("m3").add("X");
//		getMethods().get("m3").add("Z");
//		getMethods().get("m3").add("W");
	}
		
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
