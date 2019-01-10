package br.com.bhansen.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.JavaModelException;

public class ParameterHelper {

	final static Set<String> primitives = new HashSet<>();

	static {
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

	protected final static String explodGenerics(String parameters) {
		String generics = parameters;
		
		generics = generics.replaceAll("\\w+ super ", "");
		generics = generics.replaceAll("\\w+ extends ", "");
		generics = generics.replaceAll("<", ", ");
		generics = generics.replaceAll(">", "");
		generics = generics.replaceAll(", \\?", "");
		
		return generics;
	}

	public final static Set<String> removePrimitives(Set<String> parameters) {
		Set<String> params = new HashSet<>();
		
		for (String param : parameters) {
			if(! primitives.contains(param)) {
				params.add(param);
			}
		}
		
		return params;
	}
		
	public static final void removeSelfParameter(Set<String> parameters, String className) throws JavaModelException {
	
		parameters.remove(Signature.normalizeInnerSignature(className));
		parameters.remove(Signature.normalizeSignature(className));
		return;
	
	}
	
	public static final void main(String[] args) throws Exception {
		String generics = "AbstractChain0_<Input, Output>, AbstractDelegate<Chain<Input, Output>>, EntityFactory<Ent extends Entity<?>>, Id<T extends Id<T>>, CRUDer<Id, Ent extends Entity<Id>>, ServiceProvider<Service super Retriever<?, ?>>";
		generics = ParameterHelper.explodGenerics(generics);
		
		String [] str = generics.split(", ");
		
		HashSet<String> hs = new HashSet<>(Arrays.asList(str));
				
		System.out.println(hs);
	}

}
