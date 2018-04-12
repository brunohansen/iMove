package br.com.bhansen.iuc.metric;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.corext.callhierarchy.CallHierarchy;
import org.eclipse.jdt.internal.corext.callhierarchy.MethodWrapper;

@SuppressWarnings("restriction")
public class IUCClass {
	
	public static String METHOD_PREFIX = "IUC"; 
	
	private String name;
	private String fakeDelegate;
	private HashMap<String, HashSet<String>> methods;
	
	public IUCClass(IType type, String fakeDelegate) throws Exception {
		this.methods = new HashMap<>();		
		this.name = getClassName(type);
		this.fakeDelegate = fakeDelegate;
		
		IMethod[] methods = type.getMethods();
				
		for (IMethod method : methods) {
			
			if(this.methods.put(getSignature(method), getCallerMethods(method)) != null) {
				throw new Exception("Method " + getSignature(method) + " colision!");
			};
		}
		
		removeFakeDelegate();
	}
	
	public void removeFakeDelegate() {
		if(fakeDelegate != null) {
			
			for (Entry<String, HashSet<String>> method : methods.entrySet()) {
				String fDelegateSig = method.getKey();
				
				if(fDelegateSig.split("\\(", 2)[0].equals(fakeDelegate)) {
					String methodSig = fDelegateSig.replaceFirst("[0-9]{0,1}" + METHOD_PREFIX+ "\\(", "(");
					
					if(methods.containsKey(methodSig)) {
						HashSet<String> delegateCallers = method.getValue();
						
						if((delegateCallers != null) && (delegateCallers.contains(getName())) && (delegateCallers.size() == 1)) {
							methods.remove(fDelegateSig);
							break;
						}
					}
				}
			}
		}
	}
	
	public IUCClass(IType type) throws Exception {
		this(type, null);
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
		
		return signature;
	}
	
	public static String generateSignature(String method) throws Exception {
		method = method.replaceAll("\\s", " ");// Change whitespace character: [
												// \t\n\x0B\f\r]
		method = method.replaceAll(",", ", ");// Add space after comma
		method = method.replaceAll(" {2,}", " ");// Remove more than one
		method = method.replaceAll("[a-z|A-Z|_|$]*?\\.", "");// Remove packages

		return method;
	}
	
	public String getName() {
		return name;
	}
	
	public HashMap<String, HashSet<String>> getMethods() {
		return methods;
	}
	
	public float getIUC() {
		HashMap<String, Integer> callers = getCallerClasses();
		
		return calcIUC(callers);
	}
	
	public float compareIUC(IUCClass clazz) {
		return getIUC() - clazz.getIUC();
	}
		
	private HashSet<String> getCallerMethods(IMethod method) {
		HashSet<String> callerMethods = new HashSet<>();
		
		CallHierarchy callHierarchy = CallHierarchy.getDefault();

		IMember[] members = { method };

		MethodWrapper[] methodWrappers = callHierarchy.getCallerRoots(members);
		for (MethodWrapper mw : methodWrappers) {
			MethodWrapper[] mw2 = mw.getCalls(new NullProgressMonitor());
			for (MethodWrapper m : mw2) {
				callerMethods.add(getClassName(m.getMember().getDeclaringType()));
			}
		}
		
		return callerMethods;
	}
		
	private HashMap<String, Integer> getCallerClasses() {
		HashMap<String, Integer> callerClasses = new HashMap<>();
		
		for (Entry<String, HashSet<String>> method : methods.entrySet()) {
			
			for (String caller : method.getValue()) {
				Integer count = callerClasses.get(caller);
				count = (count == null)? 1 : count + 1;
				callerClasses.put(caller, count);
			}
		}
		
		return callerClasses;
	}
	
	private float calcIUC(HashMap<String, Integer> callerClasses) {
		float iuc = 0.0f;
		float numMethods = methods.size();
		
		if((numMethods == 0) || (callerClasses.size() == 0)) {
			return 0.0f;
		}
		
		for (Entry<String, Integer> caller : callerClasses.entrySet()) {
			iuc += caller.getValue() / numMethods;			
		}
		
		iuc = iuc / callerClasses.size();
		
		return iuc;
	}
	
	@Override
	public String toString() {
		StringBuilder txt = new StringBuilder();
		
		txt.append(this.name);
		
		HashMap<String, Integer> callers = getCallerClasses();
		
		txt.append("\n\n\tIUC: ").append(calcIUC(callers));		
		txt.append("\n\n\tNum. of methods: ").append(methods.size()).append("\n");
		
		for (Entry<String, HashSet<String>> method : methods.entrySet()) {
			txt.append("\n\t").append(method.getValue().size()).append(" -> ").append(method.getKey());
			
			for (String caller : method.getValue()) {
				txt.append("\n\t\t").append(caller);
			}			
		}
		
		txt.append("\n\n\tNum. of callers: ").append(callers.size()).append("\n");
		
		for (Entry<String, Integer> caller : callers.entrySet()) {
			txt.append("\n\t").append(caller.getKey()).append(" -> ").append(caller.getValue());
		}
		
		return txt.toString();
	}
	
}
