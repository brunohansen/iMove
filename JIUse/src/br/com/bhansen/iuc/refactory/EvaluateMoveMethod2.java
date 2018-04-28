package br.com.bhansen.iuc.refactory;

import org.eclipse.jdt.core.IType;

public class EvaluateMoveMethod2 implements MoveMethodEvaluator {
	
	private MoveMethodEvaluator actual;
	private MoveMethodEvaluator one;
	private MoveMethodEvaluator two;
	
	public EvaluateMoveMethod2(IType classFrom, String method, IType classTo) throws Exception {
		
	}
		
	public boolean shouldMove() {
		return actual.shouldMove();
	}

	@Override
	public String toString() {
		return actual.toString();
	}

	public String toLineString() {
		return actual.toLineString();
	}

}
