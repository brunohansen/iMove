package br.com.bhansen.iuc.metric;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

public class MetricClass {
	
	public static String METHOD_PREFIX = "IUC"; 
	
	private String name;
	private Map<String, Set<String>> methods;
	
	public MetricClass(String name) {
		super();
		this.name = name;
		this.methods = new HashMap<>();
	}

	public static String getMoveMethodName(String methodName) {
		return methodName + METHOD_PREFIX;
	}
	
	public static String getClassName(IType type) {
		// \\$ replace the inner class separator for . and (\\.[0-9])*$ removes the anonymous class representation  
		return type.getFullyQualifiedName().replaceAll("\\$", ".").replaceFirst("(\\.[0-9])*$", "");
	}
	
	public static String getSignature(IMethod method) throws IllegalArgumentException, JavaModelException {
		String [] sigParts = Signature.toString(method.getSignature()).split(" ", 2);
		String signature = method.getElementName() + sigParts[1] + ":" + sigParts[0];
		
		return generateInnerSignature(signature);
	}
	
	public static String generateSignature(String method) {
		method = method.replaceAll("\\s", " ");// Change whitespace character: [
												// \t\n\x0B\f\r]
		method = method.replaceAll(",", ", ");// Add space after comma
		method = method.replaceAll(" {2,}", " ");// Remove more than one
		method = method.replaceAll("[a-z|A-Z|0-9|_|$]*?\\.", "");// Remove packages and inner classes

		return method;
	}
	
	public static String generateInnerSignature(String method) {
		method = method.replaceAll("\\s", " ");// Change whitespace character: [
												// \t\n\x0B\f\r]
		method = method.replaceAll(",", ", ");// Add space after comma
		method = method.replaceAll(" {2,}", " ");// Remove more than one
		method = method.replaceAll("([a-z|0-9|_|$]*\\.){2,}", "");// Remove just packages

		return method;
	}
	
	public String getName() {
		return name;
	}
	
	public Map<String, Set<String>> getMethods() {
		return methods;
	}
	
	//TODO Não funciona quando a assinatura não bate
	public void removeFakeDelegate(String fakeDelegate) {
		if(fakeDelegate != null) {
			
			for (Entry<String, Set<String>> method : methods.entrySet()) {
				String fDelegateSig = method.getKey();
				
				if(fDelegateSig.split("\\(", 2)[0].equals(fakeDelegate)) {
					String methodSig = fDelegateSig.replaceFirst("[0-9]{0,1}" + METHOD_PREFIX+ "\\(", "(");
					
					if(methods.containsKey(methodSig)) {
						Set<String> delegateCallers = method.getValue();
						
						if((delegateCallers != null) && (delegateCallers.contains(getName())) && (delegateCallers.size() == 1)) {
							methods.remove(fDelegateSig);
							break;
						}
					} else {
						break;
					}
				}
			}
		}
	}
	
	public float getMetric() {
		return getMetric(null);
	}
	
	public float getMetric(String fakeDelegate) {
		removeFakeDelegate(fakeDelegate);
		
		return 0f;
	}

}
