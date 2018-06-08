package br.com.bhansen.handler.input;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;

import br.com.bhansen.handler.JIUseHandler;
import br.com.bhansen.refactory.MMEvaluatorBuilder;

public abstract class InputMovement extends JIUseHandler {
		
	protected IType findType(IJavaProject javaProject, String fullyQualifiedName) throws Exception {
		IType iType = javaProject.findType(fullyQualifiedName);
		
		if(iType != null) {
			return iType;
		} else {
			throw new Exception("Class " + fullyQualifiedName + " not found!");
		}
	}
	
	protected MMEvaluatorBuilder move(IJavaProject javaProject, String movement) throws Exception {
		String [] parts = movement.split("::");
		
		IType classFrom = findType(javaProject, parts[0]);
		
		parts = parts[1].split("\t");
		
		IType classTo = findType(javaProject, parts[1]);
						
		return new MMEvaluatorBuilder(classFrom, parts[0], classTo);
	}

}