package br.com.bhansen.utils;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.callhierarchy.CallHierarchy;
import org.eclipse.jdt.internal.corext.callhierarchy.MethodWrapper;

@SuppressWarnings("restriction")
public class MethodHelper {

	private static String METHOD_SUFFIX = "Moved";

	public static String getSignature(IMethod method) throws IllegalArgumentException, JavaModelException {
		String [] sigParts = org.eclipse.jdt.core.Signature.toString(method.getSignature()).split(" ", 2);
		String signature = method.getElementName() + sigParts[1] + ":" + sigParts[0];
		
		return Signature.normalizeInnerSignature(signature);
	}

	public static String getMethodName(String signature) {
		return signature.split("\\(", 2)[0];
	}
	
	public static final boolean isMethod(IMethod iMethod, String signature) throws Exception {
		if(signature == null)
			return false;

		if (getSignature(iMethod).equals(Signature.normalizeInnerSignature(signature))) {
			return true;
		}

		if (getSignature(iMethod).equals(Signature.normalizeSignature(signature))) {
			return true;
		}

		return false;
	}
	
	public static final Set<String> createParametersSet(IMethod iMethod) throws IllegalArgumentException, JavaModelException {
		return createParametersSet(iMethod, TypeHelper.getClassName(iMethod.getDeclaringType()));
	}

	public static final Set<String> createParametersSet(IMethod iMethod, String className) throws IllegalArgumentException, JavaModelException {
		//String strParams = getParameters(iMethod);
		String strParams = getParametersAndReturn(iMethod);
		strParams = ParameterHelper.explodGenerics(strParams);
	
		Set<String> parameters = new HashSet<>();
	
		if (! strParams.isEmpty()) {
			for (String param : strParams.split(", ")) {
				parameters.add(param);
			}
		}
	
		//parameters = removePrimitives(parameters);
		ParameterHelper.removeSelfParameter(parameters, className);
		
		return parameters;
	}

	protected final static String getParameters(IMethod method) throws IllegalArgumentException, JavaModelException {
		return getParameters(getSignature(method));
	}

	protected final static String getParametersAndReturn(IMethod method) throws IllegalArgumentException, JavaModelException {
		String signature = getSignature(method);
		String parameters = getParameters(signature);
	
		String ret = signature.split(":", 2)[1];
	
		if (! ret.equals("void")) {
			if (! parameters.isEmpty()) {
				parameters = parameters + ", " + ret;
			} else {
				parameters = ret;
			}
		}
	
		return parameters;
	}

	private final static String getParameters(String signature) {
		return signature.replaceAll(".*\\(", "").replaceAll("\\).*", "");
	}

	private static boolean isFakeDelegate(IMethod iMethod, String methodName) throws IllegalArgumentException, JavaModelException {
		return isMovedMethod(iMethod, methodName) && (getOriginalMethod(iMethod) != null);
	}

	private static String getOriginalMethod(IMethod method) throws IllegalArgumentException, JavaModelException {
	
		Set<String> callers = getCallerMethods(method);
				
		if(callers.size() != 1) {
			return null;
		}
		
		String mSig = getSignature(method);
		String originalName = getMethodName(mSig).replaceFirst("[0-9]{0,1}" + METHOD_SUFFIX, "");
		
		for (String caller : callers) {						
			if(originalName.equals(getMethodName(caller))) {
				return caller;
			} else {
				return null;
			}
		}				
		
		return null;
	}
	
	public static Set<String> getCallerTypes(IMethod method) {
		Set<String> callerTypes = new HashSet<>();
	
		CallHierarchy callHierarchy = CallHierarchy.getDefault();
	
		IMember[] members = { method };
	
		MethodWrapper[] methodWrappers = callHierarchy.getCallerRoots(members);
		for (MethodWrapper mw : methodWrappers) {
			MethodWrapper[] mw2 = mw.getCalls(new NullProgressMonitor());
			for (MethodWrapper m : mw2) {
				IMethod im = getIMethodFromMethodWrapper(m);
				if (im != null) {
					callerTypes.add(TypeHelper.getClassName(im.getDeclaringType()));
				}
			}
		}
	
		return callerTypes;
	}

	private static Set<String> getCallerMethods(IMethod method) throws IllegalArgumentException, JavaModelException {
		Set<String> callerMethods = new HashSet<>();
		
		CallHierarchy callHierarchy = CallHierarchy.getDefault();
	
		IMember[] members = { method };
	
		MethodWrapper[] methodWrappers = callHierarchy.getCallerRoots(members);
		for (MethodWrapper mw : methodWrappers) {
			MethodWrapper[] mw2 = mw.getCalls(new NullProgressMonitor());
			for (MethodWrapper m : mw2) {
				IMethod im = MethodHelper.getIMethodFromMethodWrapper(m);
				if (im != null) {
					callerMethods.add(getSignature(im));
				}
			}
		}
		
		return callerMethods;
	}
	
	private static IMethod getIMethodFromMethodWrapper(MethodWrapper method) {
		IMember iMember = method.getMember();
		
		if (iMember.getElementType() == IJavaElement.METHOD) {
			return (IMethod) method.getMember();
		} else {
			return null;
		}
	}

	public static String getMoveMethodName(String methodName) {
		return methodName + METHOD_SUFFIX;
	}
	
	public static boolean isMovedMethod(String methodName) {
		if(methodName == null)
			return false;
	
		return methodName.endsWith(METHOD_SUFFIX);
	}

	public static boolean isMovedMethod(IMethod iMethod, String methodName) throws IllegalArgumentException, JavaModelException {
		if(! isMovedMethod(methodName))
			return false;
					
		String mSig = getSignature(iMethod);
		
		if(Flags.isStatic(iMethod.getFlags())) {
			return getMethodName(mSig).equals(methodName.replaceFirst(METHOD_SUFFIX, ""));
		} else {
			return getMethodName(mSig).equals(methodName);
		}
	}

}
