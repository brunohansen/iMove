package br.com.bhansen.handler.input;

import br.com.bhansen.handler.IMoveHandler;
import br.com.bhansen.refactory.MoveMethodEvaluator;
import br.com.bhansen.utils.Method;
import br.com.bhansen.utils.Project;

public abstract class InputMovement extends IMoveHandler {
			
	protected MoveMethodEvaluator create(Project project, String movement, String type, String metric) throws Exception {
		return createEvaluator(project.findClassFrom(movement), Method.getSignature(movement), project.findClassTo(movement), type, metric);
	}
	
}