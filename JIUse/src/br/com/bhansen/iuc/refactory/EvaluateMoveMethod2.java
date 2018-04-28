package br.com.bhansen.iuc.refactory;

import org.eclipse.jdt.core.IType;

import br.com.bhansen.iuc.metric.MetricFactory;

public class EvaluateMoveMethod2 extends MoveMethodEvaluator {
	
	private MoveMethodEvaluator evaluator;
		
	public EvaluateMoveMethod2(IType classFrom, String method, IType classTo, MetricFactory fac1, MetricFactory fac2) throws Exception {
		super(classFrom, method, classTo);
		
		this.evaluator = new EvaluateMoveMethod1(classFrom, method, classTo, fac1);
		
		if(! this.evaluator.shouldMove()) {
			this.evaluator = new EvaluateMoveMethod1(classFrom, method, classTo, fac2);
		}
		
	}
		
	public boolean shouldMove() {
		return evaluator.shouldMove();
	}

	@Override
	public String toString() {
		return evaluator.toString();
	}

	public String toLineString() {
		return evaluator.toLineString();
	}

}
