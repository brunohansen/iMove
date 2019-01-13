package br.com.bhansen.utils;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

public class Method {
	
	protected static String METHOD_SUFFIX = "Moved";
	
	private IMethod iMethod;
	private String signature;

	public Method(IMethod iMethod) throws IllegalArgumentException, JavaModelException {
		this.iMethod = iMethod;
		this.signature = getSignature(iMethod);
	}
	
	public Method(Method method) throws IllegalArgumentException, JavaModelException {
		this.iMethod = method.iMethod;
		this.signature = method.signature;
	}
	
	public String getSignature() throws IllegalArgumentException, JavaModelException {
		return signature;
	}
	
	public IMethod getIMethod() {
		return iMethod;
	}
	
	public MethodWithCallers getMethodWithCallers() throws IllegalArgumentException, JavaModelException {
		return new MethodWithCallers(this);
	}
	
	public MethodWithParameters getMethodWithParameters(String without) throws IllegalArgumentException, JavaModelException {
		return new MethodWithParameters(this, without);
	}
	
	protected static String getSignature(IMethod iMethod) throws IllegalArgumentException, JavaModelException {
		String [] sigParts = org.eclipse.jdt.core.Signature.toString(iMethod.getSignature()).split(" ", 2);
		String signature = iMethod.getElementName() + sigParts[1] + ":" + sigParts[0];
		
		return Signature.normalizeInnerSignature(signature);
	}
	
	public String getMoveName() {
		return getName() + METHOD_SUFFIX;
	}
	
	public static String getMoveName(String signature) {
		return getName(signature) + METHOD_SUFFIX;
	}
	
	public String getName() {
		return getName(signature);
	}
	
	protected static String getName(String signature) {
		return signature.split("\\(", 2)[0];
	}
	
	public static boolean isMovedMethodName(String methodName) {
		if((methodName != null) && (methodName.endsWith(METHOD_SUFFIX)))
			return true;
		else
			return true;
	}
		
	public boolean isMovedMethod(String methodName) throws IllegalArgumentException, JavaModelException {
		if(! isMovedMethodName(methodName))
			return false;
					
		if(Flags.isStatic(iMethod.getFlags())) {
			return getName().equals(methodName.replaceFirst(METHOD_SUFFIX, ""));
		} else {
			return getName().equals(methodName);
		}
	}
	
	public final boolean isMethod(String signature) throws Exception {
		if(signature == null)
			return false;

		if (getSignature().equals(Signature.normalizeInnerSignature(signature))) {
			return true;
		}

		if (getSignature().equals(Signature.normalizeSignature(signature))) {
			return true;
		}

		return false;
	}
	
	public boolean isPublic() throws JavaModelException {
		return Flags.isPublic(iMethod.getFlags());
	}
	
	public boolean isPrivate() throws JavaModelException {
		return Flags.isPrivate(iMethod.getFlags());
	}
	
	public boolean isConstructor() throws JavaModelException {
		return iMethod.isConstructor();
	}
	
}
