package br.com.bhansen.metric;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.corext.callhierarchy.CallHierarchy;
import org.eclipse.jdt.internal.corext.callhierarchy.MethodWrapper;

@SuppressWarnings("restriction")
public abstract class AbsMetric implements Metric {
	
	private static String METHOD_SUFFIX = "Moved";
	
	private IType type;
	
	private String name;
	private Map<String, Set<String>> methods;
	
	public AbsMetric(IType type) {
		super();
		this.type = type;
		this.name = getClassName(type);
		this.methods = new HashMap<>();
	}

	public static String getMoveMethodName(String methodName) {
		return methodName + METHOD_SUFFIX;
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
		
	protected boolean isFakeDelegate(IMethod iMethod, String method) throws IllegalArgumentException, JavaModelException {
		return isMovedMethod(iMethod, method) && (getOriginalMethod(iMethod) != null);
	}
	
	protected boolean isMovedMethod(String name) {
		if(name == null)
			return false;

		return name.endsWith(METHOD_SUFFIX);
	}
	
	protected boolean isMovedMethod(IMethod iMethod, String name) throws IllegalArgumentException, JavaModelException {
		if(! isMovedMethod(name))
			return false;
					
		String mSig = getSignature(iMethod);
		
		if(getName(mSig).equals(name)) {
			return true;
		}
		
		return false;
	}
	
	protected boolean isMethod(IMethod iMethod, String method) throws Exception {
		if(method == null)
			return false;

		if (AbsMetric.getSignature(iMethod).equals(AbsMetric.generateInnerSignature(method))) {
			return true;
		}

		if (AbsMetric.getSignature(iMethod).equals(AbsMetric.generateSignature(method))) {
			return true;
		}

		return false;
	}
	
	public static String getName(String method) {
		return method.split("\\(", 2)[0];
	}
	
	protected String getOriginalMethod(IMethod method) throws IllegalArgumentException, JavaModelException {

		Set<String> callers = getCallerMethods(method);
				
		if(callers.size() != 1) {
			return null;
		}
		
		String mSig = getSignature(method);
		String originalName = getName(mSig).replaceFirst("[0-9]{0,1}" + METHOD_SUFFIX, "");
		
		for (String caller : callers) {						
			if(originalName.equals(getName(caller))) {
				return caller;
			} else {
				return null;
			}
		}				
		
		return null;
	}
	
	protected Set<String> getCallerMethods(IMethod method) throws IllegalArgumentException, JavaModelException {
		Set<String> callerMethods = new HashSet<>();
		
		CallHierarchy callHierarchy = CallHierarchy.getDefault();

		IMember[] members = { method };

		MethodWrapper[] methodWrappers = callHierarchy.getCallerRoots(members);
		for (MethodWrapper mw : methodWrappers) {
			MethodWrapper[] mw2 = mw.getCalls(new NullProgressMonitor());
			for (MethodWrapper m : mw2) {
				IMethod im = getIMethodFromMethodWrapper(m);
				if (im != null) {
					callerMethods.add(getSignature(im));
				}
			}
		}
		
		return callerMethods;
	}
			
	protected IMethod getIMethodFromMethodWrapper(MethodWrapper m) {
		IMember im = m.getMember();
		
		if (im.getElementType() == IJavaElement.METHOD) {
			return (IMethod) m.getMember();
		} else {
			return null;
		}
	}
	
	public String getName() {
		return name;
	}
	
	public IType getType() {
		return type;
	}
	
	public Map<String, Set<String>> getMethods() {
		return methods;
	}
	
	@Override
	public String toString() {
		try {
			return getName() + ": " + getMetric();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static double howMuchIntersect(Set<String> s1, Set<String> s2) {
		Set<String> intersection = new HashSet<>(s1);
		intersection.retainAll(s2);
	
		Set<String> union = new HashSet<>(s1);
		union.addAll(s2);
	
		if (union.size() == 0) {
			return 0;
		} else {
			return (double) intersection.size() / (double) union.size();
		}
	}

}
