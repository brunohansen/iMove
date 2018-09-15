package br.com.bhansen.handler.input;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;

import br.com.bhansen.handler.IMoveHandler;
import br.com.bhansen.refactory.MoveMethodEvaluator;

public abstract class InputMovement extends IMoveHandler {
		
	public static IType findType(IJavaProject javaProject, String fullyQualifiedName) throws Exception {
		IType iType = javaProject.findType(fullyQualifiedName);
		
		if(iType != null) {
			return iType;
		} else {
			throw new Exception("Class " + fullyQualifiedName + " not found!");
		}
	}
	
	protected MoveMethodEvaluator create(IJavaProject javaProject, String movement, String type, String metric) throws Exception {
		String [] parts = movement.split("::");
		
		IType classFrom = findType(javaProject, parts[0]);
		
		parts = parts[1].split("\t");
		
		IType classTo = findType(javaProject, parts[1]);
		
		return createEvaluator(classFrom, parts[0], classTo, type, metric);
						
		//return new MMEvaluatorBuilder(classFrom, parts[0], classTo, evaluator);
	}
	
}