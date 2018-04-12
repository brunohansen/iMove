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
	
	private String name;
	private HashMap<String, HashSet<String>> methods;
	
	public IUCClass(IType type) throws Exception {
		this.methods = new HashMap<>();		
		this.name = getClassName(type);
		
		IMethod[] methods = type.getMethods();
				
		for (IMethod method : methods) {
			
			if(this.methods.put(getSignature(method), getCallerMethods(method)) != null) {
				throw new Exception("Method " + getSignature(method) + " colision!");
			};
		}
	}
	
	public static String getClassName(IType type) {
		return type.getFullyQualifiedName().replaceAll("\\$", ".");
	}
	
	public static String getSignature(IMethod method) throws IllegalArgumentException, JavaModelException {
		String [] sigParts = Signature.toString(method.getSignature()).split(" ", 2);
		String signature = method.getElementName() + sigParts[1] + ":" + sigParts[0];
		
		return signature;
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
				callerMethods.add(m.getMember().getPath().toOSString());
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
