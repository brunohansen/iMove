package br.com.bhansen.utils;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

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
	
	public IType getIType() {
		return iType;
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
	
	public Method getMethodBySignature(String signature) throws Exception {
		
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

}
