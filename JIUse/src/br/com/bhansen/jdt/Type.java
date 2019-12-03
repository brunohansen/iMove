package br.com.bhansen.jdt;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class Type {
	
	private IType iType;
	private String name;
	
	public Type(IType iType) {
		this.iType = iType;
		this.name = getName(iType);
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getSimpleName() {
		return this.iType.getElementName();
	}
	
	public IType getIType() {
		return iType;
	}
	
	public Project getProject() {
		return new Project(iType.getJavaProject());
	}
		
	public static String getName(IType iType) {
		// \\$ replace the inner class separator for . and (\\.[0-9])*$ removes the anonymous class representation  
		return iType.getFullyQualifiedName().replaceAll("\\$", ".").replaceFirst("(\\.[0-9])*$", "");
	}
	
	public Method getMethod(String method) throws Exception {
		if(Method.isMovedMethodName(method)) {
			return getMovedMethod(method);
		} else {
			return getMethodBySignature(method);
		}
		
	}
	
	private Method getMovedMethod(String methodName) throws Exception {
		IMethod[] iMethods = iType.getMethods();
	
		if (Method.isMovedMethodName(methodName)) {
	
			for (IMethod iMethod : iMethods) {
				
				Method m = new Method(iMethod);
	
				if (m.isMovedMethod(methodName)) {
					return m;
				}
	
			}
		}
		
		throw new Exception("Moved method not found!");
	}
	
	private Method getMethodBySignature(String signature) throws Exception {
		
		String mSig = Signature.normalizeInnerSignature(signature);
	
		IMethod[] methods = iType.getMethods();

		for (IMethod iMethod : methods) {
			if (Method.getSignature(iMethod).equals(mSig)) {
				return new Method(iMethod);
			}
		}
		
		mSig = Signature.normalizeSignature(signature);

		for (IMethod iMethod : methods) {
			if (Method.getSignature(iMethod).equals(mSig)) {
				return new Method(iMethod);
			}
		}
	
		throw new Exception("Method " + signature + " not found!");
	}
	
	public final boolean hasField(Type type) throws JavaModelException {

		if (type == null)
			return false;

		String fInnerType = Signature.normalizeInnerSignature(type.getName());
		String fType = Signature.normalizeSignature(type.getName());

		for (IField iField : getIType().getFields()) {
			String iType = iField.toString().split(" ", 2)[0];
			
			if (iType.equals(fInnerType) || iType.equals(fType)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static String getAbbreviatedName(String name) {
		final int MIN_LENGTH = 4;
		
		if(name.length() <= MIN_LENGTH) {
			return name;
		}
		
		String abbr = name.replaceAll("[a-z_\\-]", "");
		
		if (abbr.isEmpty()) {
			abbr = name.substring(0, MIN_LENGTH - abbr.length());
		} else if (abbr.length() < MIN_LENGTH) {
			int index = name.lastIndexOf(abbr.charAt(abbr.length() - 1)) + 1;
			
			String suffix = name.substring(index);
			
			if(suffix.length() > MIN_LENGTH - abbr.length()) {
				abbr = abbr + suffix.substring(0, MIN_LENGTH - abbr.length());
			} else {
				abbr = abbr + suffix;
			}
		}
		
		return abbr;
	}

}
