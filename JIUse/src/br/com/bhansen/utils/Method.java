package br.com.bhansen.utils;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

public class Method {
	
	protected static final String METHOD_SUFFIX = "Moved";
	private static final String STATIC_SUFFIX = "Static" + METHOD_SUFFIX;
	
	private IMethod iMethod;
	private String signature;
	private boolean isStatic;

	public Method(IMethod iMethod) throws IllegalArgumentException, JavaModelException {
		this.iMethod = iMethod;
		this.signature = getSignature(iMethod);
		this.isStatic = Flags.isStatic(iMethod.getFlags());
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
	
	public static String getSignature(IMethod iMethod) throws IllegalArgumentException, JavaModelException {
		String [] sigParts = org.eclipse.jdt.core.Signature.toString(iMethod.getSignature()).split(" ", 2);
		String signature = iMethod.getElementName() + sigParts[1] + ":" + sigParts[0];
		
		return Signature.normalizeInnerSignature(signature);
	}
	
	protected static boolean isMovedMethodName(String methodName) {
		if((methodName != null) && (methodName.endsWith(METHOD_SUFFIX)))
			return true;
		else
			return false;
	}
	
	protected static boolean isStaticMovedMethodName(String methodName) {
		if((methodName != null) && (methodName.endsWith(STATIC_SUFFIX)))
			return true;
		else
			return false;
	}
	
	public String getMoveName() throws JavaModelException {
		if(isStatic())
			return getName() + STATIC_SUFFIX;
		else
			return getName() + METHOD_SUFFIX;
	}
		
	public String getName() {
		return getName(signature);
	}
	
	protected static String getName(String signature) {
		return signature.split("\\(", 2)[0];
	}
	

		
	public boolean isMovedMethod(String methodName) throws IllegalArgumentException, JavaModelException {
		if(! isMovedMethodName(methodName))
			return false;
		
		if(isStaticMovedMethodName(methodName)) {
			if(isStatic()) {
				return getName().equals(methodName.replaceFirst(STATIC_SUFFIX, ""));
			} else {
				return false;
			}
		} else {
			if(! isStatic()) {
				return getName().equals(methodName);
			} else {
				return false;
			}
		}
	}
	
	public static boolean isMovedMethod(IMethod iMethod, String methodName) throws IllegalArgumentException, JavaModelException {
		if(! isMovedMethodName(methodName))
			return false;
		
		if(isStaticMovedMethodName(methodName)) {
			if(Flags.isStatic(iMethod.getFlags())) {
				return getName(getSignature(iMethod)).equals(methodName.replaceFirst(STATIC_SUFFIX, ""));
			} else {
				return false;
			}
		} else {
			if(! Flags.isStatic(iMethod.getFlags())) {
				return getName(getSignature(iMethod)).equals(methodName);
			} else {
				return false;
			}
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
	
	public boolean isStatic() throws JavaModelException {
		return isStatic;
	}
	
}
