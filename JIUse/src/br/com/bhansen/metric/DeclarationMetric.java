package br.com.bhansen.metric;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public abstract class DeclarationMetric extends AbsMetric {

	private final static Set<String> primitives;

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
	
	public DeclarationMetric(IType type) {
		super(type);
	}

	protected final static String getParameters(String signature) {
		return signature.replaceAll(".*\\(", "").replaceAll("\\).*", "");
	}
	
	protected final static Set<String> createParametersSet(IMethod iMethod) throws IllegalArgumentException, JavaModelException {
		//String strParams = getParameters(iMethod);
		String strParams = getParametersAndReturn(iMethod);
		strParams = explodGenerics(strParams);

		Set<String> parameters = new HashSet<>();

		if (! strParams.isEmpty()) {
			for (String param : strParams.split(", ")) {
				parameters.add(param);
			}
		}

		//params = removePrimitives(params);
		
		return parameters;
	}
	
	protected final static String explodGenerics(String strParameters) {
		String generics = strParameters;
		
		generics = generics.replaceAll("\\w+ super ", "");
		generics = generics.replaceAll("\\w+ extends ", "");
		generics = generics.replaceAll("<", ", ");
		generics = generics.replaceAll(">", "");
		generics = generics.replaceAll(", \\?", "");
		
		return generics;
	}

	protected final static String getParameters(IMethod method) throws IllegalArgumentException, JavaModelException {
		return getParameters(getSignature(method));
	}

	protected final static String getParametersAndReturn(IMethod method) throws IllegalArgumentException, JavaModelException {
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
	
	protected final static Set<String> removePrimitives(Set<String> parameters) {
		Set<String> params = new HashSet<>();
		
		for (String param : parameters) {
			if(! primitives.contains(param)) {
				params.add(param);
			}
		}
		
		return params;
	}

	protected final void removeFakeParameter(Set<String> params, String parameter) throws JavaModelException {

		if (parameter == null)
			return;

		String fType = generateInnerSignature(parameter);

		for (IField iField : getType().getFields()) {
			if (iField.toString().split(" ", 2)[0].equals(fType)) {
				params.remove(fType);
				return;
			}
		}

		fType = generateSignature(parameter);

		for (IField iField : getType().getFields()) {
			if (iField.toString().split(" ", 2)[0].equals(fType)) {
				params.remove(fType);
				return;
			}
		}

	}

	protected final Set<String> getParams() {
		Set<String> params = new HashSet<>();

		for (Set<String> ps : getMethods().values()) {
			params.addAll(ps);
		}

		return params;
	}
	
	public static void main(String[] args) throws Exception {
		String generics = "AbstractChain0_<Input, Output>, AbstractDelegate<Chain<Input, Output>>, EntityFactory<Ent extends Entity<?>>, Id<T extends Id<T>>, CRUDer<Id, Ent extends Entity<Id>>, ServiceProvider<Service super Retriever<?, ?>>";
		generics = explodGenerics(generics);
		
		String [] str = generics.split(", ");
		
		HashSet<String> hs = new HashSet<>(Arrays.asList(str));
				
		System.out.println(hs);
	}
	
}
