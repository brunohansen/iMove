package br.com.bhansen.iuc.metric;

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
public class MetricClass implements Metric {
	
	private static String METHOD_PREFIX = "Moved";
	
	private IType type;
	
	private String name;
	private Map<String, Set<String>> methods;
	
	public MetricClass(IType type) {
		super();
		this.type = type;
		// TODO Gatilho 
		this.name = (type == null)? "Null" : getClassName(type);
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
	
	public void removeFakes(String fakeDelegate, String fakeParameter) throws Exception {
		if(fakeDelegate != null) {
			IMethod delegate = getMethod(fakeDelegate);
			
			if(isFakeDelegate(delegate)) {
				methods.remove(getSignature(delegate));
			} else {
				removeFakeParameter(getSignature(delegate), fakeParameter);
			}
		}
	}
	
	public void removeFakeParameter(String fakeSignature, String fakeParameter) throws JavaModelException {
		
	}
	
	private IMethod getMethod(String name) throws Exception, JavaModelException {
		
		for (IMethod iMethod : getType().getMethods()) {
			String mSig = getSignature(iMethod);
			
			if(mSig.split("\\(", 2)[0].equals(name)) {
				return iMethod;
			}
		}
		
		return null;
	}
	
	private boolean isFakeDelegate(IMethod method) throws Exception, JavaModelException {

		Set<String> callers = getCallerMethods(method);
		String mSig = getSignature(method);
		
		if(callers.size() != 1) {
			return false;
		}
		
		for (String string : callers) {
			String originalName = mSig.split("\\(", 2)[0].replaceFirst("[0-9]{0,1}" + METHOD_PREFIX, "");
			
			if(originalName.equals(string.split("\\(", 2)[0])) {
				return true;
			} else {
				return false;
			}
		}				
		
		return false;
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
	
	protected Set<String> getCallerClasses(IMethod method) {
		Set<String> callerMethods = new HashSet<>();
		
		CallHierarchy callHierarchy = CallHierarchy.getDefault();

		IMember[] members = { method };

		MethodWrapper[] methodWrappers = callHierarchy.getCallerRoots(members);
		for (MethodWrapper mw : methodWrappers) {
			MethodWrapper[] mw2 = mw.getCalls(new NullProgressMonitor());
			for (MethodWrapper m : mw2) {
				IMethod im = getIMethodFromMethodWrapper(m);
				if (im != null) {
					callerMethods.add(getClassName(im.getDeclaringType()));
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
		
	public double getMetric() throws Exception {
		return getMetric(null, null);
	}
	
	public double getMetric(String fakeDelegate, String fakeParameter) throws Exception {
		removeFakes(fakeDelegate, fakeParameter);
		
		return 0f;
	}

}
