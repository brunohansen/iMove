package br.com.bhansen.iuc.refactory;

import org.eclipse.jdt.core.IType;

public class EvaluateMoveMethod {
	
	private IType classFrom;
	private IType classTo;
	private String method;
	
	private EvaluateMM evaluate;
	
	public EvaluateMoveMethod(IType classFrom) throws Exception {
		this.setClassFrom(classFrom);
	}
	
	public EvaluateMoveMethod(IType classFrom, String method, IType classTo) throws Exception {
		this(classFrom);
		this.move(method, classTo);
	}
	
	public void setClassFrom(IType classFrom) {
		this.classFrom = classFrom;
	}

	public IType getClassFrom() {
		return classFrom;
	}

	public void setClassTo(IType classTo) {
		this.classTo = classTo;
	}

	public IType getClassTo() {
		return classTo;
	}

	public void move(String method) throws Exception {
		this.method = method;
		this.evaluate = create();
	}
	
	private EvaluateMM create() throws Exception {
		return new EvaluateMoveMethod1(this.classFrom, this.method, this.classTo);
	}

	public void move(String method, IType classTo) throws Exception {
		this.setClassTo(classTo);
		this.move(method);
	}

	public boolean shouldMove() {
		return this.evaluate.shouldMove();
	}

	@Override
	public String toString() {
		return this.evaluate.toString();
	}

	public String toLineString() {
		return this.evaluate.toLineString();
	}

}
