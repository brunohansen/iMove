package br.com.bhansen.utils;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.callhierarchy.CallHierarchy;
import org.eclipse.jdt.internal.corext.callhierarchy.MethodWrapper;

@SuppressWarnings("restriction")
public class CallerHelper {
		
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
	
	public static Set<String> getCallerMethods(IMethod method) throws IllegalArgumentException, JavaModelException {
		Set<String> callerMethods = new HashSet<>();
		
		CallHierarchy callHierarchy = CallHierarchy.getDefault();
	
		IMember[] members = { method };
	
		MethodWrapper[] methodWrappers = callHierarchy.getCallerRoots(members);
		for (MethodWrapper mw : methodWrappers) {
			MethodWrapper[] mw2 = mw.getCalls(new NullProgressMonitor());
			for (MethodWrapper m : mw2) {
				IMethod im = getIMethodFromMethodWrapper(m);
				if (im != null) {
					callerMethods.add(MethodHelper.getSignature(im));
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

	public static void removeCaller(Set<String> callers, IType type) {
		callers.remove(TypeHelper.getClassName(type));
	}

	public static boolean isCalledOnlyBy(Set<String> callers, IType caller) {
		return (!callers.isEmpty()) && (callers.size() == 1) && (callers.contains(TypeHelper.getClassName(caller)));
	}

	public static boolean hasNoCaller(Set<String> callers) throws Exception {
		return callers.size() == 0;
	}	
	
}
