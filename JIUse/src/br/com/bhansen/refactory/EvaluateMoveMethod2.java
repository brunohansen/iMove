package br.com.bhansen.refactory;

import org.eclipse.jdt.core.IType;

import br.com.bhansen.metric.MetricFactory;

public class EvaluateMoveMethod2 extends MoveMethodEvaluator {
	
	private MoveMethodEvaluator evaluator;
		
	public EvaluateMoveMethod2(IType classFrom, String method, IType classTo, MetricFactory fac1, MetricFactory fac2) throws Exception {
		super(classFrom, method, classTo, fac2, 0);
		
		this.evaluator = new EvaluateMoveMethod3(classFrom, method, classTo, fac1, 0);
		
		if(! this.evaluator.shouldMove()) {
			this.evaluator = new EvaluateMoveMethod3(classFrom, method, classTo, fac2, 0);
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
