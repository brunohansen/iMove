package br.com.bhansen.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.corext.callhierarchy.CallHierarchy;
import org.eclipse.jdt.internal.corext.callhierarchy.MethodWrapper;

@SuppressWarnings("restriction")
public class MethodWithCallers extends Method {
	
	private Set<String> callers;

	public MethodWithCallers(Method method) throws IllegalArgumentException, JavaModelException {
		super(method);
		callers = getCallerTypes();
	}
	
	public Set<String> getCallers() {
		return callers;
	}
	
	public void removeCaller(Type caller) {
		callers.remove(caller.getName());
	}

	public boolean isCalledOnlyBy(Type caller) {
		return (!callers.isEmpty()) && (callers.size() == 1) && (callers.contains(caller.getName()));
	}

	public boolean hasCaller() throws Exception {
		return callers.size() > 0;
	}
	
	private Set<String> getCallerTypes() throws JavaModelException {
		Set<String> callerTypes = new HashSet<>();
	
		CallHierarchy callHierarchy = CallHierarchy.getDefault();
	
		IMember[] members = { getIMethod() };
		
		//IJavaElement[] elems = { getIMethod().getJavaProject() };
		//callHierarchy.setSearchScope(SearchEngine.createJavaSearchScope(getScope(), false));
	
		MethodWrapper[] methodWrappers = callHierarchy.getCallerRoots(members);
		for (MethodWrapper mw : methodWrappers) {
			MethodWrapper[] mw2 = mw.getCalls(new NullProgressMonitor());
			for (MethodWrapper m : mw2) {
				IMethod im = getIMethodFromMethodWrapper(m);
				if (im != null) {
					callerTypes.add(Type.getName(im.getDeclaringType()));
				}
			}
		}
	
		return callerTypes;
	}
	
	private Set<String> getCallerMethods() throws IllegalArgumentException, JavaModelException {
		Set<String> callerMethods = new HashSet<>();
		
		CallHierarchy callHierarchy = CallHierarchy.getDefault();
	
		IMember[] members = { getIMethod() };
		
//		IJavaElement[] elems = { getIMethod().getJavaProject() };
//		callHierarchy.setSearchScope(SearchEngine.createJavaSearchScope(elems, false));
		
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
	
	private IJavaElement[] getScope() throws JavaModelException {
		IJavaProject project = getIMethod().getJavaProject();
		List<IJavaElement> elems = new ArrayList<IJavaElement>();
		
		for (IPackageFragmentRoot pack : project.getAllPackageFragmentRoots()) {
			if(pack.getKind() == IPackageFragmentRoot.K_SOURCE) {
				elems.add(pack);
			}
		}
		
		return elems.toArray(new IJavaElement[elems.size()]);
	} 
	
	private static IMethod getIMethodFromMethodWrapper(MethodWrapper method) {
		IMember iMember = method.getMember();
		
		if (iMember.getElementType() == IJavaElement.METHOD) {
			return (IMethod) method.getMember();
		} else {
			return null;
		}
	}
	
	private boolean isFakeDelegate(String methodName) throws IllegalArgumentException, JavaModelException {
		return isMovedMethod(methodName) && (getOriginalMethod() != null);
	}
	
	private String getOriginalMethod() throws IllegalArgumentException, JavaModelException {
		
		Set<String> callerMtds = getCallerMethods();
				
		if(callerMtds.size() != 1) {
			return null;
		}
		
		String originalName = getName().replaceFirst("[0-9]{0,1}" + METHOD_SUFFIX, "");
		
		for (String callerMtd : callerMtds) {						
			if(originalName.equals(getName(callerMtd))) {
				return callerMtd;
			} else {
				return null;
			}
		}				
		
		return null;
	}

}
