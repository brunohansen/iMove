package br.com.bhansen.utils;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

public class TypeHelper {

	public static String getClassName(IType type) {
		// \\$ replace the inner class separator for . and (\\.[0-9])*$ removes the anonymous class representation  
		return type.getFullyQualifiedName().replaceAll("\\$", ".").replaceFirst("(\\.[0-9])*$", "");
	}

	public static IMethod getMovedMethod(IType type, String methodName) throws Exception {
		IMethod[] iMethods = type.getMethods();
	
		if (MethodHelper.isMovedMethod(methodName)) {
	
			for (IMethod iMethod : iMethods) {
	
				if (MethodHelper.isMovedMethod(iMethod, methodName)) {
					return iMethod;
				}
	
			}
		}
		
		throw new Exception("Moved method not found!");
	}

	public static IMethod getMethod(IType type, String signature) throws Exception {
		
		String mSig = Signature.normalizeInnerSignature(signature);
	
		IMethod[] methods = type.getMethods();
	
		for (IMethod iMethod : methods) {
			if (MethodHelper.getSignature(iMethod).equals(mSig)) {
				return iMethod;
			}
		}
		
		mSig = Signature.normalizeSignature(signature);
	
		for (IMethod iMethod : methods) {
			if (MethodHelper.getSignature(iMethod).equals(mSig)) {
				return iMethod;
			}
		}
	
		throw new Exception("Method " + signature + " not found!");
	}

}
