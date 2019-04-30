package br.com.bhansen.jdt;

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
	
	public boolean isAccessorMethod() {
		return Signature.isAccessorMethod(signature);
	}
	
	public MethodWithCallers getMethodWithCallers() throws IllegalArgumentException, JavaModelException {
		return new MethodWithCallers(this);
	}
	
	public MethodWithParameters getMethodWithParameters(String without) throws IllegalArgumentException, JavaModelException {
		return new MethodWithParameters(this, without);
	}
	
	public MethodWithParameters getMethodWithParameters() throws IllegalArgumentException, JavaModelException {
		return new MethodWithParameters(this);
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
	
	public boolean isMethod(String method) throws Exception {
		if(isMovedMethodName(method)) {
			return isMovedMethod(method);
		} else {
			return isMethodSignature(method);
		}
	}
			
	protected boolean isMovedMethod(String methodName) throws IllegalArgumentException, JavaModelException {
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
		
	protected final boolean isMethodSignature(String signature) throws Exception {
		if(signature == null)
			return false;
		
		String innerSig = Signature.normalizeInnerSignature(signature);
		String sig = Signature.normalizeSignature(signature);
	
		return getSignature().equals(innerSig) || getSignature().equals(sig);
	}
	
	public boolean isPublic() throws JavaModelException {
		return Flags.isPublic(iMethod.getFlags());
	}
	
	public boolean isPrivate() throws JavaModelException {
		return Flags.isPrivate(iMethod.getFlags());
	}
	
	public boolean isProtected() throws JavaModelException {
		return Flags.isProtected(iMethod.getFlags());
	}
	
	public boolean isDefault() throws JavaModelException {
		return Flags.isPackageDefault(iMethod.getFlags());
	}
	
	public boolean isConstructor() throws JavaModelException {
		return iMethod.isConstructor();
	}
	
	public boolean isStatic() throws JavaModelException {
		return isStatic;
	}
	
	public String getVisibility() throws JavaModelException {
		if (isPublic()) {
			return "+";
		} else if (isProtected()) {
			return "#";
		} else if (isPrivate()) {
			return "-";
		} else {
			return "";
		}
	}
	
	public boolean hasSameVisibility(Method method) throws JavaModelException {
		return (isPublic() && method.isPublic()) || (isProtected() && method.isProtected()) || (isPrivate() && method.isProtected());
	}
	
	public boolean hasVisibility(Method method) throws JavaModelException {
		if(isPublic() && method.isPublic()) {
			return true;
		} else if (isProtected() && ! method.isPrivate()) {
			return true;
		} else if (isPrivate()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String getAbbreviatedName(String name) {
		final int MIN_LENGTH = 4;
		
		if(name.length() <= MIN_LENGTH) {
			return name;
		}
		
		return name.substring(0, MIN_LENGTH) + name.substring(MIN_LENGTH).replaceAll("[a-z_\\-]", "");
	}
	
}
