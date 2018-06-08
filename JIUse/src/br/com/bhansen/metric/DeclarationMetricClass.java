package br.com.bhansen.metric;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public abstract class DeclarationMetricClass extends MetricClass {

	private static Set<String> primitives;

	static {
		primitives = new HashSet<>();

		primitives.add("byte");
		primitives.add("Byte"); 
		primitives.add("short");
		primitives.add("Short");
		primitives.add("int");
		primitives.add("Integer");
		primitives.add("long");
		primitives.add("Long");
		primitives.add("float");
		primitives.add("Float");
		primitives.add("double");
		primitives.add("Double");
		primitives.add("char");
		primitives.add("Character");
		primitives.add("boolean");
		primitives.add("Boolean");
		primitives.add("String");
		primitives.add("void");
	}

	public DeclarationMetricClass() throws Exception {
		this(null, true, null, null);

		getMethods().put("m1", new HashSet<>());
		getMethods().get("m1").add("X");

		getMethods().put("m2", new HashSet<>());
		getMethods().get("m2").add("y");

		getMethods().put("m3", new HashSet<>());
		getMethods().get("m3").add("X");
		// getMethods().get("m3").add("Z");
		// getMethods().get("m3").add("W");
	}

	public DeclarationMetricClass(IType type, boolean zeroParams, String fakeDelegate, String fakeParameter)
			throws Exception {
		super(type);

		// TODO Gatilho
		if (type == null)
			return;

		IMethod[] methods = type.getMethods();

		for (IMethod method : methods) {

			if ((Flags.isPrivate(method.getFlags())) || (isFakeDelegate(method, fakeDelegate)))
				continue;

			//String strParams = getParameters(method);
			String strParams = getParametersAndReturn(method);
			strParams = explodGenerics(strParams);
			
			Set<String> params = createParametersSet(strParams);
			
			//params = removePrimitives(params);

			if (isMethod(method, fakeDelegate))
				removeFakeParameter(params, fakeParameter);

			if (zeroParams) {
				getMethods().put(getSignature(method), params);
			} else if (params.size() > 0) {
				getMethods().put(getSignature(method), params);
			}

		}
	}

	private  static String getParameters(String signature) {
		return signature.replaceAll(".*\\(", "").replaceAll("\\).*", "");
	}
	
	private static Set<String> createParametersSet(String strParameters) {
		Set<String> parameters = new HashSet<>();

		if (! strParameters.isEmpty()) {
			for (String param : strParameters.split(", ")) {
				parameters.add(param);
			}
		}

		return parameters;
	}
	
	private static String explodGenerics(String strParameters) {
		String generics = strParameters;
		
		generics = generics.replaceAll("\\w+ super ", "");
		generics = generics.replaceAll("\\w+ extends ", "");
		generics = generics.replaceAll("<", ", ");
		generics = generics.replaceAll(">", "");
		generics = generics.replaceAll(", \\?", "");
		
		return generics;
	}

	private static String getParameters(IMethod method) throws IllegalArgumentException, JavaModelException {
		return getParameters(getSignature(method));
	}

	private static String getParametersAndReturn(IMethod method) throws IllegalArgumentException, JavaModelException {
		String signature = getSignature(method);
		String parameters = getParameters(signature);

		String ret = signature.split(":", 2)[1];

		if (! ret.equals("void")) {
			if (! parameters.isEmpty()) {
				parameters = parameters + ", " + ret;
			} else {
				parameters = ret;
			}
		}

		return parameters;
	}
	
	private static Set<String> removePrimitives(Set<String> parameters) {
		Set<String> params = new HashSet<>();
		
		for (String param : parameters) {
			if(! primitives.contains(param)) {
				params.add(param);
			}
		}
		
		return params;
	}

	public void removeFakeParameter(Set<String> params, String fakeParameter) throws JavaModelException {

		if (fakeParameter == null)
			return;

		String fType = generateInnerSignature(fakeParameter);

		for (IField iField : getType().getFields()) {
			if (iField.toString().split(" ", 2)[0].equals(fType)) {
				params.remove(fType);
				return;
			}
		}

		fType = generateSignature(fakeParameter);

		for (IField iField : getType().getFields()) {
			if (iField.toString().split(" ", 2)[0].equals(fType)) {
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
		if (n == 0)
			return 0;

		if (n <= 2)
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
	
	public static void main(String[] args) throws Exception {
		String generics = "AbstractChain0_<Input, Output>,    AbstractDelegate<Chain<Input, Output>>, EntityFactory<Ent extends Entity<?>>, Id<T extends Id<T>>, CRUDer<Id, Ent extends Entity<Id>>, ServiceProvider<Service super Retriever<?, ?>>";
		generics = getParameters(generics);
		generics = explodGenerics(generics);
		
		Set<String> parameters = createParametersSet(generics);
		parameters = removePrimitives(parameters);
		
		
		System.out.println(parameters);
	}
}
