package br.com.bhansen.refactory;

import org.eclipse.jdt.core.IType;

public abstract class MoveMethodEvaluator {
	
	protected IType classFrom;
	protected IType classTo;
	protected String method;
	
	public MoveMethodEvaluator(IType classFrom, String method, IType classTo) {
		super();
		this.classFrom = classFrom;
		this.classTo = classTo;
		this.method = method;
	}

	public abstract boolean shouldMove();

	public abstract String toLineString();

}